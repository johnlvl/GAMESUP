package com.gamesUP.gamesUP.service.impl;

import com.gamesUP.gamesUP.model.Publisher;
import com.gamesUP.gamesUP.repository.PublisherRepository;
import com.gamesUP.gamesUP.service.PublisherService;
import com.gamesUP.gamesUP.web.dto.PublisherDTO;
import com.gamesUP.gamesUP.web.mapper.PublisherMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PublisherServiceImpl implements PublisherService {

    private final PublisherRepository repository;
    private final PublisherMapper mapper;

    public PublisherServiceImpl(PublisherRepository repository, PublisherMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public PublisherDTO create(PublisherDTO dto) {
        Publisher entity = mapper.toEntity(dto);
        Publisher saved = repository.save(entity);
        return mapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PublisherDTO> list(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDto);
    }
}
