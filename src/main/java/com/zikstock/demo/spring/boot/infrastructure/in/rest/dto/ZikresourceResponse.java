package com.zikstock.demo.spring.boot.infrastructure.in.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.UUID;

public record ZikresourceResponse(
    @JsonProperty("_id") UUID id,
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
