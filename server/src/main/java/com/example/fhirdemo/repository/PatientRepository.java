package com.example.fhirdemo.repository;

import com.example.fhirdemo.model.Patient;
import org.springframework.data.repository.CrudRepository;

public interface PatientRepository extends CrudRepository<Patient, String> {
}
