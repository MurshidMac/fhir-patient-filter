package com.example.fhirdemo.handler;

import com.example.fhirdemo.model.Patient;
import com.example.fhirdemo.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.hl7.fhir.dstu3.model.Enumerations;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.sql.Date;

@Component
@RequiredArgsConstructor
public class PatientHandler {

    private final PatientRepository patientRepository;

    public Mono<ServerResponse> getAll() {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(patientRepository.findAll(), Patient.class);
    }

    public Mono<ServerResponse> getAllByName(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(patientRepository
                        .findAllByNameIgnoreCaseContaining(request.queryParam("q").get()), Patient.class);
    }

    public Mono<ServerResponse> getAllByActive(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(patientRepository
                        .findAllByActiveEquals(Boolean.parseBoolean(request.queryParam("q").get())),
                        Patient.class);
    }

    public Mono<ServerResponse> getAllByGender(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(patientRepository
                        .findAllByGender(Enumerations.AdministrativeGender.valueOf(request.queryParam("q").get())),
                        Patient.class);
    }

    public Mono<ServerResponse> getAllByDeceased(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(patientRepository
                        .findAllByDeceasedEquals(Boolean.parseBoolean(request.queryParam("q").get())),
                        Patient.class);
    }

    public Mono<ServerResponse> getAllByDeceasedOn(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(patientRepository
                        .findAllByDeceasedOn(Date.valueOf(request.queryParam("q").get())), Patient.class);
    }

    public Mono<ServerResponse> getAllByBirthDate(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(patientRepository
                        .findAllByBirthDate(Date.valueOf(request.queryParam("q").get())), Patient.class);
    }



}
