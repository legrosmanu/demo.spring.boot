package com.zikstock.demo.spring.boot.infrastructure.in.rest.mapper;

import com.zikstock.demo.spring.boot.domain.model.Zikresource;
import com.zikstock.demo.spring.boot.infrastructure.in.rest.dto.ZikresourceRequest;
import com.zikstock.demo.spring.boot.infrastructure.in.rest.dto.ZikresourceResponse;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ZikresourceMapper {

    public Zikresource toDomain(ZikresourceRequest request, UUID id) {
        if (request == null) {
            return null;
        }
        return new Zikresource(
                id,
                request.url(),
                request.artist(),
                request.title(),
                request.type(),
                toDomainTags(request.tags()),
                toDomainAddedBy(request.addedBy())
        );
    }

    public ZikresourceResponse toResponse(Zikresource domain) {
        if (domain == null) {
            return null;
        }
        return new ZikresourceResponse(
                domain.id(),
                domain.url(),
                domain.artist(),
                domain.title(),
                domain.type(),
                toResponseTags(domain.tags()),
                toResponseAddedBy(domain.addedBy())
        );
    }

    private List<Zikresource.Tag> toDomainTags(List<ZikresourceRequest.TagDto> tagDtos) {
        if (tagDtos == null) {
            return Collections.emptyList();
        }
        return tagDtos.stream()
                .map(dto -> new Zikresource.Tag(dto.label(), dto.value()))
                .collect(Collectors.toList());
    }

    private List<ZikresourceResponse.TagDto> toResponseTags(List<Zikresource.Tag> tags) {
        if (tags == null) {
            return Collections.emptyList();
        }
        return tags.stream()
                .map(tag -> new ZikresourceResponse.TagDto(tag.label(), tag.value()))
                .collect(Collectors.toList());
    }

    private Zikresource.AddedBy toDomainAddedBy(ZikresourceRequest.AddedByDto dto) {
        if (dto == null) {
            return null;
        }
        return new Zikresource.AddedBy(dto.email(), dto.displayName(), dto.link());
    }

    private ZikresourceResponse.AddedByDto toResponseAddedBy(Zikresource.AddedBy addedBy) {
        if (addedBy == null) {
            return null;
        }
        return new ZikresourceResponse.AddedByDto(addedBy.email(), addedBy.displayName(), addedBy.link());
    }
}
