package com.gamesUP.gamesUP.service;

import com.gamesUP.gamesUP.web.dto.GameDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface GameService {
    GameDTO create(GameDTO dto);
    Page<GameDTO> search(String q, Pageable pageable);
    Page<GameDTO> searchAdvanced(String q,
                                 Long categoryId,
                                 Long publisherId,
                                 Long authorId,
                                 BigDecimal priceMin,
                                 BigDecimal priceMax,
                                 Boolean inStock,
                                 Pageable pageable);
    Page<GameDTO> list(Pageable pageable);
    GameDTO get(Integer id);
    GameDTO update(Integer id, GameDTO dto);
    void delete(Integer id);
}
