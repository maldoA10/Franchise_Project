package com.accenture.franchise.infrastructure.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BranchRequest {

    @NotBlank(message = "Branch name is required")
    private String name;
}