package com.gamesUP.gamesUP.service;

import com.gamesUP.gamesUP.web.dto.PublisherDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PublisherService {
    PublisherDTO create(PublisherDTO dto);
    Page<PublisherDTO> list(Pageable pageable);
}
