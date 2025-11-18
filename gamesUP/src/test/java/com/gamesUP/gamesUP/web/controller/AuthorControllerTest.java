package com.gamesUP.gamesUP.web.controller;

import com.gamesUP.gamesUP.service.AuthorService;
import com.gamesUP.gamesUP.web.dto.AuthorDTO;
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

@WebMvcTest(controllers = AuthorController.class)
@Import(GlobalExceptionHandler.class)
class AuthorControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthorService service;

    @TestConfiguration
    static class TestConfig {
        @Bean
        AuthorService authorService() {
            return Mockito.mock(AuthorService.class);
        }
    }

    @Test
    void list_ok() throws Exception {
        AuthorDTO a = new AuthorDTO(); a.id = 1L; a.name = "Bruno Cathala";
        Page<AuthorDTO> page = new PageImpl<>(List.of(a), PageRequest.of(0, 20), 1);
        Mockito.when(service.list(any())).thenReturn(page);

        mockMvc.perform(get("/api/authors").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name", is("Bruno Cathala")));
    }

    @Test
    void create_201() throws Exception {
        AuthorDTO created = new AuthorDTO(); created.id = 7L; created.name = "Reiner Knizia";
        Mockito.when(service.create(any())).thenReturn(created);

        String body = "{\n  \"name\": \"Reiner Knizia\"\n}";
        mockMvc.perform(post("/api/authors").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/api/authors/7")))
                .andExpect(jsonPath("$.id", is(7)));
    }
}
