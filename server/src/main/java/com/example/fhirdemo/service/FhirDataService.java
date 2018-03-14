package com.example.fhirdemo.service;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.gclient.ICriterion;
import ca.uhn.fhir.rest.gclient.IQuery;
import com.example.fhirdemo.repository.EncounterRepository;
import com.example.fhirdemo.repository.ObservationRepository;
import com.example.fhirdemo.repository.PatientRepository;
import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.exceptions.FHIRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FhirDataService {

    private FhirContext fhirContext = FhirContext.forDstu3();

    public static final String TMPDIR = "/tmp/tmp.8T0q0MhsOD";

    @Value("${fhir.server.url}")
    private String server;

    @Value("${fhir.data.patients.count}")
    private int patientCount;

    @Value("${fhir.data.encounters.count}")
    private int encounterCount;

    @Value("${fhir.data.observations.count}")
    private int observationCount;

    private final PatientRepository patientRepository;

    private final ObservationRepository observationRepository;

    private final EncounterRepository encounterRepository;

    @Autowired
    public FhirDataService(PatientRepository patientRepository, ObservationRepository observationRepository, EncounterRepository encounterRepository) {
        this.patientRepository = patientRepository;
        this.observationRepository = observationRepository;
        this.encounterRepository = encounterRepository;
    }

    public void populate() throws FHIRException {
        Bundle patients;
        IGenericClient client = fhirContext.newRestfulGenericClient(server);

        // Retrieve patients
        patients = getData(client, patientCount, Patient.class, null);

        // Store data in patient table
        for (Bundle.BundleEntryComponent entry :
                patients.getEntry()) {
            Patient fhirPatient = (Patient) entry.getResource();
            com.example.fhirdemo.model.Patient patient =
                    new com.example.fhirdemo.model.Patient(
                            fhirPatient.getIdElement().getIdPart(),
                            fhirPatient.getId(),
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
            patientRepository.save(patient);
        }

        patientRepository.findAll().forEach(patient -> {
            // Retrieve encounters based on the patients
            Bundle encounterBundle =
                    getData(client, encounterCount, Encounter.class, Encounter.PATIENT.hasId(patient.getFullId()));
            for (Bundle.BundleEntryComponent entry :
                    encounterBundle.getEntry()) {
                Encounter fhirEncounter = (Encounter) entry.getResource();
                com.example.fhirdemo.model.Encounter encounter =
                        new com.example.fhirdemo.model.Encounter(
                                fhirEncounter.getIdElement().getIdPart(),
                                fhirEncounter.getId(),
                                fhirEncounter.getStatus(),
                                fhirEncounter.getTypeFirstRep().getText(),
                                fhirEncounter.getPeriod().getStart(),
                                fhirEncounter.getReasonFirstRep().getText()
                        );
                encounter.setPatient(patient);
                encounterRepository.save(encounter);
            }

        });

        // Retrieve observations based on the patients
        for (com.example.fhirdemo.model.Encounter encounter : encounterRepository.findAll()) {
            Bundle observationBundle =
                    getData(client, observationCount, Observation.class, Observation.ENCOUNTER.hasId(encounter.getFullId()));
            for (Bundle.BundleEntryComponent entry :
                    observationBundle.getEntry()) {
                Observation fhirObservation = (Observation) entry.getResource();
                com.example.fhirdemo.model.Observation observation =
                        new com.example.fhirdemo.model.Observation(
                                fhirObservation.getIdElement().getIdPart(),
                                fhirObservation.getId(),
                                fhirObservation.getStatus(),
                                fhirObservation.getEffectivePeriod().getStart()
                        );
                observation.setEncounter(encounter);
                observationRepository.save(observation);
            }
        }

    }

    public Bundle getData(IGenericClient client, int count, Class<? extends DomainResource> type, ICriterion criterion) {
        IQuery<Bundle> query = client.search().forResource(type)
                .count(count)
                .returnBundle(Bundle.class);
        if (criterion != null) {
            query.where(criterion);
        }
        return query.execute();
    }

}
