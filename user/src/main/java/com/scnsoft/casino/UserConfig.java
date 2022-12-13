package com.scnsoft.casino;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;

@Configuration
@EnableReactiveMongoRepositories
public class UserConfig extends AbstractReactiveMongoConfiguration implements WebFluxConfigurer {
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

    // @Override
    // public void addCorsMappings(CorsRegistry registry) {
    //     registry.addMapping("/**")
    //         .allowedOrigins("http://localhost:8080")
    //         .allowedMethods("*");
    // }

    @Bean
    CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(List.of("http://localhost:8080/", "http://localhost:8080"));
        corsConfig.setMaxAge(8000L);
        corsConfig.addAllowedMethod("*");
    
        UrlBasedCorsConfigurationSource source =
          new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
    
        return new CorsWebFilter(source);
    }
}
