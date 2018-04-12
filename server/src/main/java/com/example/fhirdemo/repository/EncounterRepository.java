package com.example.fhirdemo.repository;

import com.example.fhirdemo.model.Encounter;
import com.example.fhirdemo.model.Patient;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EncounterRepository extends ReactiveCrudRepository<Encounter, String> {

    Flux<Encounter> findAllByPatient(Mono<Patient> patient);

}
