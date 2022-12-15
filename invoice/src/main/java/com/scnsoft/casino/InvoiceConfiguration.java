package com.scnsoft.casino;

import java.util.Map;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class InvoiceConfiguration {
    private static final String QUEUE_NAME = "";

    @RabbitListener(queues = "bet-queue")
    public void processBetQueue(Map<String, String> message) {
        System.out.println(message);
    }
}
