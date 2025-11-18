package com.gamesUP.gamesUP.web.controller;

import com.gamesUP.gamesUP.service.GameService;
import com.gamesUP.gamesUP.web.dto.GameDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

import java.math.BigDecimal;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/games")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    public Page<GameDTO> list(Pageable pageable) {
        return gameService.list(pageable);
    }

    @GetMapping("/search")
    public Page<GameDTO> search(@RequestParam(required = false) String q,
                                @RequestParam(required = false) Long categoryId,
                                @RequestParam(required = false) Long publisherId,
                                @RequestParam(required = false) Long authorId,
                                @RequestParam(required = false) BigDecimal priceMin,
                                @RequestParam(required = false) BigDecimal priceMax,
                                @RequestParam(required = false) Boolean inStock,
                                @PageableDefault Pageable pageable) {
        return gameService.searchAdvanced(q, categoryId, publisherId, authorId, priceMin, priceMax, inStock, pageable);
    }

    @GetMapping("/{id}")
    public GameDTO get(@PathVariable Integer id) {
        return gameService.get(id);
    }

    @PostMapping
    public ResponseEntity<GameDTO> create(@Valid @RequestBody GameDTO dto, UriComponentsBuilder uriBuilder) {
        GameDTO created = gameService.create(dto);
        return ResponseEntity.created(
                uriBuilder.path("/api/games/{id}").buildAndExpand(created.id).toUri()
        ).body(created);
    }

    @PutMapping("/{id}")
    public GameDTO update(@PathVariable Integer id, @Valid @RequestBody GameDTO dto) {
        return gameService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        gameService.delete(id);
    }
}
