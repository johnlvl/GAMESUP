package com.gamesUP.gamesUP.web.controller;

import com.gamesUP.gamesUP.service.GameService;
import com.gamesUP.gamesUP.web.dto.GameDTO;
import com.gamesUP.gamesUP.web.error.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.boot.test.context.TestConfiguration;
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

@WebMvcTest(controllers = GameController.class)
@Import(GlobalExceptionHandler.class)
class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GameService gameService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        GameService gameService() {
            return Mockito.mock(GameService.class);
        }
    }

    @Test
    void list_returnsPagedGames() throws Exception {
        GameDTO g = new GameDTO();
        g.id = 1; g.nom = "Catan"; g.numEdition = 1;
        Page<GameDTO> page = new PageImpl<>(List.of(g), PageRequest.of(0, 20), 1);
        Mockito.when(gameService.list(any())).thenReturn(page);

        mockMvc.perform(get("/api/games").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].nom", is("Catan")));
    }

    @Test
    void create_valid_returns201() throws Exception {
        GameDTO req = new GameDTO();
        req.nom = "Azul"; req.numEdition = 1;
        GameDTO created = new GameDTO(); created.id = 10; created.nom = req.nom; created.numEdition = 1;
        Mockito.when(gameService.create(any())).thenReturn(created);

        String body = "{\n  \"nom\": \"Azul\", \"numEdition\": 1\n}";
        mockMvc.perform(post("/api/games").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/api/games/10")))
                .andExpect(jsonPath("$.id", is(10)))
                .andExpect(jsonPath("$.nom", is("Azul")));
    }

    @Test
    void get_notFound_returns404() throws Exception {
        Mockito.when(gameService.get(anyInt())).thenThrow(new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/api/games/999").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    @Test
    void search_withFilters_returnsPagedGames() throws Exception {
        GameDTO g = new GameDTO();
        g.id = 2; g.nom = "Terraforming Mars"; g.numEdition = 1;
        Page<GameDTO> page = new PageImpl<>(List.of(g), PageRequest.of(0, 10), 1);
        Mockito.when(gameService.searchAdvanced(any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(page);

        mockMvc.perform(get("/api/games/search")
                        .param("q", "terra")
                        .param("priceMin", "20")
                        .param("priceMax", "80")
                        .param("inStock", "true")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].nom", containsString("Terraforming")));
    }
}
