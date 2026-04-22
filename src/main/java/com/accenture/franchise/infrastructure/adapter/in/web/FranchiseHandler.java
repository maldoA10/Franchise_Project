package com.accenture.franchise.infrastructure.adapter.in.web;

import com.accenture.franchise.domain.model.Franchise;
import com.accenture.franchise.domain.port.in.FranchiseUseCase;
import com.accenture.franchise.infrastructure.adapter.in.web.dto.BranchRequest;
import com.accenture.franchise.infrastructure.adapter.in.web.dto.FranchiseRequest;
import com.accenture.franchise.infrastructure.adapter.in.web.dto.UpdateNameRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class FranchiseHandler {

    private final FranchiseUseCase franchiseUseCase;

    public Mono<ServerResponse> addFranchise(ServerRequest request) {
        return request.bodyToMono(FranchiseRequest.class)
            .flatMap(dto -> {
                Franchise franchise = Franchise.builder()
                    .name(dto.getName())
                    .build();
                return franchiseUseCase.addFranchise(franchise);
            })
            .flatMap(saved -> ServerResponse
                .status(HttpStatus.CREATED)
                .bodyValue(saved)
            );
    }

    public Mono<ServerResponse> updateFranchiseName(ServerRequest request) {
        String franchiseId = request.pathVariable("franchiseId");
        return request.bodyToMono(UpdateNameRequest.class)
            .flatMap(dto -> franchiseUseCase
                .updateFranchiseName(franchiseId, dto.getName()))
            .flatMap(updated -> ServerResponse.ok().bodyValue(updated));
    }

    public Mono<ServerResponse> addBranchToFranchise(ServerRequest request) {
        String franchiseId = request.pathVariable("franchiseId");
        return request.bodyToMono(BranchRequest.class)
            .flatMap(dto -> franchiseUseCase
                .addBranchToFranchise(franchiseId, dto.getName()))
            .flatMap(updated -> ServerResponse
                .status(HttpStatus.CREATED)
                .bodyValue(updated));
    }

    public Mono<ServerResponse> getTopProductPerBranch(ServerRequest request) {
        String franchiseId = request.pathVariable("franchiseId");
        return ServerResponse.ok()
            .body(franchiseUseCase.getTopProductPerBranch(franchiseId),
                com.accenture.franchise.domain.model.TopProductByBranch.class);
    }
}