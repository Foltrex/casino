package com.scnsoft.casino;

import java.util.UUID;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveMongoRepository<User, UUID> {
    // @Query("SELECT * FROM user WHERE user.name LIKE :name")
    // Flux<User> findAllByName(String name);
    
    // @Query("SELECT COUNT(*) FROM user WHERE user.name LIKE :name")
    // Mono<Long> countAllByName(String name);
}
