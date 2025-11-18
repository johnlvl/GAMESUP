package com.gamesUP.gamesUP.web.dto;

import java.util.List;

public class GameSearchResponseDTO {
    public List<GameDTO> items;
    public long total;
    public int page;
    public int size;

    public GameSearchResponseDTO(List<GameDTO> items, long total, int page, int size) {
        this.items = items;
        this.total = total;
        this.page = page;
        this.size = size;
    }
}
