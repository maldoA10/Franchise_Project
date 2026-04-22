package com.accenture.franchise.infrastructure.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateNameRequest {

    @NotBlank(message = "Name is required")
    private String name;
}