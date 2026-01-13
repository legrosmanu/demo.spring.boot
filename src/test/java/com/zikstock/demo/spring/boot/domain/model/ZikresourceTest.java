package com.zikstock.demo.spring.boot.domain.model;

import com.zikstock.demo.spring.boot.domain.exception.InvalidZikresourceException;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ZikresourceTest {

    @Test
    void should_create_valid_zikresource() {
        var zikresource = new Zikresource(
                UUID.randomUUID(),
                "http://url.com",
                "Artist",
                "Title",
                "video",
                Collections.emptyList(),
                new Zikresource.AddedBy("test@test.com", "Test", "link"));

        assertThat(zikresource).isNotNull();
    }

    @Test
    void should_throw_exception_when_url_is_missing() {
        assertThatThrownBy(() -> new Zikresource(
                UUID.randomUUID(),
                "",
                "Artist",
                "Title",
                "video",
                Collections.emptyList(),
                new Zikresource.AddedBy("test@test.com", "Test", "link")))
                .isInstanceOf(InvalidZikresourceException.class)
                .hasMessage("url");
    }

    @Test
    void should_throw_exception_when_artist_is_missing() {
        assertThatThrownBy(() -> new Zikresource(
                UUID.randomUUID(),
                "http://url.com",
                "",
                "Title",
                "video",
                Collections.emptyList(),
                new Zikresource.AddedBy("test@test.com", "Test", "link")))
                .isInstanceOf(InvalidZikresourceException.class)
                .hasMessage("artist");
    }

    @Test
    void should_throw_exception_when_title_is_missing() {
        assertThatThrownBy(() -> new Zikresource(
                UUID.randomUUID(),
                "http://url.com",
                "Artist",
                null,
                "video",
                Collections.emptyList(),
                new Zikresource.AddedBy("test@test.com", "Test", "link")))
                .isInstanceOf(InvalidZikresourceException.class)
                .hasMessage("title");
    }
}
