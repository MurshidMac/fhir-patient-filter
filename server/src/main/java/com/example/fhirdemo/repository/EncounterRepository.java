package com.example.fhirdemo.repository;

import com.example.fhirdemo.model.Encounter;
import org.springframework.data.repository.CrudRepository;

public interface EncounterRepository extends CrudRepository<Encounter, String> {
}
