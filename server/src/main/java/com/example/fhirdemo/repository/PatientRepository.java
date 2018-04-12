package com.example.fhirdemo.repository;

import com.example.fhirdemo.model.Patient;
import org.hl7.fhir.dstu3.model.Enumerations;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.format.annotation.DateTimeFormat;
import reactor.core.publisher.Flux;

import java.util.Date;

public interface PatientRepository extends ReactiveCrudRepository<Patient, String> {

    Flux<Patient> findAllByNameIgnoreCaseContaining(String name);

    Flux<Patient> findAllByActiveEquals(Boolean active);

    Flux<Patient> findAllByGender(Enumerations.AdministrativeGender gender);

    Flux<Patient> findAllByDeceasedEquals(Boolean deceased);

    Flux<Patient> findAllByDeceasedOn(Date deceasedOn);

    Flux<Patient> findAllByBirthDate(Date birthDate);

}
