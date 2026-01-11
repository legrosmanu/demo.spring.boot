package com.zikstock.demo.spring.boot.domain.service;

import com.zikstock.demo.spring.boot.domain.model.Zikresource;
import com.zikstock.demo.spring.boot.domain.model.ZikresourceIdentifier;
import com.zikstock.demo.spring.boot.domain.out.ZikresourceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.zikstock.demo.spring.boot.domain.exception.ZikresourceNotFound;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

class ZikresourceServiceTest {

    @Mock
    private ZikresourceRepository repository;

    @InjectMocks
    private ZikresourceService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void should_create_zikresource() {
        // Given
        var zikresource = mock(Zikresource.class);

        // When
        service.create(zikresource);

        // Then
        then(repository).should().save(zikresource);
    }

    @Test
    void should_update_zikresource() {
        // Given
        var zikresource = mock(Zikresource.class);

        // When
        service.update(zikresource);

        // Then
        then(repository).should().save(zikresource);
    }

    @Test
    void should_delete_zikresource() {
        // Given
        var id = UUID.randomUUID();
        var zikresource = mock(Zikresource.class);
        given(zikresource.id()).willReturn(id);

        // When
        service.delete(zikresource);

        // Then
        then(repository).should().delete(new ZikresourceIdentifier(id));
    }

    @Test
    void should_get_all_zikresources() {
        // Given
        var zikresource = mock(Zikresource.class);
        given(repository.findAll()).willReturn(List.of(zikresource));

        // When
        var result = service.getZikresources();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result).contains(zikresource);
    }

    @Test
    void should_get_zikresource_by_id() {
        // Given
        var id = UUID.randomUUID();
        var identifier = new ZikresourceIdentifier(id);
        var zikresource = mock(Zikresource.class);
        given(repository.findById(identifier)).willReturn(Optional.of(zikresource));

        // When
        var result = service.getZikresource(identifier);

        // Then
        assertThat(result).isEqualTo(zikresource);
    }

    @Test
    void should_throw_exception_when_zikresource_not_found() {
        // Given
        var id = UUID.randomUUID();
        var identifier = new ZikresourceIdentifier(id);
        given(repository.findById(identifier)).willReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> service.getZikresource(identifier))
                .isInstanceOf(ZikresourceNotFound.class)
                .hasMessage("Zikresource with id " + id + " not found");
    }
}
