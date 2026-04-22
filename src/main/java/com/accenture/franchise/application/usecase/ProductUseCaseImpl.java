package com.accenture.franchise.application.usecase;

import com.accenture.franchise.domain.model.Branch;
import com.accenture.franchise.domain.model.Product;
import com.accenture.franchise.domain.port.in.ProductUseCase;
import com.accenture.franchise.domain.port.out.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductUseCaseImpl implements ProductUseCase {

    private final FranchiseRepository franchiseRepository;

    @Override
    public Mono<Product> addProductToBranch(String franchiseId, String branchId, Product product) {
        return franchiseRepository.findById(franchiseId)
            .switchIfEmpty(Mono.error(
                new RuntimeException("Franchise not found: " + franchiseId)))
            .flatMap(franchise -> {
                Branch branch = findBranch(franchise.getBranches()
                    .stream()
                    .filter(b -> b.getId().equals(branchId))
                    .findFirst(), branchId);

                if (branch.getProducts() == null) {
                    branch.setProducts(new ArrayList<>());
                } else {
                    branch.setProducts(new ArrayList<>(branch.getProducts()));
                }

                product.setId(UUID.randomUUID().toString());
                branch.getProducts().add(product);

                return franchiseRepository.save(franchise)
                    .thenReturn(product);
            }
        );
    }

    @Override
    public Mono<Void> deleteProductFromBranch(String franchiseId, String branchId, String productId) {
        return franchiseRepository.findById(franchiseId)
            .switchIfEmpty(Mono.error(
                new RuntimeException("Franchise not found: " + franchiseId)))
            .flatMap(franchise -> {
                Branch branch = findBranch(franchise.getBranches()
                    .stream()
                    .filter(b -> b.getId().equals(branchId))
                    .findFirst(), branchId);

                if(branch.getProducts() != null) {
                    branch.setProducts(new ArrayList<>(branch.getProducts()));
                } else {
                    return Mono.error(new RuntimeException("Product list is null"));
                }

                boolean removed = branch.getProducts()
                    .removeIf(p -> p.getId().equals(productId));

                if (!removed) {
                    return Mono.error(
                            new RuntimeException("Product not found: " + productId));
                }

                return franchiseRepository.save(franchise).then();
            }
        );
    }

    @Override
    public Mono<Product> updateProductStock(String franchiseId, String branchId, String productId, int newStock) {
        return franchiseRepository.findById(franchiseId)
            .switchIfEmpty(Mono.error(
                new RuntimeException("Franchise not found: " + franchiseId)))
            .flatMap(franchise -> {
                Branch branch = findBranch(franchise.getBranches()
                    .stream()
                    .filter(b -> b.getId().equals(branchId))
                    .findFirst(), branchId);

                Product product = findProduct(branch, productId);
                product.setStock(newStock);

                return franchiseRepository.save(franchise)
                    .thenReturn(product);
            }
        );
    }

    @Override
    public Mono<Product> updateProductName(String franchiseId, String branchId, String productId, String newName) {
        return franchiseRepository.findById(franchiseId)
            .switchIfEmpty(Mono.error(
                new RuntimeException("Franchise not found: " + franchiseId)))
            .flatMap(franchise -> {
                Branch branch = findBranch(franchise.getBranches()
                    .stream()
                    .filter(b -> b.getId().equals(branchId))
                    .findFirst(), branchId);

                Product product = findProduct(branch, productId);
                product.setName(newName);

                return franchiseRepository.save(franchise)
                    .thenReturn(product);
            }
        );
    }

    private Branch findBranch(java.util.Optional<Branch> optional, String branchId) {
        return optional.orElseThrow(() -> new RuntimeException("Branch not found: " + branchId));
    }

    private Product findProduct(Branch branch, String productId) {
        return branch.getProducts().stream()
            .filter(p -> p.getId().equals(productId))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Product not found: " + productId));
    }
}