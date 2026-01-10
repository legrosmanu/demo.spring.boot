package com.zikstock.demo.spring.boot.domain.model;

import java.util.List;
import java.util.UUID;

public record Zikresource(
    UUID id,
    String url,
    String artist,
    String title,
    String type,
    List<Tag> tags,
    AddedBy addedBy
) {
    public record Tag(String label, String value) {}

    public record AddedBy(String email, String displayName, String link) {}
}
