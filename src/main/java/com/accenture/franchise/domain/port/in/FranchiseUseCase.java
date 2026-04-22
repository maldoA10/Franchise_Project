package com.accenture.franchise.domain.port.in;

import com.accenture.franchise.domain.model.Franchise;
import com.accenture.franchise.domain.model.TopProductByBranch;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FranchiseUseCase {

    Mono<Franchise> addFranchise(Franchise franchise);

    Mono<Franchise> updateFranchiseName(String franchiseId, String newName);

    Mono<Franchise> addBranchToFranchise(String franchiseId, String branchName);

    Flux<TopProductByBranch> getTopProductPerBranch(String franchiseId);
}