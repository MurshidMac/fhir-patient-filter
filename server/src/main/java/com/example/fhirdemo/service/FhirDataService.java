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

    private final PatientRepository patientRepository;
    private final ObservationRepository observationRepository;
    private final EncounterRepository encounterRepository;
    IGenericClient client;
    private FhirContext fhirContext = FhirContext.forDstu3();
    @Value("${fhir.server.url}")
    private String server;
    @Value("${fhir.data.patients.count}")
    private int patientCount;
    @Value("${fhir.data.encounters.count}")
    private int encounterCount;
    @Value("${fhir.data.observations.count}")
    private int observationCount;

    @Autowired
    public FhirDataService(PatientRepository patientRepository, ObservationRepository observationRepository, EncounterRepository encounterRepository) {
        this.patientRepository = patientRepository;
        this.observationRepository = observationRepository;
        this.encounterRepository = encounterRepository;
    }

    /**
     * Re-stock the database with FHIR DSTU3 values from the supplied server
     *
     * @throws FHIRException
     */
    public void populate() throws FHIRException {
        Bundle patients;
        client = fhirContext.newRestfulGenericClient(server);

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

    private void saveEncounters(com.example.fhirdemo.model.Patient patient) {
        // Retrieve encounters for each patient
        Bundle encounterBundle = getData(client, encounterCount * patientCount, Encounter.class, null);
        for (Bundle.BundleEntryComponent entryComponent : encounterBundle.getEntry()) {
            Encounter fhirEncounter = (Encounter) entryComponent.getResource();
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

    private void saveObservations(com.example.fhirdemo.model.Encounter encounter, com.example.fhirdemo.model.Patient patient) {
        Bundle observationBundle = getData(client, observationCount * encounterCount * patientCount, Observation.class, null);
        for (Bundle.BundleEntryComponent entryComponent : observationBundle.getEntry()) {
            Observation fhirObservation = (Observation) entryComponent.getResource();
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
     * @param client    Client object used to execute the query
     * @param count     No. of entries in the bundle
     * @param type      Type of resource to retrieve and return
     * @param criterion Criteria to retrieve the data
     * @return bundle of resources of type.
     */
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
