package com.gamesUP.gamesUP.web.mapper;

import com.gamesUP.gamesUP.model.Author;
import com.gamesUP.gamesUP.web.dto.AuthorDTO;
import org.springframework.stereotype.Component;

@Component
public class AuthorMapper {
    public AuthorDTO toDto(Author e) {
        if (e == null) return null;
        AuthorDTO d = new AuthorDTO();
        d.id = e.id;
        d.name = e.name;
        return d;
    }

    public Author toEntity(AuthorDTO d) {
        if (d == null) return null;
        Author e = new Author();
        e.name = d.name;
        return e;
    }
}
