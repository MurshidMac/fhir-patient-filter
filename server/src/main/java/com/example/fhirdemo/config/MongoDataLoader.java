package com.example.fhirdemo.config;

import com.example.fhirdemo.repository.EncounterRepository;
import com.example.fhirdemo.repository.ObservationRepository;
import com.example.fhirdemo.repository.PatientRepository;
import com.example.fhirdemo.service.FHIRDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MongoDataLoader implements CommandLineRunner {

    private final FHIRDataService fhirDataService;
    private final PatientRepository patientRepository;
    private final EncounterRepository encounterRepository;
    private final ObservationRepository observationRepository;

    @SuppressWarnings("ConstantConditions")
    @Override
    public void run(String... args) throws Exception {
        if (patientRepository.count().block() == 0L
                || encounterRepository.count().block() == 0L
                || observationRepository.count().block() == 0L) {
            fhirDataService.populate();
        }
    }
}
