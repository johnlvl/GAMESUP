package com.gamesUP.gamesUP.web.mapper;

import com.gamesUP.gamesUP.model.Publisher;
import com.gamesUP.gamesUP.web.dto.PublisherDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PublisherMapperTest {

    PublisherMapper mapper = new PublisherMapper();

    @Test
    @DisplayName("toDto maps fields; null input returns null")
    void toDtoMappingAndNull() {
        Publisher e = new Publisher();
        e.setName("Ubisoft");
        PublisherDTO d = mapper.toDto(e);
        assertThat(d).isNotNull();
        assertThat(d.name).isEqualTo("Ubisoft");

        assertThat(mapper.toDto(null)).isNull();
    }

    @Test
    @DisplayName("toEntity maps fields; null input returns null")
    void toEntityMappingAndNull() {
        PublisherDTO d = new PublisherDTO();
        d.name = "EA";
        Publisher e = mapper.toEntity(d);
        assertThat(e).isNotNull();
        assertThat(e.getName()).isEqualTo("EA");

        assertThat(mapper.toEntity(null)).isNull();
    }
}
