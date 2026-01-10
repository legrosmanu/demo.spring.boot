package com.zikstock.demo.spring.boot.infrastructure.in.rest.dto;

import java.util.List;

public record ZikresourceRequest(
    String url,
    String artist,
    String title,
    String type,
    List<TagDto> tags,
    AddedByDto addedBy
) {
    public record TagDto(String label, String value) {}

    public record AddedByDto(String email, String displayName, String link) {}
}
