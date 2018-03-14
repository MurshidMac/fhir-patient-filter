package com.example.fhirdemo.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

@Entity
public class Observation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @NotNull
    private String id;
    @NotNull
    private String fullId;
    @NotNull
    @Enumerated(EnumType.STRING)
    private org.hl7.fhir.dstu3.model.Observation.ObservationStatus status;
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date effectiveStartPeriod;
    @NotNull
    @JoinColumn(name = "encounter", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Encounter encounter;

    public Observation() {
    }

    public Observation(@NotNull String id) {
        this.id = id;
    }

    public Observation(@NotNull String id, @NotNull String fullId, org.hl7.fhir.dstu3.model.Observation.ObservationStatus status, @NotNull Date effectiveStartPeriod) {
        this.id = id;
        this.fullId = fullId;
        this.status = status;
        this.effectiveStartPeriod = effectiveStartPeriod;
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

    public org.hl7.fhir.dstu3.model.Observation.ObservationStatus getStatus() {
        return status;
    }

    public void setStatus(org.hl7.fhir.dstu3.model.Observation.ObservationStatus status) {
        this.status = status;
    }

    public Date getEffectiveStartPeriod() {
        return effectiveStartPeriod;
    }

    public void setEffectiveStartPeriod(Date effectiveStartPeriod) {
        this.effectiveStartPeriod = effectiveStartPeriod;
    }

    public Encounter getEncounter() {
        return encounter;
    }

    public void setEncounter(Encounter encounter) {
        this.encounter = encounter;
    }
}
