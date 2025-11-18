package com.gamesUP.gamesUP.service.impl;

import com.gamesUP.gamesUP.model.Category;
import com.gamesUP.gamesUP.repository.CategoryRepository;
import com.gamesUP.gamesUP.service.CategoryService;
import com.gamesUP.gamesUP.web.dto.CategoryDTO;
import com.gamesUP.gamesUP.web.mapper.CategoryMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper mapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper mapper) {
        this.categoryRepository = categoryRepository;
        this.mapper = mapper;
    }

    @Override
    public CategoryDTO create(CategoryDTO dto) {
        Category entity = mapper.toEntity(dto);
        Category saved = categoryRepository.save(entity);
        return mapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryDTO> list(Pageable pageable) {
        return categoryRepository.findAll(pageable).map(mapper::toDto);
    }
}
