package com.zikstock.demo.spring.boot.infrastructure.in.rest;

import com.zikstock.demo.spring.boot.Application;
import com.zikstock.demo.spring.boot.domain.model.ZikresourceIdentifier;
import com.zikstock.demo.spring.boot.domain.out.ZikresourceRepository;
import com.zikstock.demo.spring.boot.infrastructure.in.rest.dto.ZikresourceRequest;
import com.zikstock.demo.spring.boot.infrastructure.in.rest.dto.ZikresourceRequest.AddedByDto;
import com.zikstock.demo.spring.boot.infrastructure.in.rest.dto.ZikresourceResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import java.util.Collections;
import org.springframework.web.client.HttpClientErrorException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ZikresourceControllerTest {

    @org.springframework.boot.test.web.server.LocalServerPort
    private int port;

    @Autowired
    private ZikresourceRepository repository;

    private org.springframework.web.client.RestTemplate restTemplate;
    private String baseUrl;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        restTemplate = new org.springframework.web.client.RestTemplate();
        baseUrl = "http://localhost:" + port + "/api/zikresources";

        // Clear repository
        repository.findAll().forEach(
                z -> repository.delete(new ZikresourceIdentifier(z.id())));
    }

    @Test
    void should_return_created_Zikresource() {
        var request = new ZikresourceRequest(
                "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
                "Rick Astley",
                "Never Gonna Give You Up",
                "video",
                Collections.emptyList(),
                new AddedByDto("rick@roll.com", "Rick", "/users/rick"));

        var response = restTemplate.postForEntity(baseUrl, request, ZikresourceResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().id()).isNotNull();
        assertThat(response.getBody().url()).isEqualTo(request.url());
        assertThat(response.getBody().artist()).isEqualTo(request.artist());
        assertThat(response.getBody().title()).isEqualTo(request.title());
    }

    @Test
    void should_return_Zikresource_by_id() {
        // Given
        var request = new ZikresourceRequest(
                "https://example.com/tool",
                "Tool",
                "Sober",
                "video",
                Collections.emptyList(),
                new AddedByDto("tool@fan.com", "Fan", "/users/fan"));
        var created = restTemplate.postForEntity(baseUrl, request, ZikresourceResponse.class).getBody();

        // When
        var response = restTemplate.getForEntity(baseUrl + "/{id}", ZikresourceResponse.class, created.id());

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().id()).isEqualTo(created.id());
        assertThat(response.getBody().artist()).isEqualTo("Tool");
        assertThat(response.getBody().title()).isEqualTo("Sober");
    }

    @Test
    void should_return_empty_list() {
        var response = restTemplate.getForEntity(baseUrl, ZikresourceResponse[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEmpty();
    }

    @Test
    void should_return_updated_Zikresource() {
        // Given
        var request = new ZikresourceRequest(
                "https://example.com/old",
                "Old Artist",
                "Old Title",
                "video",
                Collections.emptyList(),
                new AddedByDto("user@test", "User", "/users/1"));
        var created = restTemplate.postForEntity(baseUrl, request, ZikresourceResponse.class).getBody();

        var updateRequest = new ZikresourceRequest(
                "https://www.updated.com",
                "Updated Artist",
                "Updated Title",
                "tablature",
                Collections.emptyList(),
                new AddedByDto("user@test", "User", "/users/1"));

        // When
        var requestEntity = new HttpEntity<>(updateRequest);
        var response = restTemplate.exchange(baseUrl + "/{id}", HttpMethod.PUT, requestEntity,
                ZikresourceResponse.class, created.id());

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().id()).isEqualTo(created.id());
        assertThat(response.getBody().artist()).isEqualTo("Updated Artist");
        assertThat(response.getBody().title()).isEqualTo("Updated Title");
    }

    @Test
    void should_return_no_content_on_delete() {
        // Given
        var request = new ZikresourceRequest(
                "https://example.com/delete",
                "Delete Me",
                "Delete Title",
                "video",
                Collections.emptyList(),
                new AddedByDto("user@test", "User", "/users/1"));
        var created = restTemplate.postForEntity(baseUrl, request, ZikresourceResponse.class).getBody();

        // When
        var response = restTemplate.exchange(baseUrl + "/{id}", HttpMethod.DELETE, null, Void.class, created.id());

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        // Verify it is gone
        assertThatThrownBy(() -> restTemplate.getForEntity(baseUrl + "/{id}", ZikresourceResponse.class, created.id()))
                .isInstanceOf(HttpClientErrorException.NotFound.class);
    }

    @Test
    void should_return_formatted_error_when_request_is_invalid() {
        // Given
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>("", headers); // Empty body to trigger error

        // When
        try {
            restTemplate.postForEntity(baseUrl, request, ZikresourceResponse.class);
        } catch (HttpClientErrorException.BadRequest ex) {
            // Then
            assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            var responseBody = ex
                    .getResponseBodyAs(com.zikstock.demo.spring.boot.infrastructure.in.rest.exception.ApiError.class);
            assertThat(responseBody).isNotNull();
            assertThat(responseBody.status()).isEqualTo(400);
            assertThat(responseBody.error()).isEqualTo("Bad Request");
            assertThat(responseBody.message()).contains("Required request body is missing");
            assertThat(responseBody.timestamp()).isNotNull();
        }
    }

    @Test
    void should_return_not_found_when_updating_non_existent_zikresource() {
        // Given
        var id = java.util.UUID.randomUUID();
        var updateRequest = new ZikresourceRequest(
                "https://www.updated.com",
                "Updated Artist",
                "Updated Title",
                "tablature",
                Collections.emptyList(),
                new AddedByDto("user@test", "User", "/users/1"));

        var requestEntity = new HttpEntity<>(updateRequest);

        // When
        try {
            restTemplate.exchange(baseUrl + "/{id}", HttpMethod.PUT, requestEntity,
                    ZikresourceResponse.class, id);
        } catch (HttpClientErrorException.NotFound ex) {
            // Then
            assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            var responseBody = ex
                    .getResponseBodyAs(com.zikstock.demo.spring.boot.infrastructure.in.rest.exception.ApiError.class);
            assertThat(responseBody).isNotNull();
            assertThat(responseBody.status()).isEqualTo(404);
            assertThat(responseBody.error()).isEqualTo("Not Found");
            assertThat(responseBody.message()).isEqualTo("Zikresource with id " + id + " not found");
            assertThat(responseBody.timestamp()).isNotNull();
        }
    }

    @Test
    void should_return_no_content_when_deleting_non_existent_zikresource() {
        // Given
        var id = java.util.UUID.randomUUID();

        // When
        var response = restTemplate.exchange(baseUrl + "/{id}", HttpMethod.DELETE, null, Void.class, id);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
