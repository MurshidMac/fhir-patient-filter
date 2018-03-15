package com.example.fhirdemo.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

@Entity
public class Encounter implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @NotNull
    private String id;
    @NotNull
    private String fullId;
    @NotNull
    @Enumerated(EnumType.STRING)
    private org.hl7.fhir.dstu3.model.Encounter.EncounterStatus status;
    @NotNull
    private String type;
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date startPeriod;
    private String reason;
    @JoinColumn(name = "patient", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Patient patient;
    @OneToMany(mappedBy = "encounter", cascade = CascadeType.ALL)
    private Collection<Observation> observations;

    public Encounter() {
    }

    public Encounter(@NotNull String id) {
        this.id = id;
    }

    public Encounter(@NotNull String id, @NotNull String fullId, org.hl7.fhir.dstu3.model.Encounter.EncounterStatus status, @NotNull String type, @NotNull Date startPeriod, @NotNull String reason) {
        this.id = id;
        this.fullId = fullId;
        this.status = status;
        this.type = type;
        this.startPeriod = startPeriod;
        this.reason = reason;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullId() {
        return fullId;
    }

    public void setFullId(String fullId) {
        this.fullId = fullId;
    }

    public org.hl7.fhir.dstu3.model.Encounter.EncounterStatus getStatus() {
        return status;
    }

    public void setStatus(org.hl7.fhir.dstu3.model.Encounter.EncounterStatus status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getStartPeriod() {
        return startPeriod;
    }

    public void setStartPeriod(Date startPeriod) {
        this.startPeriod = startPeriod;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Collection<Observation> getObservations() {
        return observations;
    }

    public void setObservations(Collection<Observation> observations) {
        this.observations = observations;
    }
}
