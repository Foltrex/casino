package com.scnsoft.casino;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableRabbit
@RequiredArgsConstructor
public class InvoiceConfiguration {
    private static final String QUEUE_NAME = "bet.queue";

    private final BetService betService;

    
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @RabbitListener(queues = QUEUE_NAME)
    public void processBetQueue(Bet bet) {
        betService.save(bet).subscribe();
    }


    @Bean
    public RouterFunction<ServerResponse> routes(InvoiceHandler invoiceHandler) {
        return route(GET("/report/{userId}"), invoiceHandler::generateReport);
    }
}
