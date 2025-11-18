package com.gamesUP.gamesUP.service;

import com.gamesUP.gamesUP.web.dto.AuthorDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuthorService {
    AuthorDTO create(AuthorDTO dto);
    Page<AuthorDTO> list(Pageable pageable);
}
