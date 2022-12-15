package com.scnsoft.casino;

import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

@Service
public record BetService(BetRepository betRepository) {
    public Mono<Bet> save(Bet bet) {
        return betRepository.save(bet);
    }
}
