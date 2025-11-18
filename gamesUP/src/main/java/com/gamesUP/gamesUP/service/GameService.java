package com.gamesUP.gamesUP.service;

import com.gamesUP.gamesUP.web.dto.GameDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GameService {
    GameDTO create(GameDTO dto);
    Page<GameDTO> search(String q, Pageable pageable);
}
