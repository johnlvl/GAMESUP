package com.gamesUP.gamesUP.repository;

import com.gamesUP.gamesUP.model.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface GameRepository extends JpaRepository<Game, Integer>, JpaSpecificationExecutor<Game> {
    Page<Game> findByNomContainingIgnoreCase(String nom, Pageable pageable);
}
