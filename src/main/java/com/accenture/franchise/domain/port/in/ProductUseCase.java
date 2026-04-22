package com.accenture.franchise.domain.port.in;

import com.accenture.franchise.domain.model.Product;
import reactor.core.publisher.Mono;

public interface ProductUseCase {

    Mono<Product> addProductToBranch(String franchiseId, String branchId, Product product);

    Mono<Void> deleteProductFromBranch(String franchiseId, String branchId, String productId);

    Mono<Product> updateProductStock(String franchiseId, String branchId, String productId, int newStock);

    Mono<Product> updateProductName(String franchiseId, String branchId, String productId, String newName);
}