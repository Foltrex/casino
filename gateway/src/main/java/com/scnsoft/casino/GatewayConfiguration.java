package com.scnsoft.casino;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.netty.handler.logging.LogLevel;
import io.netty.resolver.DefaultAddressResolverGroup;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

@Configuration
public class GatewayConfiguration {
    // @Bean
    // public HttpClient httpClient() {
    //     return HttpClient.create().resolver(DefaultAddressResolverGroup.INSTANCE);
    // }
    @Bean
    HttpClient httpClient() {
        return HttpClient.create().wiretap("LoggingFilter", LogLevel.INFO, AdvancedByteBufFormat.TEXTUAL);
    }
}
