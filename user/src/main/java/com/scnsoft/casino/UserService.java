package com.scnsoft.casino;

import java.util.UUID;

import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public record UserService(UserRepository userRepository) {
    
    public Mono<User> findById(UUID id) {
        return userRepository.findById(id);
    }

    public Flux<User> findAll() {
        return userRepository.findAll();
    }

    public Mono<User> save(User user) {
        return userRepository.save(user);
    }
    // public Mono<User> create()
}
