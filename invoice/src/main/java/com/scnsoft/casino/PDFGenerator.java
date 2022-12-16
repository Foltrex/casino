package com.scnsoft.casino;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.google.common.base.Objects;
import com.lowagie.text.DocumentException;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Log4j2
public record PDFGenerator(BetService betService) {
    private static final String PDF_FILE_PATH = "src/main/resources/report.pdf";
    private static final String SOURCE_HTML_NAME = "report";
    private static final String HTML_FILE_SUFFIX = ".html";

    private static final String USER_ID_CONTEXT_VARIABLE_NAME = "userId";
    private static final String BETS_CONTEXT_VARIABLE_NAME = "bets";

    public void generateInvoiceReportForUser(UUID userId) {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setSuffix(HTML_FILE_SUFFIX);
        templateResolver.setTemplateMode(TemplateMode.HTML);
    
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
    
        Context context = new Context();
        context.setVariable(USER_ID_CONTEXT_VARIABLE_NAME, userId);
        betService.findAllByUserId(userId)
            .collectList()
            .flatMap(bets -> validate(bets))
            .subscribe(bets -> context.setVariable(BETS_CONTEXT_VARIABLE_NAME, bets));
        
    
        String html = templateEngine.process(SOURCE_HTML_NAME, context);

        try (OutputStream outputStream = new FileOutputStream(PDF_FILE_PATH)) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(html);
            renderer.layout();
            renderer.createPDF(outputStream);
        } catch (DocumentException | IOException e) {
            log.warn(e);
        }
    }

    private Mono<List<Bet>> validate(List<Bet> bets) {
        Bet currentBet = bets.get(0);
        Bet previousBet = currentBet;
        for (Bet bet : bets) {
            currentBet = bet;
            if (previousBet != currentBet && 
                    !Objects.equal(currentBet.previousBetId(), previousBet.currentBetId())) {
                return Mono.just(Collections.emptyList());
            }

            previousBet = currentBet;
        }

        return Mono.just(bets);
    }
}
