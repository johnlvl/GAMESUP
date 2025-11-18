package com.gamesUP.gamesUP.service;

import com.gamesUP.gamesUP.web.dto.PublisherDTO;
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
class PublisherServiceTest {

    @Autowired
    private PublisherService publisherService;

    @Test
    @DisplayName("create and list publishers")
    void createAndList() {
        PublisherDTO dto = new PublisherDTO();
        dto.name = "Asmodee";
        PublisherDTO saved = publisherService.create(dto);
        assertThat(saved.id).isNotNull();

        Page<PublisherDTO> page = publisherService.list(PageRequest.of(0, 10));
        assertThat(page.getTotalElements()).isGreaterThanOrEqualTo(1);
        assertThat(page.getContent()).extracting(x -> x.name).contains("Asmodee");
    }
}
