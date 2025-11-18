package com.gamesUP.gamesUP.service;

import com.gamesUP.gamesUP.web.dto.CategoryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    CategoryDTO create(CategoryDTO dto);
    Page<CategoryDTO> list(Pageable pageable);
}
