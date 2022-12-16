package com.scnsoft.casino;

import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

@Component
public record InvoiceHandler(PDFGenerator generator) {

    public Mono<ServerResponse> generateReport(ServerRequest request) {
        UUID userId = UUID.fromString(request.pathVariable("userId"));
        generator.generateInvoiceReportForUser(userId);
        return ServerResponse.ok().build();
    }
}
