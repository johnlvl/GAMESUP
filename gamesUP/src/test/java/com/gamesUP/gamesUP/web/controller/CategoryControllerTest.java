package com.gamesUP.gamesUP.web.controller;

import com.gamesUP.gamesUP.service.CategoryService;
import com.gamesUP.gamesUP.web.dto.CategoryDTO;
import com.gamesUP.gamesUP.web.error.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CategoryController.class)
@Import(GlobalExceptionHandler.class)
class CategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryService service;

    @TestConfiguration
    static class TestConfig {
        @Bean
        CategoryService categoryService() {
            return Mockito.mock(CategoryService.class);
        }
    }

    @Test
    void list_ok() throws Exception {
        CategoryDTO c = new CategoryDTO(); c.id = 1L; c.type = "Family";
        Page<CategoryDTO> page = new PageImpl<>(List.of(c), PageRequest.of(0, 20), 1);
        Mockito.when(service.list(any())).thenReturn(page);

        mockMvc.perform(get("/api/categories").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].type", is("Family")));
    }

    @Test
    void create_201() throws Exception {
        CategoryDTO created = new CategoryDTO(); created.id = 10L; created.type = "Strategy";
        Mockito.when(service.create(any())).thenReturn(created);

        String body = "{\n  \"type\": \"Strategy\"\n}";
        mockMvc.perform(post("/api/categories").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/api/categories/10")))
                .andExpect(jsonPath("$.id", is(10)));
    }
}
