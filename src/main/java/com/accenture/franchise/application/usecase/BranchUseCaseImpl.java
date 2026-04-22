package com.accenture.franchise.application.usecase;

import com.accenture.franchise.domain.model.Branch;
import com.accenture.franchise.domain.port.in.BranchUseCase;
import com.accenture.franchise.domain.port.out.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BranchUseCaseImpl implements BranchUseCase {

    private final FranchiseRepository franchiseRepository;

    @Override
    public Mono<Branch> updateBranchName(String franchiseId, String branchId, String newName) {
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(
                        new RuntimeException("Franchise not found: " + franchiseId)))
                .flatMap(franchise -> {
                    Branch branch = franchise.getBranches().stream()
                            .filter(b -> b.getId().equals(branchId))
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException(
                                    "Branch not found: " + branchId));
                    branch.setName(newName);
                    return franchiseRepository.save(franchise)
                            .thenReturn(branch);
                });
    }
}