package com.accenture.franchise.infrastructure.adapter.in.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class FranchiseRouter {

    private static final String FRANCHISE_BASE = "/api/franchises";
    private static final String FRANCHISE_ID = FRANCHISE_BASE + "/{franchiseId}";
    private static final String BRANCH_BASE = FRANCHISE_ID + "/branches";
    private static final String BRANCH_ID = BRANCH_BASE + "/{branchId}";
    private static final String PRODUCT_BASE = BRANCH_ID + "/products";
    private static final String PRODUCT_ID = PRODUCT_BASE + "/{productId}";

    @Bean
    public RouterFunction<ServerResponse> routes(FranchiseHandler franchiseHandler, BranchHandler branchHandler, ProductHandler productHandler) {
        return RouterFunctions
            .route(POST(FRANCHISE_BASE),
                franchiseHandler::addFranchise)
            .andRoute(PATCH(FRANCHISE_ID + "/name"),
                franchiseHandler::updateFranchiseName)

            .andRoute(POST(BRANCH_BASE),
                franchiseHandler::addBranchToFranchise)
            .andRoute(PATCH(BRANCH_ID + "/name"),
                branchHandler::updateBranchName)

            .andRoute(POST(PRODUCT_BASE),
                productHandler::addProduct)
            .andRoute(DELETE(PRODUCT_ID),
                productHandler::deleteProduct)
            .andRoute(PATCH(PRODUCT_ID + "/stock"),
                productHandler::updateStock)
            .andRoute(PATCH(PRODUCT_ID + "/name"),
                productHandler::updateProductName)

            .andRoute(GET(FRANCHISE_ID + "/top-products"),
                franchiseHandler::getTopProductPerBranch);
    }
}