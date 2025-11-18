package com.gamesUP.gamesUP.web.mapper;

import com.gamesUP.gamesUP.model.Publisher;
import com.gamesUP.gamesUP.web.dto.PublisherDTO;
import org.springframework.stereotype.Component;

@Component
public class PublisherMapper {
    public PublisherDTO toDto(Publisher e) {
        if (e == null) return null;
        PublisherDTO d = new PublisherDTO();
        d.id = e.getId();
        d.name = e.getName();
        return d;
    }

    public Publisher toEntity(PublisherDTO d) {
        if (d == null) return null;
        Publisher e = new Publisher();
        e.setName(d.name);
        return e;
    }
}
