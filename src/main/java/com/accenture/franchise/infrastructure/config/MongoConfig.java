package com.accenture.franchise.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Configuration
@EnableReactiveMongoAuditing
@EnableReactiveMongoRepositories(
        basePackages = "com.accenture.franchise.infrastructure.adapter.out.mongo.repository"
)
public class MongoConfig {
}