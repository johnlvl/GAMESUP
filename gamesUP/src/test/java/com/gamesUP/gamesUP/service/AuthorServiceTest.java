package com.gamesUP.gamesUP.service;

import com.gamesUP.gamesUP.web.dto.AuthorDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class AuthorServiceTest {

    @Autowired
    private AuthorService authorService;

    @Test
    @DisplayName("create and list authors")
    void createAndList() {
        AuthorDTO dto = new AuthorDTO();
        dto.name = "Reiner Knizia";
        AuthorDTO saved = authorService.create(dto);
        assertThat(saved.id).isNotNull();

        Page<AuthorDTO> page = authorService.list(PageRequest.of(0, 10));
        assertThat(page.getTotalElements()).isGreaterThanOrEqualTo(1);
        assertThat(page.getContent()).extracting(x -> x.name).contains("Reiner Knizia");
    }
}
