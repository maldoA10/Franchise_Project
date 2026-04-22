package com.accenture.franchise.application.usecase;

import com.accenture.franchise.domain.model.Branch;
import com.accenture.franchise.domain.model.Franchise;
import com.accenture.franchise.domain.model.Product;
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
class FranchiseUseCaseImplTest {

    @Mock
    private FranchiseRepository franchiseRepository;

    @InjectMocks
    private FranchiseUseCaseImpl franchiseUseCase;

    private Franchise sampleFranchise;

    @BeforeEach
    void setUp() {
        Product product1 = Product.builder()
            .id("p1").name("Product 1").stock(50).build();
        Product product2 = Product.builder()
            .id("p2").name("Product 2").stock(100).build();

        Branch branch = Branch.builder()
            .id("b1").name("Branch 1")
            .products(new ArrayList<>(List.of(product1, product2)))
            .build();

        sampleFranchise = Franchise.builder()
            .id("f1").name("Franchise 1")
            .branches(new ArrayList<>(List.of(branch)))
            .build();
    }

    @Test
    void addFranchise_shouldSaveAndReturnFranchise() {
        Franchise input = Franchise.builder().name("New Franchise").build();
        when(franchiseRepository.save(any())).thenReturn(Mono.just(input));

        StepVerifier.create(franchiseUseCase.addFranchise(input))
            .expectNextMatches(f -> f.getName().equals("New Franchise"))
            .verifyComplete();
    }

    @Test
    void updateFranchiseName_shouldUpdateAndReturnFranchise() {
        when(franchiseRepository.findById("f1"))
            .thenReturn(Mono.just(sampleFranchise));
        when(franchiseRepository.save(any()))
            .thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(franchiseUseCase.updateFranchiseName("f1", "Updated Name"))
            .expectNextMatches(f -> f.getName().equals("Updated Name"))
            .verifyComplete();
    }

    @Test
    void updateFranchiseName_whenNotFound_shouldReturnError() {
        when(franchiseRepository.findById("invalid"))
            .thenReturn(Mono.empty());

        StepVerifier.create(franchiseUseCase.updateFranchiseName("invalid", "Name"))
            .expectErrorMatches(e -> e instanceof RuntimeException
                && e.getMessage().contains("not found"))
            .verify();
    }

    @Test
    void addBranchToFranchise_shouldAddBranchAndReturnFranchise() {
        when(franchiseRepository.findById("f1"))
            .thenReturn(Mono.just(sampleFranchise));
        when(franchiseRepository.save(any()))
            .thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(franchiseUseCase.addBranchToFranchise("f1", "New Branch"))
            .expectNextMatches(f -> f.getBranches().stream()
                .anyMatch(b -> b.getName().equals("New Branch")))
            .verifyComplete();
    }

    @Test
    void getTopProductPerBranch_shouldReturnProductWithHighestStock() {
        when(franchiseRepository.findById("f1"))
            .thenReturn(Mono.just(sampleFranchise));

        StepVerifier.create(franchiseUseCase.getTopProductPerBranch("f1"))
            .expectNextMatches(top ->
                top.getBranchId().equals("b1") &&
                top.getProduct().getStock() == 100)
            .verifyComplete();
    }

    @Test
    void getTopProductPerBranch_whenFranchiseNotFound_shouldReturnError() {
        when(franchiseRepository.findById("invalid"))
            .thenReturn(Mono.empty());

        StepVerifier.create(franchiseUseCase.getTopProductPerBranch("invalid"))
            .expectErrorMatches(e -> e instanceof RuntimeException
                && e.getMessage().contains("not found"))
            .verify();
    }
}