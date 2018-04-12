package com.example.fhirdemo.service;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.gclient.IQuery;
import com.example.fhirdemo.repository.EncounterRepository;
import com.example.fhirdemo.repository.ObservationRepository;
import com.example.fhirdemo.repository.PatientRepository;
import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.exceptions.FHIRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.stream.Collectors;

@Service
public class FHIRDataService {

    private final PatientRepository patientRepository;
    private final ObservationRepository observationRepository;
    private final EncounterRepository encounterRepository;

    private FhirContext fhirContext = FhirContext.forDstu3();
    @Value("${fhir.server.url}")
    private String server;
    @Value("${fhir.data.patients.count}")
    private int patientCount;
    @Value("${fhir.data.encounters.count}")
    private int encounterCount;
    @Value("${fhir.data.observations.count}")
    private int observationCount;

    private Deque<Encounter> encounters;
    private Deque<Observation> observations;

    @Autowired
    public FHIRDataService(PatientRepository patientRepository, ObservationRepository observationRepository, EncounterRepository encounterRepository) {
        this.patientRepository = patientRepository;
        this.observationRepository = observationRepository;
        this.encounterRepository = encounterRepository;
    }

    /**
     * Re-stock the database with FHIR DSTU3 values from the supplied server
     */
    public void populate() throws FHIRException {
        Bundle patients;
        IGenericClient client = fhirContext.newRestfulGenericClient(server);

        // Retrieve data
        patients = getData(client, patientCount, Patient.class);
        encounters = getData(client, encounterCount * patientCount, Encounter.class)
                .getEntry()
                .stream()
                .map(i -> (Encounter) i.getResource())
                .collect(Collectors.toCollection(ArrayDeque::new));
        observations = getData(client, observationCount * encounterCount * patientCount,
                Observation.class)
                .getEntry()
                .stream()
                .map(i -> (Observation) i.getResource())
                .collect(Collectors.toCollection(ArrayDeque::new));

        // Save patients to Patient collection
        for (Bundle.BundleEntryComponent entry :
                patients.getEntry()) {
            Patient fhirPatient = (Patient) entry.getResource();
            com.example.fhirdemo.model.Patient patient =
                    new com.example.fhirdemo.model.Patient(
                            fhirPatient.getIdElement().getIdPart(),
                            fhirPatient.getId(),
                            fhirPatient.getNameFirstRep().getNameAsSingleString(),
                            fhirPatient.getActive(),
                            fhirPatient.getGender(),
                            fhirPatient.getBirthDate(),
                            false, null
                    );
            if (fhirPatient.hasDeceased()) {
                patient.setDeceased(fhirPatient.getDeceasedBooleanType().booleanValue());
                if (fhirPatient.hasDeceasedDateTimeType()) {
                    patient.setDeceasedOn(fhirPatient.getDeceasedDateTimeType().getValue());
                }
            }
            System.out.println(fhirPatient.getId());
            System.out.println(patient);
            patientRepository.save(patient).subscribe(this::saveEncounters);
        }

    }

    /**
     * Save encounters to Encounter collection for the given patient
     */
    private void saveEncounters(com.example.fhirdemo.model.Patient patient) {
        for (int i = 0; i < encounterCount; i++) {
            Encounter fhirEncounter = encounters.pop();
            com.example.fhirdemo.model.Encounter encounter =
                    new com.example.fhirdemo.model.Encounter(
                            fhirEncounter.getIdElement().getIdPart(),
                            fhirEncounter.getId(),
                            fhirEncounter.getStatus(),
                            fhirEncounter.getTypeFirstRep().getText(),
                            fhirEncounter.getPeriod().getStart(),
                            fhirEncounter.getReasonFirstRep().getText(),
                            patient
                    );
            encounterRepository.save(encounter).subscribe(e -> saveObservations(e, patient));
        }
    }

    /**
     * Save observations to Observation collection for the given encounter
     */
    private void saveObservations(com.example.fhirdemo.model.Encounter encounter, com.example.fhirdemo.model.Patient patient) {
        for (int i = 0; i < observationCount; i++) {
            Observation fhirObservation = observations.pop();
            com.example.fhirdemo.model.Observation observation =
                    new com.example.fhirdemo.model.Observation(
                            fhirObservation.getIdElement().getIdPart(),
                            fhirObservation.getId(),
                            fhirObservation.getStatus(),
                            null,
                            patient,
                            encounter
                    );
            try {
                if (fhirObservation.getEffective() instanceof Period) {
                    observation.setEffectiveStartPeriod(fhirObservation.getEffectivePeriod().getStart());

                } else {
                    observation.setEffectiveStartPeriod(fhirObservation.getEffectiveDateTimeType().getValue());
                }
            } catch (FHIRException e) {
                e.printStackTrace();
            }
            System.out.println(observation);
            observationRepository.save(observation).subscribe();
        }
    }

    /**
     * Get a bundle of data of the given type matching the criteria and count.
     *
     * @param client Client object used to execute the query
     * @param count  No. of entries in the bundle
     * @param type   Type of resource to retrieve and return
     * @return bundle of resources of type.
     */
    private Bundle getData(IGenericClient client, int count, Class<? extends DomainResource> type) {
        IQuery<Bundle> query = client.search().forResource(type)
                .count(count)
                .returnBundle(Bundle.class);
        return query.execute();
    }

}
