package com.accenture.franchise.infrastructure.adapter.out.mongo.mapper;

import com.accenture.franchise.domain.model.Branch;
import com.accenture.franchise.domain.model.Franchise;
import com.accenture.franchise.domain.model.Product;
import com.accenture.franchise.infrastructure.adapter.out.mongo.document.FranchiseDocument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class FranchiseMapperTest {

    private FranchiseMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new FranchiseMapper();
    }

    @Test
    void toDomain_shouldMapAllFieldsCorrectly() {
        FranchiseDocument doc = buildSampleDocument();

        Franchise domain = mapper.toDomain(doc);

        assertThat(domain.getId()).isEqualTo("f1");
        assertThat(domain.getName()).isEqualTo("Franchise 1");
        assertThat(domain.getBranches()).hasSize(1);
        assertThat(domain.getBranches().get(0).getId()).isEqualTo("b1");
        assertThat(domain.getBranches().get(0).getProducts()).hasSize(1);
        assertThat(domain.getBranches().get(0).getProducts().get(0).getStock()).isEqualTo(10);
    }

    @Test
    void toDocument_shouldMapAllFieldsCorrectly() {
        Franchise franchise = buildSampleDomain();

        FranchiseDocument doc = mapper.toDocument(franchise);

        assertThat(doc.getId()).isEqualTo("f1");
        assertThat(doc.getName()).isEqualTo("Franchise 1");
        assertThat(doc.getBranches()).hasSize(1);
        assertThat(doc.getBranches().get(0).getProducts()).hasSize(1);
    }

    @Test
    void toDocument_whenProductIdIsNull_shouldGenerateId() {
        Product product = Product.builder().name("P").stock(5).build(); // id null
        Branch branch = Branch.builder()
            .id("b1").name("B")
            .products(new ArrayList<>(List.of(product)))
            .build();
        Franchise franchise = Franchise.builder()
            .id("f1").name("F")
            .branches(new ArrayList<>(List.of(branch)))
            .build();

        FranchiseDocument doc = mapper.toDocument(franchise);

        assertThat(doc.getBranches().get(0).getProducts().get(0).getId()).isNotNull();
    }

    @Test
    void toDomain_withNullLists_shouldReturnEmptyLists() {
        FranchiseDocument doc = FranchiseDocument.builder()
            .id("f1").name("F").branches(null).build();

        Franchise domain = mapper.toDomain(doc);

        assertThat(domain.getBranches()).isEmpty();
    }

    private FranchiseDocument buildSampleDocument() {
        var productDoc = com.accenture.franchise.infrastructure.adapter.out.mongo.document
            .ProductDocument.builder().id("p1").name("P1").stock(10).build();
        var branchDoc = com.accenture.franchise.infrastructure.adapter.out.mongo.document
            .BranchDocument.builder().id("b1").name("B1")
            .products(new ArrayList<>(List.of(productDoc))).build();
        return FranchiseDocument.builder().id("f1").name("Franchise 1")
            .branches(new ArrayList<>(List.of(branchDoc))).build();
    }

    private Franchise buildSampleDomain() {
        Product product = Product.builder().id("p1").name("P1").stock(10).build();
        Branch branch = Branch.builder().id("b1").name("B1")
            .products(new ArrayList<>(List.of(product))).build();
        return Franchise.builder().id("f1").name("Franchise 1")
            .branches(new ArrayList<>(List.of(branch))).build();
    }
}