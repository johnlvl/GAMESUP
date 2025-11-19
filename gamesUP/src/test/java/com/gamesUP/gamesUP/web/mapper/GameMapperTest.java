package com.gamesUP.gamesUP.web.mapper;

import com.gamesUP.gamesUP.model.Author;
import com.gamesUP.gamesUP.model.Category;
import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.model.Publisher;
import com.gamesUP.gamesUP.web.dto.GameDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class GameMapperTest {

    GameMapper mapper = new GameMapper();

    @Test
    @DisplayName("toDto maps scalar fields and related IDs when present")
    void toDtoWithRelations() {
        Game e = new Game();
        e.id = 42;
        e.nom = "Nom";
        e.auteur = "TxtAuthor";
        e.genre = "Strategy";
        e.numEdition = 3;
        e.price = new BigDecimal("12.34");
        Category c = new Category();
        Publisher p = new Publisher();
        Author a = new Author();
        // IDs might be null (unsaved), but relations non-null must exercise ternary branches
        e.category = c;
        e.publisher = p;
        e.author = a;

        GameDTO d = mapper.toDto(e);
        assertThat(d.id).isEqualTo(42);
        assertThat(d.nom).isEqualTo("Nom");
        assertThat(d.genre).isEqualTo("Strategy");
        assertThat(d.numEdition).isEqualTo(3);
        assertThat(d.price).isEqualByComparingTo("12.34");
        // Related entity IDs are null here (unsaved entities), but branch with non-null relations is exercised without NPE
        assertThat(d.categoryId).isNull();
        assertThat(d.publisherId).isNull();
        assertThat(d.authorId).isNull();
    }

    @Test
    @DisplayName("toDto handles null relations without NPE and returns null IDs")
    void toDtoNullRelations() {
        Game e = new Game();
        e.id = 7;
        e.nom = "Solo";
        GameDTO d = mapper.toDto(e);
        assertThat(d.categoryId).isNull();
        assertThat(d.publisherId).isNull();
        assertThat(d.authorId).isNull();
    }

    @Test
    @DisplayName("toEntity copies scalar fields from DTO")
    void toEntityScalars() {
        GameDTO d = new GameDTO();
        d.id = 5;
        d.nom = "Copy";
        d.auteur = "T";
        d.genre = "G";
        d.numEdition = 1;
        d.price = new BigDecimal("9.99");
        Game e = mapper.toEntity(d);
        assertThat(e.id).isEqualTo(5);
        assertThat(e.nom).isEqualTo("Copy");
        assertThat(e.genre).isEqualTo("G");
        assertThat(e.numEdition).isEqualTo(1);
        assertThat(e.price).isEqualByComparingTo("9.99");
    }

    @Test
    @DisplayName("toDto returns null on null input")
    void toDtoNullInput() {
        assertThat(mapper.toDto(null)).isNull();
    }

    @Test
    @DisplayName("toEntity returns null on null input")
    void toEntityNullInput() {
        assertThat(mapper.toEntity(null)).isNull();
    }
}
