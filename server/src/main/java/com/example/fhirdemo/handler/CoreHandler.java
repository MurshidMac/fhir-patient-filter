package com.example.fhirdemo.handler;

import com.example.fhirdemo.service.FhirDataService;
import lombok.RequiredArgsConstructor;
import org.hl7.fhir.exceptions.FHIRException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.logging.Level;
import java.util.logging.Logger;

@Component
@RequiredArgsConstructor
public class CoreHandler {

    private final FhirDataService dataService;

    public Mono<ServerResponse> populate() {
        try {
            dataService.populate();
            return ServerResponse.ok().build();
        } catch (FHIRException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
            return ServerResponse.badRequest().body(BodyInserters.fromObject(e.getMessage()));
        }
    }
}
