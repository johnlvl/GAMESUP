package com.gamesUP.gamesUP.service;

import com.gamesUP.gamesUP.web.dto.CategoryDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @Test
    @DisplayName("create and list categories")
    void createAndList() {
        CategoryDTO dto = new CategoryDTO();
        dto.type = "Family";
        CategoryDTO saved = categoryService.create(dto);
        assertThat(saved.id).isNotNull();

        Page<CategoryDTO> page = categoryService.list(PageRequest.of(0, 10));
        assertThat(page.getTotalElements()).isGreaterThanOrEqualTo(1);
        assertThat(page.getContent()).extracting(x -> x.type).contains("Family");
    }
}
