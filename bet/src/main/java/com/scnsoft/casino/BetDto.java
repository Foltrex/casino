package com.scnsoft.casino;

import java.math.BigDecimal;
import java.util.UUID;

// import org.springframework.data.annotation.Id;
// import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;

// @Document
@Builder
public record BetDto(
    UUID currentBet, 
    UUID previousBet, 
    BigDecimal money, 
    UUID user
) {
}
