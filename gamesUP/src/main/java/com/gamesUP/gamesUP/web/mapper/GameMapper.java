package com.gamesUP.gamesUP.web.mapper;

import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.web.dto.GameDTO;
import org.springframework.stereotype.Component;

@Component
public class GameMapper {
    public GameDTO toDto(Game e) {
        if (e == null) return null;
        GameDTO d = new GameDTO();
        d.id = e.id;
        d.nom = e.nom;
        d.auteur = e.auteur;
        d.genre = e.genre;
        d.numEdition = e.numEdition;
        d.categoryId = e.category != null ? e.category.getId() : null;
        d.publisherId = e.publisher != null ? e.publisher.getId() : null;
        d.authorId = e.author != null ? e.author.id : null;
        return d;
    }

    public Game toEntity(GameDTO d) {
        if (d == null) return null;
        Game e = new Game();
        e.id = d.id;
        e.nom = d.nom;
        e.auteur = d.auteur;
        e.genre = d.genre;
        e.numEdition = d.numEdition;
        // Relations (category/publisher/author) are set in service using IDs
        return e;
    }
}
