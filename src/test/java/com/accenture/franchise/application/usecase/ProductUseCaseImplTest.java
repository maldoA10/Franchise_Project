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
class ProductUseCaseImplTest {

    @Mock
    private FranchiseRepository franchiseRepository;

    @InjectMocks
    private ProductUseCaseImpl productUseCase;

    private Franchise sampleFranchise;

    @BeforeEach
    void setUp() {
        Product product = Product.builder()
            .id("p1").name("Product 1").stock(50).build();

        Branch branch = Branch.builder()
            .id("b1").name("Branch 1")
            .products(new ArrayList<>(List.of(product)))
            .build();

        sampleFranchise = Franchise.builder()
            .id("f1").name("Franchise 1")
            .branches(new ArrayList<>(List.of(branch)))
            .build();
    }

    @Test
    void addProductToBranch_shouldAddProductAndReturnIt() {
        when(franchiseRepository.findById("f1"))
            .thenReturn(Mono.just(sampleFranchise));
        when(franchiseRepository.save(any()))
            .thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        Product newProduct = Product.builder().name("New Product").stock(20).build();

        StepVerifier.create(productUseCase.addProductToBranch("f1", "b1", newProduct))
            .expectNextMatches(p -> p.getName().equals("New Product")
                && p.getStock() == 20
                && p.getId() != null)
            .verifyComplete();
    }

    @Test
    void deleteProductFromBranch_shouldRemoveProduct() {
        when(franchiseRepository.findById("f1"))
            .thenReturn(Mono.just(sampleFranchise));
        when(franchiseRepository.save(any()))
            .thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(productUseCase.deleteProductFromBranch("f1", "b1", "p1"))
            .verifyComplete();
    }

    @Test
    void deleteProductFromBranch_whenProductNotFound_shouldReturnError() {
        when(franchiseRepository.findById("f1"))
            .thenReturn(Mono.just(sampleFranchise));

        StepVerifier.create(productUseCase.deleteProductFromBranch("f1", "b1", "invalid"))
            .expectErrorMatches(e -> e instanceof RuntimeException
                && e.getMessage().contains("not found"))
            .verify();
    }

    @Test
    void updateProductStock_shouldUpdateAndReturnProduct() {
        when(franchiseRepository.findById("f1"))
            .thenReturn(Mono.just(sampleFranchise));
        when(franchiseRepository.save(any()))
            .thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(productUseCase.updateProductStock("f1", "b1", "p1", 99))
            .expectNextMatches(p -> p.getStock() == 99)
            .verifyComplete();
    }

    @Test
    void updateProductName_shouldUpdateAndReturnProduct() {
        when(franchiseRepository.findById("f1"))
            .thenReturn(Mono.just(sampleFranchise));
        when(franchiseRepository.save(any()))
            .thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(productUseCase.updateProductName("f1", "b1", "p1", "Renamed"))
            .expectNextMatches(p -> p.getName().equals("Renamed"))
            .verifyComplete();
    }

    @Test
    void addProductToBranch_whenFranchiseNotFound_shouldReturnError() {
        when(franchiseRepository.findById("invalid"))
            .thenReturn(Mono.empty());

        StepVerifier.create(productUseCase.addProductToBranch("invalid", "b1", Product.builder().name("P").stock(1).build()))
            .expectErrorMatches(e -> e instanceof RuntimeException
                && e.getMessage().contains("not found"))
            .verify();
    }
}