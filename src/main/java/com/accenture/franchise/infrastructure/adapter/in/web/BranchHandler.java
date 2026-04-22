package com.accenture.franchise.infrastructure.adapter.in.web;

import com.accenture.franchise.domain.port.in.BranchUseCase;
import com.accenture.franchise.infrastructure.adapter.in.web.dto.UpdateNameRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class BranchHandler {

    private final BranchUseCase branchUseCase;

    public Mono<ServerResponse> updateBranchName(ServerRequest request) {
        String franchiseId = request.pathVariable("franchiseId");
        String branchId = request.pathVariable("branchId");
        return request.bodyToMono(UpdateNameRequest.class)
            .flatMap(dto -> branchUseCase
                .updateBranchName(franchiseId, branchId, dto.getName()))
            .flatMap(updated -> ServerResponse.ok().bodyValue(updated));
    }
}