package com.gamesUP.gamesUP.web.security;

import com.gamesUP.gamesUP.config.SecurityConfig;
import com.gamesUP.gamesUP.service.GameService;
import com.gamesUP.gamesUP.web.controller.GameController;
import com.gamesUP.gamesUP.web.dto.GameDTO;
import com.gamesUP.gamesUP.web.error.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = GameController.class)
@Import({SecurityConfig.class, GlobalExceptionHandler.class})
class GameSecurityTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    GameService gameService;

    @TestConfiguration
    static class Config {
        @Bean
        GameService gameService() { return Mockito.mock(GameService.class); }
    }

    @Test
    void post_withoutAuth_returns401() throws Exception {
        String body = "{\n  \"nom\": \"Test\", \"numEdition\": 1\n}";
        mockMvc.perform(post("/api/games").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void post_withAdminAuth_returns201() throws Exception {
        GameDTO created = new GameDTO();
        created.id = 55; created.nom = "Test"; created.numEdition = 1;
        Mockito.when(gameService.create(Mockito.any())).thenReturn(created);
        String body = "{\n  \"nom\": \"Test\", \"numEdition\": 1\n}";
        mockMvc.perform(post("/api/games")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin","admin"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());
    }

    @Test
    void post_withClientAuth_returns403() throws Exception {
        String body = "{\n  \"nom\": \"Test\", \"numEdition\": 1\n}";
        mockMvc.perform(post("/api/games")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("client","client"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isForbidden());
    }
}