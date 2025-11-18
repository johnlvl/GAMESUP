package com.gamesUP.gamesUP.service;

import com.gamesUP.gamesUP.web.dto.AuthorDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuthorService {
    AuthorDTO create(AuthorDTO dto);
    Page<AuthorDTO> list(Pageable pageable);
    AuthorDTO get(Long id);
    AuthorDTO update(Long id, AuthorDTO dto);
    void delete(Long id);
}
