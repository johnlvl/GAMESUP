package com.gamesUP.gamesUP.web.mapper;

import com.gamesUP.gamesUP.model.Category;
import com.gamesUP.gamesUP.web.dto.CategoryDTO;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
    public CategoryDTO toDto(Category e) {
        if (e == null) return null;
        CategoryDTO d = new CategoryDTO();
        d.id = e.getId();
        d.type = e.getType();
        return d;
    }

    public Category toEntity(CategoryDTO d) {
        if (d == null) return null;
        Category e = new Category();
        e.setType(d.type);
        return e;
    }
}
