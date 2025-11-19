package com.gamesUP.gamesUP.web.mapper;

import com.gamesUP.gamesUP.model.Author;
import com.gamesUP.gamesUP.web.dto.AuthorDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AuthorMapperTest {

    AuthorMapper mapper = new AuthorMapper();

    @Test
    @DisplayName("toDto maps fields; null input returns null")
    void toDtoMappingAndNull() {
        Author e = new Author();
        e.name = "John";
        AuthorDTO d = mapper.toDto(e);
        assertThat(d).isNotNull();
        assertThat(d.name).isEqualTo("John");

        assertThat(mapper.toDto(null)).isNull();
    }

    @Test
    @DisplayName("toEntity maps fields; null input returns null")
    void toEntityMappingAndNull() {
        AuthorDTO d = new AuthorDTO();
        d.name = "Jane";
        Author e = mapper.toEntity(d);
        assertThat(e).isNotNull();
        assertThat(e.name).isEqualTo("Jane");

        assertThat(mapper.toEntity(null)).isNull();
    }
}
