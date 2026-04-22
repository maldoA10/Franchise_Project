package com.accenture.franchise.application.usecase;

import com.accenture.franchise.domain.model.Branch;
import com.accenture.franchise.domain.model.Franchise;
import com.accenture.franchise.domain.model.TopProductByBranch;
import com.accenture.franchise.domain.port.in.FranchiseUseCase;
import com.accenture.franchise.domain.port.out.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FranchiseUseCaseImpl implements FranchiseUseCase {

    private final FranchiseRepository franchiseRepository;

    @Override
    public Mono<Franchise> addFranchise(Franchise franchise) {
        franchise.setId(UUID.randomUUID().toString());
        franchise.setBranches(new ArrayList<>());
        return franchiseRepository.save(franchise);
    }

    @Override
    public Mono<Franchise> updateFranchiseName(String franchiseId, String newName) {
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(
                        new RuntimeException("Franchise not found: " + franchiseId)))
                .flatMap(franchise -> {
                    franchise.setName(newName);
                    return franchiseRepository.save(franchise);
                });
    }

    @Override
    public Mono<Franchise> addBranchToFranchise(String franchiseId, String branchName) {
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(
                        new RuntimeException("Franchise not found: " + franchiseId)))
                .flatMap(franchise -> {
                    Branch newBranch = Branch.builder()
                            .id(UUID.randomUUID().toString())
                            .name(branchName)
                            .products(new ArrayList<>())
                            .build();
                    franchise.getBranches().add(newBranch);
                    return franchiseRepository.save(franchise);
                });
    }

    @Override
    public Flux<TopProductByBranch> getTopProductPerBranch(String franchiseId) {
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(
                        new RuntimeException("Franchise not found: " + franchiseId)))
                .flatMapMany(franchise -> Flux.fromIterable(franchise.getBranches()))
                .flatMap(branch -> {
                    if (branch.getProducts().isEmpty()) {
                        return Flux.empty();
                    }
                    return branch.getProducts().stream()
                            .max(Comparator.comparingInt(p -> p.getStock()))
                            .map(topProduct -> Flux.just(
                                    TopProductByBranch.builder()
                                            .branchId(branch.getId())
                                            .branchName(branch.getName())
                                            .product(topProduct)
                                            .build()))
                            .orElse(Flux.empty());
                });
    }
}