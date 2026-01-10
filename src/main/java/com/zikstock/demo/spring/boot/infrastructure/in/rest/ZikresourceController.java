package com.zikstock.demo.spring.boot.infrastructure.in.rest;

import com.zikstock.demo.spring.boot.domain.in.ZikresourceCommand;
import com.zikstock.demo.spring.boot.domain.in.ZikresourceQuery;
import com.zikstock.demo.spring.boot.domain.model.ZikresourceIdentifier;
import com.zikstock.demo.spring.boot.infrastructure.in.rest.dto.ZikresourceResponse;
import com.zikstock.demo.spring.boot.infrastructure.in.rest.dto.ZikresourceRequest;
import com.zikstock.demo.spring.boot.infrastructure.in.rest.mapper.ZikresourceMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/zikresources")
public class ZikresourceController {

    private final ZikresourceCommand zikresourceCommand;
    private final ZikresourceQuery zikresourceQuery;
    private final ZikresourceMapper zikresourceMapper;

    public ZikresourceController(ZikresourceCommand zikresourceCommand,
                                 ZikresourceQuery zikresourceQuery,
                                 ZikresourceMapper zikresourceMapper) {
        this.zikresourceCommand = zikresourceCommand;
        this.zikresourceQuery = zikresourceQuery;
        this.zikresourceMapper = zikresourceMapper;
    }

    @PostMapping
    public ResponseEntity<ZikresourceResponse> create(@RequestBody ZikresourceRequest request) {
        var domainObject = zikresourceMapper.toDomain(request, UUID.randomUUID());
        zikresourceCommand.create(domainObject);
        return new ResponseEntity<>(zikresourceMapper.toResponse(domainObject), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ZikresourceResponse> getById(@PathVariable UUID id) {
        var domainObject = zikresourceQuery.getZikresource(new ZikresourceIdentifier(id));
        if (domainObject == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(zikresourceMapper.toResponse(domainObject));
    }

    @GetMapping
    public ResponseEntity<List<ZikresourceResponse>> getAll() {
        var domainObjects = zikresourceQuery.getZikresources();
        var responseDtos = domainObjects.stream()
                .map(zikresourceMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ZikresourceResponse> update(@PathVariable UUID id, @RequestBody ZikresourceRequest request) {
        var domainObject = zikresourceMapper.toDomain(request, id);
        // Ensure availability before update? Or just update. 
        // Assuming update implies existence check in domain or we check here.
        // For REST, usually we check if exists.
        var existing = zikresourceQuery.getZikresource(new ZikresourceIdentifier(id));
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        
        zikresourceCommand.update(domainObject);
        return ResponseEntity.ok(zikresourceMapper.toResponse(domainObject));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        var existing = zikresourceQuery.getZikresource(new ZikresourceIdentifier(id));
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        zikresourceCommand.delete(existing);
        return ResponseEntity.noContent().build();
    }
}
