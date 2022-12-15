package com.scnsoft.casino;

import java.math.BigDecimal;
import java.net.URI;
import java.util.UUID;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

@Component
public record BetHandler(
    @Qualifier("customRabbitTemplate") RabbitTemplate template,
    Queue queue
) {
    @Value("${app.winrate:0.5}")
    private static double WINRATE;

    public Mono<ServerResponse> makeBet(ServerRequest request) {
        return request.bodyToMono(BetDto.class)
            .flatMap(betDto -> calculateWinnings(betDto))
            .flatMap(newBet -> {
                URI location = URI.create("/bets/" + newBet.currentBet());
                return ServerResponse.created(location).build();
            });
    }

    private Mono<BetDto> calculateWinnings(BetDto betDto) {
        BetDto bet = null;
        UUID newBetId = UUID.randomUUID();
        if (Math.random() > WINRATE) {
            BigDecimal moneyAmount = betDto.money();
            BigDecimal moneyAmountAfterWinning = moneyAmount.multiply(BigDecimal.valueOf(2));
            bet = BetDto.builder()
                    .currentBet(newBetId)
                    .previousBet(betDto.currentBet())
                    .money(moneyAmountAfterWinning)
                    .user(betDto.user())
                    .build();
        } else {
            bet = BetDto.builder()
                .currentBet(newBetId)
                .previousBet(betDto.currentBet())
                .money(BigDecimal.ZERO)
                .user(betDto.user())
                .build();
        }
        
        template.convertAndSend(queue.getName(), bet);
        return Mono.just(bet);
    }
}
