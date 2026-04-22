package com.accenture.franchise.infrastructure.adapter.out.mongo.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BranchDocument {
    private String id;
    private String name;

    @Builder.Default
    private List<ProductDocument> products = new ArrayList<>();
}