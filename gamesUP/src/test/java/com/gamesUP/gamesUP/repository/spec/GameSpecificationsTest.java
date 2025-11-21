package com.gamesUP.gamesUP.repository.spec;

import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.repository.GameRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class GameSpecificationsTest {

    @Autowired
    GameRepository gameRepository;

    @Test
    @DisplayName("nomContains filters by partial name, case insensitive")
    void nomContainsFilters() {
        Game g1 = new Game(); g1.nom = "Terraforming Mars"; gameRepository.save(g1);
        Game g2 = new Game(); g2.nom = "Catan"; gameRepository.save(g2);
        Specification<Game> spec = GameSpecifications.nomContains("mars");
        var results = gameRepository.findAll(spec, PageRequest.of(0,10));
        assertThat(results.getTotalElements()).isEqualTo(1);
        assertThat(results.getContent().get(0).nom).containsIgnoringCase("mars");
    }

    @Test
    @DisplayName("priceGreaterOrEqual and priceLessOrEqual combine range")
    void priceRange() {
        Game cheap = new Game(); cheap.nom = "Cheap"; cheap.price = new BigDecimal("10"); gameRepository.save(cheap);
        Game mid = new Game(); mid.nom = "Mid"; mid.price = new BigDecimal("25"); gameRepository.save(mid);
        Game high = new Game(); high.nom = "High"; high.price = new BigDecimal("50"); gameRepository.save(high);
        Specification<Game> spec = GameSpecifications.priceGreaterOrEqual(new BigDecimal("20"))
                .and(GameSpecifications.priceLessOrEqual(new BigDecimal("30")));
        var results = gameRepository.findAll(spec, PageRequest.of(0,10));
        assertThat(results.getTotalElements()).isEqualTo(1);
        assertThat(results.getContent().get(0).nom).isEqualTo("Mid");
    }

    @Test
    @DisplayName("Null parameters yield conjunction (no filtering)")
    void nullParams() {
        Game g1 = new Game(); g1.nom = "A"; gameRepository.save(g1);
        Specification<Game> spec = GameSpecifications.nomContains(null)
                .and(GameSpecifications.categoryIdEquals(null))
                .and(GameSpecifications.publisherIdEquals(null))
                .and(GameSpecifications.authorIdEquals(null))
                .and(GameSpecifications.priceGreaterOrEqual(null))
                .and(GameSpecifications.priceLessOrEqual(null))
                .and(GameSpecifications.inStock(null));
        var results = gameRepository.findAll(spec, PageRequest.of(0,10));
        assertThat(results.getTotalElements()).isEqualTo(1);
    }

    @Test
    @DisplayName("inStock placeholder returns conjunction")
    void inStockPlaceholder() {
        Game g1 = new Game(); g1.nom = "StockTest"; gameRepository.save(g1);
        var specTrue = GameSpecifications.inStock(true);
        var specFalse = GameSpecifications.inStock(false);
        assertThat(gameRepository.findAll(specTrue, PageRequest.of(0,10)).getTotalElements()).isEqualTo(1);
        assertThat(gameRepository.findAll(specFalse, PageRequest.of(0,10)).getTotalElements()).isEqualTo(1);
    }
}
