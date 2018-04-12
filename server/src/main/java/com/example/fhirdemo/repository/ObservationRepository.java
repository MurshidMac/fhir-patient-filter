package com.example.fhirdemo.repository;

import com.example.fhirdemo.model.Encounter;
import com.example.fhirdemo.model.Observation;
import com.example.fhirdemo.model.Patient;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ObservationRepository extends ReactiveCrudRepository<Observation, String> {

    Flux<Observation> findAllByPatient(Mono<Patient> patient);

    Flux<Observation> findAllByEncounter(Mono<Encounter> encounter);

}
