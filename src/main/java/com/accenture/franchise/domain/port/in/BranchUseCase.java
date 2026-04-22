package com.accenture.franchise.domain.port.in;

import com.accenture.franchise.domain.model.Branch;
import reactor.core.publisher.Mono;

public interface BranchUseCase {

    Mono<Branch> updateBranchName(String franchiseId, String branchId, String newName);
}