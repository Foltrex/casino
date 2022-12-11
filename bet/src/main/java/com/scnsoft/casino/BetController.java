package com.scnsoft.casino;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;

@RestController
public class BetController {
    private static final String USER_ID = "user";
    private static final String BET_ID = "currentBet";
    private static final String MONEY_AMOUNT = "money";

    @Value("${app.winrate:0.5}")
    private static double WINRATE;

    @PostMapping("/bet")
    public BetDto makeBet(@RequestBody ObjectNode json) {
        String userIdString = json.get(USER_ID).asText();
        UUID userId = UUID.fromString(userIdString);

        String currentBetIdString = json.get(BET_ID).asText();
        UUID currentBetId = UUID.fromString(currentBetIdString);

        String moneyAmountString = json.get(MONEY_AMOUNT).asText();
        BigDecimal moneyAmount = new BigDecimal(moneyAmountString);

        if (Math.random() > WINRATE) {
            UUID newBetId = UUID.randomUUID();
            return BetDto.builder()
                    .currentBet(newBetId)
                    .previousBet(currentBetId)
                    .money(moneyAmount)
                    .user(userId)
                    .build();
        } else {
            return BetDto.builder()
                .currentBet(currentBetId)
                .previousBet(currentBetId)
                .money(moneyAmount)
                .user(userId)
                .build();
        }
    }
}
