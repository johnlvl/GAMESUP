package com.gamesUP.gamesUP.service;

import com.gamesUP.gamesUP.web.dto.GameDTO;
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
class GameServiceTest {

    @Autowired
    private GameService gameService;

    @Test
    @DisplayName("create and search via service")
    void createAndSearch() {
        GameDTO dto = new GameDTO();
        dto.nom = "Terraforming Mars";
        dto.genre = "Strategy";
        dto.numEdition = 1;
        GameDTO saved = gameService.create(dto);
        assertThat(saved.id).isNotNull();

        Page<GameDTO> page = gameService.search("terra", PageRequest.of(0, 10));
        assertThat(page.getTotalElements()).isGreaterThanOrEqualTo(1);
        assertThat(page.getContent()).extracting(x -> x.nom).contains("Terraforming Mars");
    }
}
