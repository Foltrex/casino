package com.scnsoft.casino;

import java.util.UUID;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface BetRepository extends ReactiveMongoRepository<Bet, UUID> {

}
