package com.gamesUP.gamesUP.web.controller;

import com.gamesUP.gamesUP.service.CategoryService;
import com.gamesUP.gamesUP.web.dto.CategoryDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @GetMapping
    public Page<CategoryDTO> list(Pageable pageable) {
        return service.list(pageable);
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> create(@Valid @RequestBody CategoryDTO dto, UriComponentsBuilder uriBuilder) {
        CategoryDTO created = service.create(dto);
        return ResponseEntity.created(
                uriBuilder.path("/api/categories/{id}").buildAndExpand(created.id).toUri()
        ).body(created);
    }

    @GetMapping("/{id}")
    public CategoryDTO get(@PathVariable Long id) {
        return service.get(id);
    }

    @PutMapping("/{id}")
    public CategoryDTO update(@PathVariable Long id, @Valid @RequestBody CategoryDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(org.springframework.http.HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
