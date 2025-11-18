package com.gamesUP.gamesUP.repository;

import com.gamesUP.gamesUP.model.Game;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class GameRepositoryTest {

    @Autowired
    private GameRepository gameRepository;

    @Test
    @DisplayName("save and search with pagination")
    void saveAndSearch() {
        Game g1 = new Game();
        g1.nom = "Catan";
        gameRepository.save(g1);

        Game g2 = new Game();
        g2.nom = "Catalyst";
        gameRepository.save(g2);

        Page<Game> page = gameRepository.findByNomContainingIgnoreCase("cat", PageRequest.of(0, 10));
        assertThat(page.getTotalElements()).isEqualTo(2);
        assertThat(page.getContent()).extracting(x -> x.nom).contains("Catan", "Catalyst");
    }
}
