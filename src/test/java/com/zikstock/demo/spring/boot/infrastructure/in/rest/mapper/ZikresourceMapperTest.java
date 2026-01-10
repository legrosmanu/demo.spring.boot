package com.zikstock.demo.spring.boot.infrastructure.in.rest.mapper;

import com.zikstock.demo.spring.boot.domain.model.Zikresource;
import com.zikstock.demo.spring.boot.infrastructure.in.rest.dto.ZikresourceRequest;
import com.zikstock.demo.spring.boot.infrastructure.in.rest.dto.ZikresourceResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ZikresourceMapperTest {

    private ZikresourceMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ZikresourceMapper();
    }

    @Test
    void should_map_request_to_domain() {
        // Given
        var id = UUID.randomUUID();
        var request = new ZikresourceRequest(
                "https://example.com",
                "Artist Name",
                "Song Title",
                "VIDEO",
                List.of(new ZikresourceRequest.TagDto("genre", "rock")),
                new ZikresourceRequest.AddedByDto("user@example.com", "User", "/users/1")
        );

        // When
        var result = mapper.toDomain(request, id);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(id);
        assertThat(result.url()).isEqualTo("https://example.com");
        assertThat(result.artist()).isEqualTo("Artist Name");
        assertThat(result.title()).isEqualTo("Song Title");
        assertThat(result.type()).isEqualTo("VIDEO");
        assertThat(result.tags()).hasSize(1);
        assertThat(result.tags().get(0).label()).isEqualTo("genre");
        assertThat(result.tags().get(0).value()).isEqualTo("rock");
        assertThat(result.addedBy()).isNotNull();
        assertThat(result.addedBy().email()).isEqualTo("user@example.com");
    }

    @Test
    void should_return_null_when_request_is_null() {
        // Given
        ZikresourceRequest request = null;

        // When
        var result = mapper.toDomain(request, UUID.randomUUID());

        // Then
        assertThat(result).isNull();
    }

    @Test
    void should_handle_null_tags_in_request() {
        // Given
        var id = UUID.randomUUID();
        var request = new ZikresourceRequest(
                "https://example.com",
                "Artist",
                "Title",
                "VIDEO",
                null,
                null
        );

        // When
        var result = mapper.toDomain(request, id);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.tags()).isEmpty();
        assertThat(result.addedBy()).isNull();
    }

    @Test
    void should_map_domain_to_response() {
        // Given
        var id = UUID.randomUUID();
        var domain = new Zikresource(
                id,
                "https://example.com",
                "Artist Name",
                "Song Title",
                "VIDEO",
                List.of(new Zikresource.Tag("genre", "rock")),
                new Zikresource.AddedBy("user@example.com", "User", "/users/1")
        );

        // When
        var result = mapper.toResponse(domain);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(id);
        assertThat(result.url()).isEqualTo("https://example.com");
        assertThat(result.artist()).isEqualTo("Artist Name");
        assertThat(result.title()).isEqualTo("Song Title");
        assertThat(result.type()).isEqualTo("VIDEO");
        assertThat(result.tags()).hasSize(1);
        assertThat(result.tags().get(0).label()).isEqualTo("genre");
        assertThat(result.tags().get(0).value()).isEqualTo("rock");
        assertThat(result.addedBy()).isNotNull();
        assertThat(result.addedBy().email()).isEqualTo("user@example.com");
    }

    @Test
    void should_return_null_when_domain_is_null() {
        // Given
        Zikresource domain = null;

        // When
        var result = mapper.toResponse(domain);

        // Then
        assertThat(result).isNull();
    }

    @Test
    void should_handle_null_tags_in_domain() {
        // Given
        var id = UUID.randomUUID();
        var domain = new Zikresource(
                id,
                "https://example.com",
                "Artist",
                "Title",
                "VIDEO",
                null,
                null
        );

        // When
        var result = mapper.toResponse(domain);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.tags()).isEmpty();
        assertThat(result.addedBy()).isNull();
    }

    @Test
    void should_map_empty_tags_list() {
        // Given
        var id = UUID.randomUUID();
        var request = new ZikresourceRequest(
                "https://example.com",
                "Artist",
                "Title",
                "VIDEO",
                Collections.emptyList(),
                new ZikresourceRequest.AddedByDto("user@example.com", "User", "/users/1")
        );

        // When
        var result = mapper.toDomain(request, id);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.tags()).isEmpty();
    }
}
