package com.gamesUP.gamesUP.service;

import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.repository.GameRepository;
import com.gamesUP.gamesUP.repository.AuthorRepository;
import com.gamesUP.gamesUP.repository.CategoryRepository;
import com.gamesUP.gamesUP.repository.PublisherRepository;
import com.gamesUP.gamesUP.service.impl.GameServiceImpl;
import com.gamesUP.gamesUP.web.dto.GameDTO;
import com.gamesUP.gamesUP.web.mapper.GameMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import org.springframework.data.jpa.domain.Specification;

class GameServiceImplEdgeTest {

    GameRepository gameRepository = Mockito.mock(GameRepository.class);
    CategoryRepository categoryRepository = Mockito.mock(CategoryRepository.class);
    PublisherRepository publisherRepository = Mockito.mock(PublisherRepository.class);
    AuthorRepository authorRepository = Mockito.mock(AuthorRepository.class);
    GameMapper mapper = new GameMapper();

    GameServiceImpl service = new GameServiceImpl(gameRepository, categoryRepository, publisherRepository, authorRepository, mapper);

    @Test
    @DisplayName("get throws NOT_FOUND when missing")
    void getNotFound() {
        Mockito.when(gameRepository.findById(999)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.get(999))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Game not found");
    }

    @Test
    @DisplayName("delete throws NOT_FOUND when missing")
    void deleteNotFound() {
        Mockito.when(gameRepository.existsById(999)).thenReturn(false);
        assertThatThrownBy(() -> service.delete(999))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Game not found");
    }

    @Test
    @DisplayName("searchAdvanced aggregates specifications and returns mapped page")
    void searchAdvanced() {
        Game g = new Game();
        g.id = 1; g.nom = "Gloomhaven"; g.price = new BigDecimal("100");
        Mockito.when(gameRepository.findAll(Mockito.<Specification<Game>>any(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(g), PageRequest.of(0,10),1));
        var page = service.searchAdvanced("gloom", null, null, null, null, null, null, PageRequest.of(0,10));
        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.getContent().get(0).nom).isEqualTo("Gloomhaven");
    }

    @Test
    @DisplayName("update throws NOT_FOUND if game absent")
    void updateMissing() {
        Mockito.when(gameRepository.findById(5)).thenReturn(Optional.empty());
        GameDTO dto = new GameDTO();
        assertThatThrownBy(() -> service.update(5, dto))
            .isInstanceOf(ResponseStatusException.class)
            .hasMessageContaining("Game not found");
    }

    @Test
    @DisplayName("create saves mapped entity and returns DTO")
    void createSaves() {
        GameDTO dto = new GameDTO();
        dto.nom = "Azul"; dto.numEdition = 1; dto.genre = "Abstract"; dto.auteur = "Designer";
        Game saved = new Game(); saved.id = 10; saved.nom = dto.nom; saved.numEdition = dto.numEdition; saved.genre = dto.genre; saved.auteur = dto.auteur;
        Mockito.when(gameRepository.save(any(Game.class))).thenReturn(saved);
        GameDTO out = service.create(dto);
        assertThat(out.id).isEqualTo(10);
        assertThat(out.nom).isEqualTo("Azul");
    }
}
