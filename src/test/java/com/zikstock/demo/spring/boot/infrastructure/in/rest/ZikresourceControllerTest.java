import com.zikstock.demo.spring.boot.Application;
import com.zikstock.demo.spring.boot.infrastructure.in.rest.dto.ZikresourceRequest;
import com.zikstock.demo.spring.boot.infrastructure.in.rest.dto.ZikresourceRequest.AddedByDto;
import com.zikstock.demo.spring.boot.infrastructure.in.rest.dto.ZikresourceRequest.TagDto;
import com.zikstock.demo.spring.boot.infrastructure.in.rest.dto.ZikresourceResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;



@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ZikresourceControllerTest {

    @org.springframework.boot.test.web.server.LocalServerPort
    private int port;

    private org.springframework.web.client.RestTemplate restTemplate;
    private String baseUrl;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        restTemplate = new org.springframework.web.client.RestTemplate();
        baseUrl = "http://localhost:" + port + "/api/zikresources";
    }

    @Test
    void should_return_created_Zikresource() {
        var request = new ZikresourceRequest(
                "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
                "Rick Astley",
                "Never Gonna Give You Up",
                "video",
                Collections.emptyList(),
                new AddedByDto("rick@roll.com", "Rick", "/users/rick")
        );

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
        var id = UUID.randomUUID();

        var response = restTemplate.getForEntity(baseUrl + "/{id}", ZikresourceResponse.class, id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().id()).isEqualTo(id);
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
        var id = UUID.randomUUID();
        var request = new ZikresourceRequest(
                "https://www.updated.com",
                "Updated Artist",
                "Updated Title",
                "tablature",
                Collections.emptyList(),
                new AddedByDto("user@test", "User", "/users/1")
        );

        var requestEntity = new HttpEntity<>(request);
        var response = restTemplate.exchange(baseUrl + "/{id}", HttpMethod.PUT, requestEntity, ZikresourceResponse.class, id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().id()).isEqualTo(id);
        assertThat(response.getBody().artist()).isEqualTo(request.artist());
        assertThat(response.getBody().title()).isEqualTo(request.title());
    }

    @Test
    void should_return_no_content_on_delete() {
        var id = UUID.randomUUID();

        var response = restTemplate.exchange(baseUrl + "/{id}", HttpMethod.DELETE, null, Void.class, id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
