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
import com.gamesUP.gamesUP.repository.spec.GameSpecifications;
import com.gamesUP.gamesUP.web.dto.GameDTO;
import com.gamesUP.gamesUP.web.mapper.GameMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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

    @Override
    @Transactional(readOnly = true)
    public Page<GameDTO> searchAdvanced(String q,
                                        Long categoryId,
                                        Long publisherId,
                                        Long authorId,
                                        java.math.BigDecimal priceMin,
                                        java.math.BigDecimal priceMax,
                                        Boolean inStock,
                                        Pageable pageable) {
        Specification<Game> spec = GameSpecifications.nomContains(q)
            .and(GameSpecifications.categoryIdEquals(categoryId))
            .and(GameSpecifications.publisherIdEquals(publisherId))
            .and(GameSpecifications.authorIdEquals(authorId))
            .and(GameSpecifications.priceGreaterOrEqual(priceMin))
            .and(GameSpecifications.priceLessOrEqual(priceMax))
            .and(GameSpecifications.inStock(inStock));

        return gameRepository.findAll(spec, pageable).map(mapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GameDTO> list(Pageable pageable) {
        return gameRepository.findAll(pageable).map(mapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public GameDTO get(Integer id) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found"));
        return mapper.toDto(game);
    }

    @Override
    public GameDTO update(Integer id, GameDTO dto) {
        Game existing = gameRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found"));

        // Update scalar fields
        existing.nom = dto.nom;
        existing.auteur = dto.auteur;
        existing.genre = dto.genre;
        existing.numEdition = dto.numEdition;

        // Update relations by IDs if provided (null clears relation)
        if (dto.categoryId != null) {
            Category cat = categoryRepository.findById(dto.categoryId).orElse(null);
            existing.category = cat;
        } else {
            existing.category = null;
        }

        if (dto.publisherId != null) {
            Publisher pub = publisherRepository.findById(dto.publisherId).orElse(null);
            existing.publisher = pub;
        } else {
            existing.publisher = null;
        }

        if (dto.authorId != null) {
            Author author = authorRepository.findById(dto.authorId).orElse(null);
            existing.author = author;
        } else {
            existing.author = null;
        }

        Game saved = gameRepository.save(existing);
        return mapper.toDto(saved);
    }

    @Override
    public void delete(Integer id) {
        if (!gameRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found");
        }
        gameRepository.deleteById(id);
    }
}
