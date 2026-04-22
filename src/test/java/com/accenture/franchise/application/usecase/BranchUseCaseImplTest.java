package com.accenture.franchise.application.usecase;

import com.accenture.franchise.domain.model.Branch;
import com.accenture.franchise.domain.model.Franchise;
import com.accenture.franchise.domain.port.out.FranchiseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BranchUseCaseImplTest {

    @Mock
    private FranchiseRepository franchiseRepository;

    @InjectMocks
    private BranchUseCaseImpl branchUseCase;

    private Franchise sampleFranchise;

    @BeforeEach
    void setUp() {
        Branch branch = Branch.builder()
            .id("b1").name("Branch 1")
            .products(new ArrayList<>())
            .build();

        sampleFranchise = Franchise.builder()
            .id("f1").name("Franchise 1")
            .branches(new ArrayList<>(List.of(branch)))
            .build();
    }

    @Test
    void updateBranchName_shouldUpdateAndReturnBranch() {
        when(franchiseRepository.findById("f1"))
            .thenReturn(Mono.just(sampleFranchise));
        when(franchiseRepository.save(any()))
            .thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(branchUseCase.updateBranchName("f1", "b1", "Updated Branch"))
            .expectNextMatches(b -> b.getName().equals("Updated Branch"))
            .verifyComplete();
    }

    @Test
    void updateBranchName_whenFranchiseNotFound_shouldReturnError() {
        when(franchiseRepository.findById("invalid"))
            .thenReturn(Mono.empty());

        StepVerifier.create(branchUseCase.updateBranchName("invalid", "b1", "Name"))
            .expectErrorMatches(e -> e instanceof RuntimeException
                && e.getMessage().contains("not found"))
            .verify();
    }

    @Test
    void updateBranchName_whenBranchNotFound_shouldReturnError() {
        when(franchiseRepository.findById("f1"))
            .thenReturn(Mono.just(sampleFranchise));

        StepVerifier.create(branchUseCase.updateBranchName("f1", "invalid", "Name"))
            .expectErrorMatches(e -> e instanceof RuntimeException
                && e.getMessage().contains("not found"))
            .verify();
    }
}