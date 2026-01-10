package com.zikstock.demo.spring.boot.infrastructure.in.rest;

import com.zikstock.demo.spring.boot.infrastructure.in.rest.dto.ZikresourceRequest;
import com.zikstock.demo.spring.boot.infrastructure.in.rest.dto.ZikresourceResponse;
import com.zikstock.demo.spring.boot.infrastructure.in.rest.dto.ZikresourceResponse.TagDto;
import com.zikstock.demo.spring.boot.infrastructure.in.rest.dto.ZikresourceResponse.AddedByDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/zikresources")
public class ZikresourceController {

    @PostMapping
    public ResponseEntity<ZikresourceResponse> create(@RequestBody ZikresourceRequest request) {
        // Mock implementation
        var response = new ZikresourceResponse(
                UUID.randomUUID(),
                request.url(),
                request.artist(),
                request.title(),
                request.type(),
                Collections.emptyList(),
                new AddedByDto("test@test.com", "mock user", "/users/1")
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ZikresourceResponse> getById(@PathVariable UUID id) {
        // Mock implementation
        var response = new ZikresourceResponse(
                id,
                "https://www.songsterr.com/a/wsa/tool-sober-tab-s19923t2",
                "Tool",
                "Sober",
                "tablature",
                List.of(new TagDto("difficulty", "intermediate")),
                new AddedByDto("test@test.com", "what a user", "/users/e6c14f7c-ee7a-79e8-a0cf-5ff67e2224el")
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ZikresourceResponse>> getAll() {
        // Mock implementation
        return ResponseEntity.ok(Collections.emptyList());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ZikresourceResponse> update(@PathVariable UUID id, @RequestBody ZikresourceRequest request) {
        // Mock implementation
        var response = new ZikresourceResponse(
                id,
                request.url(),
                request.artist(),
                request.title(),
                request.type(),
                Collections.emptyList(), // Mock
                new AddedByDto("test@test.com", "mock user", "/users/1")
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        // Mock implementation
        return ResponseEntity.noContent().build();
    }
}
