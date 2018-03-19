package com.example.fhirdemo.repository;

import com.example.fhirdemo.model.Patient;
import org.hl7.fhir.dstu3.model.Enumerations;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public interface PatientRepository extends CrudRepository<Patient, String> {

    @RestResource(path = "name")
    Iterable<Patient> findAllByNameIgnoreCaseContaining(@Param("q") String name);

    @RestResource(path = "active")
    Iterable<Patient> findAllByActiveEquals(@Param("q") Boolean active);

    @RestResource(path = "gender")
    Iterable<Patient> findAllByGender(@Param("q") Enumerations.AdministrativeGender gender);

    @RestResource(path = "deceased")
    Iterable<Patient> findAllByDeceasedEquals(@Param("q") Boolean deceased);

    @RestResource(path = "deceasedOn")
    Iterable<Patient> findAllByDeceasedOn(@Param("q") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date deceasedOn);

    @RestResource(path = "birthDate")
    Iterable<Patient> findAllByBirthDate(@Param("q") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date birthDate);

}
