package com.scnsoft.casino;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;

@RestController
public record BetController(
    AmqpTemplate amqpTemplate,
    @Value("${javainuse.rabbitmq.exchange}") String exchange,
    @Value("${javainuse.rabbitmq.routingkey}") String routingkey
) {
    private static final String USER_ID = "user";
    private static final String BET_ID = "currentBet";
    private static final String MONEY_AMOUNT = "money";

    @Value("${app.winrate:0.5}")
    private static double WINRATE;


    @PostMapping("/bet")
    public void makeBet(@RequestBody ObjectNode json) {
        String userIdString = json.get(USER_ID).asText();
        UUID userId = UUID.fromString(userIdString);

        String currentBetIdString = json.get(BET_ID).asText();
        UUID currentBetId = UUID.fromString(currentBetIdString);

        String moneyAmountString = json.get(MONEY_AMOUNT).asText();
        BigDecimal moneyAmount = new BigDecimal(moneyAmountString);

        BetDto bet = null;
        if (Math.random() > WINRATE) {
            UUID newBetId = UUID.randomUUID();
            BigDecimal moneyAmountAfterWinning = moneyAmount.multiply(BigDecimal.valueOf(2));
            bet = BetDto.builder()
                    .currentBet(newBetId)
                    .previousBet(currentBetId)
                    .money(moneyAmountAfterWinning)
                    .user(userId)
                    .build();
        } else {
            bet = BetDto.builder()
                .currentBet(currentBetId)
                .previousBet(currentBetId)
                .money(BigDecimal.ZERO)
                .user(userId)
                .build();
        }
        
        // amqpTemplate.convertAndSend(QUEUE, bet);
    }
}
