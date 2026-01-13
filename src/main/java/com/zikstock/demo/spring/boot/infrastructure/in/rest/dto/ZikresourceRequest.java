package com.zikstock.demo.spring.boot.infrastructure.in.rest.dto;

import java.util.List;
import jakarta.validation.constraints.NotBlank;

public record ZikresourceRequest(
        @NotBlank String url,
        @NotBlank String artist,
        @NotBlank String title,
        String type,
        List<TagDto> tags,
        AddedByDto addedBy) {
    public record TagDto(String label, String value) {
    }

    public record AddedByDto(String email, String displayName, String link) {
    }
}
