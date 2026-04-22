package com.accenture.franchise.infrastructure.adapter.in.web.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class UpdateStockRequest {

    @Min(value = 0, message = "Stock must be zero or greater")
    private int stock;
}