package com.gamesUP.gamesUP.web.mapper;

import com.gamesUP.gamesUP.model.Category;
import com.gamesUP.gamesUP.web.dto.CategoryDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CategoryMapperTest {

    CategoryMapper mapper = new CategoryMapper();

    @Test
    @DisplayName("toDto maps fields; null input returns null")
    void toDtoMappingAndNull() {
        Category e = new Category();
        e.setType("Action");
        CategoryDTO d = mapper.toDto(e);
        assertThat(d).isNotNull();
        assertThat(d.type).isEqualTo("Action");

        assertThat(mapper.toDto(null)).isNull();
    }

    @Test
    @DisplayName("toEntity maps fields; null input returns null")
    void toEntityMappingAndNull() {
        CategoryDTO d = new CategoryDTO();
        d.type = "Adventure";
        Category e = mapper.toEntity(d);
        assertThat(e).isNotNull();
        assertThat(e.getType()).isEqualTo("Adventure");

        assertThat(mapper.toEntity(null)).isNull();
    }
}
