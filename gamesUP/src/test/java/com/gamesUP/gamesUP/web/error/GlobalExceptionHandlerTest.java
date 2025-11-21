package com.gamesUP.gamesUP.web.error;

import com.gamesUP.gamesUP.web.controller.GameController; // assuming exists
import com.gamesUP.gamesUP.service.GameService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.mockito.Mockito.when;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = GameController.class)
@Import(GlobalExceptionHandler.class)
@WithMockUser // provide authenticated principal to bypass 401
class GlobalExceptionHandlerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    GameService gameService; // Mock to satisfy GameController dependency

    @Test
    @DisplayName("Validation error returns structured 400 with fields map")
    void validationError() throws Exception {
        // Missing required fields should trigger validation; ensure GameDTO has constraints (nom maybe @NotBlank in controller binding)
        mvc.perform(post("/api/games")
                .with(csrf()) // add CSRF token to avoid 403
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ }"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.fields").exists());
    }

    @Test
    @DisplayName("Not found in controller propagates to default handler (404) and not caught by validation advice")
    void notFound() throws Exception {
        when(gameService.get(999999)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found"));
        mvc.perform(get("/api/games/999999"))
                .andExpect(status().isNotFound());
    }
}
