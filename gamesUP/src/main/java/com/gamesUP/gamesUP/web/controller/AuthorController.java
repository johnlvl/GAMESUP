package com.gamesUP.gamesUP.web.controller;

import com.gamesUP.gamesUP.service.AuthorService;
import com.gamesUP.gamesUP.web.dto.AuthorDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {
    private final AuthorService service;

    public AuthorController(AuthorService service) {
        this.service = service;
    }

    @GetMapping
    public Page<AuthorDTO> list(Pageable pageable) {
        return service.list(pageable);
    }

    @PostMapping
    public ResponseEntity<AuthorDTO> create(@Valid @RequestBody AuthorDTO dto, UriComponentsBuilder uriBuilder) {
        AuthorDTO created = service.create(dto);
        return ResponseEntity.created(
                uriBuilder.path("/api/authors/{id}").buildAndExpand(created.id).toUri()
        ).body(created);
    }
}
