package com.gamesUP.gamesUP.web.controller;

import com.gamesUP.gamesUP.service.PublisherService;
import com.gamesUP.gamesUP.web.dto.PublisherDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/publishers")
public class PublisherController {
    private final PublisherService service;

    public PublisherController(PublisherService service) {
        this.service = service;
    }

    @GetMapping
    public Page<PublisherDTO> list(Pageable pageable) {
        return service.list(pageable);
    }

    @PostMapping
    public ResponseEntity<PublisherDTO> create(@Valid @RequestBody PublisherDTO dto, UriComponentsBuilder uriBuilder) {
        PublisherDTO created = service.create(dto);
        return ResponseEntity.created(
                uriBuilder.path("/api/publishers/{id}").buildAndExpand(created.id).toUri()
        ).body(created);
    }
}
