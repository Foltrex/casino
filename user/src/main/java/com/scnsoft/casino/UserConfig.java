package com.scnsoft.casino;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.mongodb.internal.connection.Server;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;

@Configuration
@EnableReactiveMongoRepositories
public class UserConfig extends AbstractReactiveMongoConfiguration {
    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create();
    }

    @Override
    protected String getDatabaseName() {
        // TODO Auto-generated method stub
        return "scnsoft";
    }

    @Bean
    public RouterFunction<ServerResponse> routes(UserHandler userHandler) {
        return route(GET("/users"), userHandler::findAll)
            .andRoute(GET("/users/{id}"), userHandler::findById)
            .andRoute(POST("/users"), userHandler::save);
    }

    @Bean
    public RouteLocator gateway(RouteLocatorBuilder builder) {
        return builder
            .routes()
            .route(routeSpec -> routeSpec
                .path("/user-service/users")
                .filters(gatewayFilterSpec -> 
                    gatewayFilterSpec.setPath("/users")
                )
                .uri("http://localhost:8085/")
            )
            .build();
    }
}
