package com.scnsoft.casino;

import java.util.UUID;

import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public record BetService(BetRepository betRepository) {
    public Mono<Bet> save(Bet bet) {
        return betRepository.save(bet);
    }

    public Flux<Bet> findAllByUserId(UUID userId) {
        return betRepository.findAllByUserId(userId);
    }
}
