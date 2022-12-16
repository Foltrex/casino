package com.scnsoft.casino;

import java.util.UUID;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import reactor.core.publisher.Flux;

public interface BetRepository extends ReactiveMongoRepository<Bet, UUID> {

    Flux<Bet> findAllByUserId(UUID userId);
}
