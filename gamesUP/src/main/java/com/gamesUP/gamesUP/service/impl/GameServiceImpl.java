package com.gamesUP.gamesUP.service.impl;

import com.gamesUP.gamesUP.model.Author;
import com.gamesUP.gamesUP.model.Category;
import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.model.Publisher;
import com.gamesUP.gamesUP.repository.AuthorRepository;
import com.gamesUP.gamesUP.repository.CategoryRepository;
import com.gamesUP.gamesUP.repository.GameRepository;
import com.gamesUP.gamesUP.repository.PublisherRepository;
import com.gamesUP.gamesUP.service.GameService;
import com.gamesUP.gamesUP.web.dto.GameDTO;
import com.gamesUP.gamesUP.web.mapper.GameMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final CategoryRepository categoryRepository;
    private final PublisherRepository publisherRepository;
    private final AuthorRepository authorRepository;
    private final GameMapper mapper;

    public GameServiceImpl(GameRepository gameRepository,
                           CategoryRepository categoryRepository,
                           PublisherRepository publisherRepository,
                           AuthorRepository authorRepository,
                           GameMapper mapper) {
        this.gameRepository = gameRepository;
        this.categoryRepository = categoryRepository;
        this.publisherRepository = publisherRepository;
        this.authorRepository = authorRepository;
        this.mapper = mapper;
    }

    @Override
    public GameDTO create(GameDTO dto) {
        Game entity = mapper.toEntity(dto);
        if (dto.categoryId != null) {
            Category cat = categoryRepository.findById(dto.categoryId).orElse(null);
            entity.category = cat;
        }
        if (dto.publisherId != null) {
            Publisher pub = publisherRepository.findById(dto.publisherId).orElse(null);
            entity.publisher = pub;
        }
        if (dto.authorId != null) {
            Author author = authorRepository.findById(dto.authorId).orElse(null);
            entity.author = author;
        }
        Game saved = gameRepository.save(entity);
        return mapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GameDTO> search(String q, Pageable pageable) {
        String term = q == null ? "" : q;
        return gameRepository.findByNomContainingIgnoreCase(term, pageable)
                .map(mapper::toDto);
    }
}
