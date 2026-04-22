package com.accenture.franchise.domain.port.out;

import com.accenture.franchise.domain.model.Franchise;
import reactor.core.publisher.Mono;

public interface FranchiseRepository {

    Mono<Franchise> save(Franchise franchise);

    Mono<Franchise> findById(String id);
}