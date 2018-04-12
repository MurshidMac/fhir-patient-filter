package com.example.fhirdemo.config;

import com.example.fhirdemo.handler.CoreHandler;
import com.example.fhirdemo.handler.PatientHandler;
import com.example.fhirdemo.model.Encounter;
import com.example.fhirdemo.model.Observation;
import com.example.fhirdemo.model.Patient;
import com.example.fhirdemo.repository.EncounterRepository;
import com.example.fhirdemo.repository.ObservationRepository;
import com.example.fhirdemo.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Configuration
@RequiredArgsConstructor
public class ReactiveConfig {

    private final PatientRepository patientRepository;
    private final EncounterRepository encounterRepository;
    private final ObservationRepository observationRepository;

    @Bean
    RouterFunction<ServerResponse> coreRoute(CoreHandler coreHandler) {
        return RouterFunctions.route(RequestPredicates.GET("/core/restock"),
                request -> coreHandler.populate());
    }

    @Bean
    RouterFunction<ServerResponse> patientRoute(PatientHandler patientHandler) {
        return RouterFunctions.route(RequestPredicates.GET("/patients"),
                request -> patientHandler.getAll())
                .andRoute(RequestPredicates.GET("/patient/search/name"),
                        patientHandler::getAllByName)
                .andRoute(RequestPredicates.GET("/patient/search/active"),
                        patientHandler::getAllByActive)
                .andRoute(RequestPredicates.GET("/patient/search/gender"),
                        patientHandler::getAllByGender)
                .andRoute(RequestPredicates.GET("/patient/search/deceased"),
                        patientHandler::getAllByDeceased)
                .andRoute(RequestPredicates.GET("/patient/search/deceasedOn"),
                        patientHandler::getAllByDeceasedOn)
                .andRoute(RequestPredicates.GET("/patient/search/birthDate"),
                        patientHandler::getAllByBirthDate);
    }

    @Bean
    RouterFunction<ServerResponse> encounterRoute() {
        return RouterFunctions.route(RequestPredicates.GET("/encounters/patient/{id}"),
                request -> {
                    Mono<Patient> patient = patientRepository.findById(request.pathVariable("id"));
                    return ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(encounterRepository.findAllByPatient(patient), Encounter.class);
                });
    }

    @Bean
    RouterFunction<ServerResponse> observationRoute() {
        return RouterFunctions.route(RequestPredicates.GET("/observations/patient/{id}"),
                request -> {
                    Mono<Patient> patient = patientRepository.findById(request.pathVariable("id"));
                    return ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(observationRepository.findAllByPatient(patient), Observation.class);
                })
                .andRoute(RequestPredicates.GET("/observations/encounter/{id}"),
                        request -> {
                            Mono<Encounter> encounter = encounterRepository.findById(request.pathVariable("id"));
                            return ServerResponse.ok()
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .body(observationRepository.findAllByEncounter(encounter), Observation.class);
                        });
    }

}
