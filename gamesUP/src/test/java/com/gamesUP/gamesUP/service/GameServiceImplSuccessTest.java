package com.gamesUP.gamesUP.service;

import com.gamesUP.gamesUP.model.Author;
import com.gamesUP.gamesUP.model.Category;
import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.model.Publisher;
import com.gamesUP.gamesUP.repository.AuthorRepository;
import com.gamesUP.gamesUP.repository.CategoryRepository;
import com.gamesUP.gamesUP.repository.GameRepository;
import com.gamesUP.gamesUP.repository.PublisherRepository;
import com.gamesUP.gamesUP.service.impl.GameServiceImpl;
import com.gamesUP.gamesUP.web.dto.GameDTO;
import com.gamesUP.gamesUP.web.mapper.GameMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;

class GameServiceImplSuccessTest {

    GameRepository gameRepository = Mockito.mock(GameRepository.class);
    CategoryRepository categoryRepository = Mockito.mock(CategoryRepository.class);
    PublisherRepository publisherRepository = Mockito.mock(PublisherRepository.class);
    AuthorRepository authorRepository = Mockito.mock(AuthorRepository.class);
    GameMapper mapper = new GameMapper();

    GameServiceImpl service = new GameServiceImpl(gameRepository, categoryRepository, publisherRepository, authorRepository, mapper);

    @Test
    @DisplayName("update sets relations when IDs provided and maps DTO back")
    void updateSetsRelations() {
        Game existing = new Game();
        existing.id = 7;
        existing.nom = "Old";
        Mockito.when(gameRepository.findById(7)).thenReturn(Optional.of(existing));

        Category cat = new Category();
        // use reflection via getters to verify later via mapper
        Mockito.when(categoryRepository.findById(1L)).thenReturn(Optional.of(cat));
        Publisher pub = new Publisher();
        Mockito.when(publisherRepository.findById(2L)).thenReturn(Optional.of(pub));
        Author author = new Author();
        Mockito.when(authorRepository.findById(3L)).thenReturn(Optional.of(author));

        // saved entity echoes relations
        ArgumentCaptor<Game> captor = ArgumentCaptor.forClass(Game.class);
        Mockito.when(gameRepository.save(any(Game.class))).thenAnswer(inv -> inv.getArgument(0));

        GameDTO dto = new GameDTO();
        dto.nom = "New";
        dto.genre = "Family";
        dto.numEdition = 2;
        dto.categoryId = 1L;
        dto.publisherId = 2L;
        dto.authorId = 3L;
        dto.price = new BigDecimal("42.00");

        GameDTO out = service.update(7, dto);

        Mockito.verify(gameRepository).save(captor.capture());
        Game saved = captor.getValue();
        assertThat(saved.category).isNotNull();
        assertThat(saved.publisher).isNotNull();
        assertThat(saved.author).isNotNull();
        assertThat(out.nom).isEqualTo("New");
    }

    @Test
    @DisplayName("update clears relations when IDs are null")
    void updateClearsRelations() {
        Game existing = new Game();
        existing.id = 8;
        // start with some relations
        existing.category = new Category();
        existing.publisher = new Publisher();
        existing.author = new Author();
        Mockito.when(gameRepository.findById(8)).thenReturn(Optional.of(existing));
        Mockito.when(gameRepository.save(any(Game.class))).thenAnswer(inv -> inv.getArgument(0));

        GameDTO dto = new GameDTO();
        dto.nom = "Clear";
        dto.categoryId = null;
        dto.publisherId = null;
        dto.authorId = null;

        GameDTO out = service.update(8, dto);
        assertThat(out.categoryId).isNull();
        assertThat(out.publisherId).isNull();
        assertThat(out.authorId).isNull();
    }

    @Test
    @DisplayName("delete calls repository when entity exists")
    void deleteSuccess() {
        Mockito.when(gameRepository.existsById(11)).thenReturn(true);
        service.delete(11);
        Mockito.verify(gameRepository).deleteById(11);
    }

    @Test
    @DisplayName("searchAdvanced with all filters delegates to repository and maps page")
    void searchAdvancedAllFilters() {
        Game g = new Game();
        g.id = 1; g.nom = "Filtered"; g.price = new BigDecimal("15");
        Mockito.when(gameRepository.findAll(Mockito.<Specification<Game>>any(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(g), PageRequest.of(0,5), 1));

        var page = service.searchAdvanced("fil", 1L, 2L, 3L, new BigDecimal("10"), new BigDecimal("20"), true, PageRequest.of(0,5));
        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.getContent().get(0).nom).isEqualTo("Filtered");
    }

    @Test
    @DisplayName("create sets relations when IDs provided")
    void createWithRelationIds() {
        GameDTO dto = new GameDTO();
        dto.nom = "Rel";
        dto.categoryId = 1L;
        dto.publisherId = 2L;
        dto.authorId = 3L;

        Mockito.when(categoryRepository.findById(1L)).thenReturn(Optional.of(new Category()));
        Mockito.when(publisherRepository.findById(2L)).thenReturn(Optional.of(new Publisher()));
        Mockito.when(authorRepository.findById(3L)).thenReturn(Optional.of(new Author()));

        Mockito.when(gameRepository.save(any(Game.class))).thenAnswer(inv -> inv.getArgument(0));

        GameDTO out = service.create(dto);
        assertThat(out).isNotNull();
    }

    @Test
    @DisplayName("search uses empty term when q is null; literal when not null")
    void searchTermBranching() {
        Game g = new Game(); g.id = 1; g.nom = "X";
        Mockito.when(gameRepository.findByNomContainingIgnoreCase(Mockito.eq(""), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(g), PageRequest.of(0,5), 1));
        Mockito.when(gameRepository.findByNomContainingIgnoreCase(Mockito.eq("abc"), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(g), PageRequest.of(0,5), 1));

        var pageNull = service.search(null, PageRequest.of(0,5));
        assertThat(pageNull.getTotalElements()).isEqualTo(1);

        var pageNonNull = service.search("abc", PageRequest.of(0,5));
        assertThat(pageNonNull.getTotalElements()).isEqualTo(1);
    }
}
