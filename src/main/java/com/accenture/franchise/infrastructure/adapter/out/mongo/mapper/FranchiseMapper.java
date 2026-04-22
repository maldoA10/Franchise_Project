package com.accenture.franchise.infrastructure.adapter.out.mongo.mapper;

import com.accenture.franchise.domain.model.Branch;
import com.accenture.franchise.domain.model.Franchise;
import com.accenture.franchise.domain.model.Product;
import com.accenture.franchise.infrastructure.adapter.out.mongo.document.BranchDocument;
import com.accenture.franchise.infrastructure.adapter.out.mongo.document.FranchiseDocument;
import com.accenture.franchise.infrastructure.adapter.out.mongo.document.ProductDocument;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class FranchiseMapper {

    public Franchise toDomain(FranchiseDocument doc) {
        return Franchise.builder()
            .id(doc.getId())
            .name(doc.getName())
            .branches(mapBranchesToDomain(doc.getBranches()))
            .build();
    }

    private List<Branch> mapBranchesToDomain(List<BranchDocument> docs) {
        return Optional.ofNullable(docs).orElse(Collections.emptyList())
            .stream()
            .map(this::toDomain)
            .toList();
    }

    private Branch toDomain(BranchDocument doc) {
        return Branch.builder()
            .id(doc.getId())
            .name(doc.getName())
            .products(mapProductsToDomain(doc.getProducts()))
            .build();
    }

    private List<Product> mapProductsToDomain(List<ProductDocument> docs) {
        return Optional.ofNullable(docs).orElse(Collections.emptyList())
            .stream()
            .map(this::toDomain)
            .toList();
    }

    private Product toDomain(ProductDocument doc) {
        return Product.builder()
            .id(doc.getId())
            .name(doc.getName())
            .stock(doc.getStock())
            .build();
    }

    public FranchiseDocument toDocument(Franchise franchise) {
        return FranchiseDocument.builder()
            .id(franchise.getId())
            .name(franchise.getName())
            .branches(mapBranchesToDocument(franchise.getBranches()))
            .build();
    }

    private List<BranchDocument> mapBranchesToDocument(List<Branch> branches) {
        return Optional.ofNullable(branches).orElse(Collections.emptyList())
            .stream()
            .map(this::toDocument)
            .toList();
    }

    public BranchDocument toDocument(Branch branch) {
        return BranchDocument.builder()
            .id(Optional.ofNullable(branch.getId()).orElse(UUID.randomUUID().toString()))
            .name(branch.getName())
            .products(mapProductsToDocument(branch.getProducts()))
            .build();
    }

    private List<ProductDocument> mapProductsToDocument(List<Product> products) {
        return Optional.ofNullable(products).orElse(Collections.emptyList())
            .stream()
            .map(this::toDocument)
            .toList();
    }

    public ProductDocument toDocument(Product product) {
        return ProductDocument.builder()
            .id(Optional.ofNullable(product.getId()).orElse(UUID.randomUUID().toString()))
            .name(product.getName())
            .stock(product.getStock())
            .build();
    }
}