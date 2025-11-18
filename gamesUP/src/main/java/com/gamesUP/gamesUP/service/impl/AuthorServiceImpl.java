package com.gamesUP.gamesUP.service.impl;

import com.gamesUP.gamesUP.model.Author;
import com.gamesUP.gamesUP.repository.AuthorRepository;
import com.gamesUP.gamesUP.service.AuthorService;
import com.gamesUP.gamesUP.web.dto.AuthorDTO;
import com.gamesUP.gamesUP.web.mapper.AuthorMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository repository;
    private final AuthorMapper mapper;

    public AuthorServiceImpl(AuthorRepository repository, AuthorMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public AuthorDTO create(AuthorDTO dto) {
        Author entity = mapper.toEntity(dto);
        Author saved = repository.save(entity);
        return mapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuthorDTO> list(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthorDTO get(Long id) {
        Author a = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found"));
        return mapper.toDto(a);
    }

    @Override
    public AuthorDTO update(Long id, AuthorDTO dto) {
        Author a = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found"));
        a.name = dto.name;
        return mapper.toDto(repository.save(a));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found");
        }
        repository.deleteById(id);
    }
}
