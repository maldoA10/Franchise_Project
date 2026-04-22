package com.accenture.franchise.infrastructure.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FranchiseRequest {

    @NotBlank(message = "Franchise name is required")
    private String name;
}