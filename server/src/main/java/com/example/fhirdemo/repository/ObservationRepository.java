package com.example.fhirdemo.repository;

import com.example.fhirdemo.model.Observation;
import org.springframework.data.repository.CrudRepository;

public interface ObservationRepository extends CrudRepository<Observation, String> {
}
