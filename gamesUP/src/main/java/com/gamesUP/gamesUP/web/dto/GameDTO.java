package com.gamesUP.gamesUP.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public class GameDTO {
    public Integer id;

    @NotBlank
    @Size(max = 255)
    public String nom;

    @Size(max = 255)
    public String auteur;

    @Size(max = 255)
    public String genre;

    public Long categoryId;
    public Long publisherId;
    public Long authorId;

    public int numEdition;
    public BigDecimal price;
}
