package com.accenture.franchise.infrastructure.adapter.out.mongo;

import com.accenture.franchise.domain.model.Franchise;
import com.accenture.franchise.domain.port.out.FranchiseRepository;
import com.accenture.franchise.infrastructure.adapter.out.mongo.mapper.FranchiseMapper;
import com.accenture.franchise.infrastructure.adapter.out.mongo.repository.FranchiseMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class FranchiseRepositoryAdapter implements FranchiseRepository {

    private final FranchiseMongoRepository mongoRepository;
    private final FranchiseMapper mapper;

    @Override
    public Mono<Franchise> save(Franchise franchise) {
        return mongoRepository
                .save(mapper.toDocument(franchise))
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Franchise> findById(String id) {
        return mongoRepository
                .findById(id)
                .map(mapper::toDomain);
    }
}