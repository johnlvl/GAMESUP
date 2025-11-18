package com.gamesUP.gamesUP.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthorDTO {
    public Long id;

    @NotBlank
    @Size(max = 255)
    public String name;
}
