package com.zikstock.demo.spring.boot.infrastructure.out.persistence;

import com.zikstock.demo.spring.boot.domain.model.Zikresource;
import com.zikstock.demo.spring.boot.domain.model.ZikresourceIdentifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryZikresourceRepositoryTest {

    private InMemoryZikresourceRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryZikresourceRepository();
    }

    @Test
    void should_save_and_find_zikresource() {
        // Given
        var id = UUID.randomUUID();
        var zikresource = new Zikresource(
                id,
                "https://example.com",
                "Artist",
                "Title",
                "Type",
                Collections.emptyList(),
                null
        );

        // When
        repository.save(zikresource);
        var result = repository.findById(new ZikresourceIdentifier(id));

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(zikresource);
    }

    @Test
    void should_return_all_zikresources() {
        // Given
        var id1 = UUID.randomUUID();
        var zikresource1 = new Zikresource(
                id1,
                "https://example.com/1",
                "Artist1",
                "Title1",
                "Type",
                Collections.emptyList(),
                null
        );
        var id2 = UUID.randomUUID();
        var zikresource2 = new Zikresource(
                id2,
                "https://example.com/2",
                "Artist2",
                "Title2",
                "Type",
                Collections.emptyList(),
                null
        );

        repository.save(zikresource1);
        repository.save(zikresource2);

        // When
        var result = repository.findAll();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyInAnyOrder(zikresource1, zikresource2);
    }

    @Test
    void should_delete_zikresource() {
        // Given
        var id = UUID.randomUUID();
        var zikresource = new Zikresource(
                id,
                "https://example.com",
                "Artist",
                "Title",
                "Type",
                Collections.emptyList(),
                null
        );
        repository.save(zikresource);

        // When
        repository.delete(new ZikresourceIdentifier(id));

        // Then
        assertThat(repository.findById(new ZikresourceIdentifier(id))).isEmpty();
    }

    @Test
    void should_return_empty_when_not_found() {
        // Given
        var id = UUID.randomUUID();

        // When
        var result = repository.findById(new ZikresourceIdentifier(id));

        // Then
        assertThat(result).isEmpty();
    }
}
