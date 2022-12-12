package com.scnsoft.casino;

import java.net.URI;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

@Component
public record UserHandler(UserService userService) {
    
    public Mono<ServerResponse> findAll(ServerRequest request) {
        return ServerResponse.ok().body(userService.findAll(), User.class);
    }    

    public Mono<ServerResponse> findById(ServerRequest request) {
        UUID id = UUID.fromString(request.pathVariable("id"));
        return userService.findById(id)
            .flatMap(user -> ServerResponse.ok().body(Mono.just(user), User.class))
            .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> save(ServerRequest request) {
        return request.bodyToMono(User.class)
            .flatMap(user -> userService.save(user))
            .flatMap(persistedUser -> {
                URI location = URI.create("/users/" + persistedUser.id());
                return ServerResponse.created(location).build();
            });
    }
}
