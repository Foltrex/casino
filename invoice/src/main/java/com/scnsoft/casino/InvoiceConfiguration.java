package com.scnsoft.casino;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class InvoiceConfiguration {
    private static final String QUEUE_NAME = "bet.queue";

    // http

    @Autowired
    private BetService betService;

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @RabbitListener(queues = QUEUE_NAME)
    public void processBetQueue(Bet bet) {
        betService.save(bet).subscribe();
        // WebClient client = WebClient.create();
    }
}
