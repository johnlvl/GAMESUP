package com.gamesUP.gamesUP.service;

import com.gamesUP.gamesUP.web.dto.PublisherDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PublisherService {
    PublisherDTO create(PublisherDTO dto);
    Page<PublisherDTO> list(Pageable pageable);
    PublisherDTO get(Long id);
    PublisherDTO update(Long id, PublisherDTO dto);
    void delete(Long id);
}
