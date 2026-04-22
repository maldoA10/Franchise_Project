package com.accenture.franchise.infrastructure.adapter.in.web;

import com.accenture.franchise.domain.model.Product;
import com.accenture.franchise.domain.port.in.ProductUseCase;
import com.accenture.franchise.infrastructure.adapter.in.web.dto.ProductRequest;
import com.accenture.franchise.infrastructure.adapter.in.web.dto.UpdateNameRequest;
import com.accenture.franchise.infrastructure.adapter.in.web.dto.UpdateStockRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ProductHandler {

    private final ProductUseCase productUseCase;

    public Mono<ServerResponse> addProduct(ServerRequest request) {
        String franchiseId = request.pathVariable("franchiseId");
        String branchId = request.pathVariable("branchId");
        return request.bodyToMono(ProductRequest.class)
            .flatMap(dto -> {
                Product product = Product.builder()
                    .name(dto.getName())
                    .stock(dto.getStock())
                    .build();
                return productUseCase.addProductToBranch(franchiseId, branchId, product);
            })
            .flatMap(saved -> ServerResponse
                .status(HttpStatus.CREATED)
                .bodyValue(saved));
    }

    public Mono<ServerResponse> deleteProduct(ServerRequest request) {
        String franchiseId = request.pathVariable("franchiseId");
        String branchId = request.pathVariable("branchId");
        String productId = request.pathVariable("productId");
        return productUseCase
            .deleteProductFromBranch(franchiseId, branchId, productId)
            .then(ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> updateStock(ServerRequest request) {
        String franchiseId = request.pathVariable("franchiseId");
        String branchId = request.pathVariable("branchId");
        String productId = request.pathVariable("productId");
        return request.bodyToMono(UpdateStockRequest.class)
            .flatMap(dto -> productUseCase
                .updateProductStock(franchiseId, branchId, productId, dto.getStock()))
            .flatMap(updated -> ServerResponse.ok().bodyValue(updated));
    }

    public Mono<ServerResponse> updateProductName(ServerRequest request) {
        String franchiseId = request.pathVariable("franchiseId");
        String branchId = request.pathVariable("branchId");
        String productId = request.pathVariable("productId");
        return request.bodyToMono(UpdateNameRequest.class)
            .flatMap(dto -> productUseCase
                .updateProductName(franchiseId, branchId, productId, dto.getName()))
            .flatMap(updated -> ServerResponse.ok().bodyValue(updated));
    }
}