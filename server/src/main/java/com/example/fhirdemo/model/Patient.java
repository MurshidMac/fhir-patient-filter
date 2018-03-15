package com.example.fhirdemo.model;

import org.hl7.fhir.dstu3.model.Enumerations;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

@Entity
public class Patient implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @NotNull
    private String id;
    @NotNull
    private String fullId;
    private String name;
    @NotNull
    private Boolean active;
    @Enumerated(EnumType.STRING)
    private Enumerations.AdministrativeGender gender;
    @Temporal(TemporalType.DATE)
    private Date birthDate;
    @NotNull
    private Boolean deceased;
    @Temporal(TemporalType.DATE)
    private Date deceasedOn;
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private Collection<Encounter> encounters;

    public Patient() {
    }

    public Patient(@NotNull String id) {
        this.id = id;
    }

    public Patient(@NotNull String id, @NotNull String fullId, String name, @NotNull Boolean active, Enumerations.AdministrativeGender gender, @NotNull Date birthDate, @NotNull Boolean deceased, Date deceasedOn) {
        this.id = id;
        this.fullId = fullId;
        this.name = name;
        this.active = active;
        this.gender = gender;
        this.birthDate = birthDate;
        this.deceased = deceased;
        this.deceasedOn = deceasedOn;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Enumerations.AdministrativeGender getGender() {
        return gender;
    }

    public void setGender(Enumerations.AdministrativeGender gender) {
        this.gender = gender;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Boolean getDeceased() {
        return deceased;
    }

    public void setDeceased(Boolean deceased) {
        this.deceased = deceased;
    }

    public Date getDeceasedOn() {
        return deceasedOn;
    }

    public void setDeceasedOn(Date deceasedOn) {
        this.deceasedOn = deceasedOn;
    }

    public Collection<Encounter> getEncounters() {
        return encounters;
    }

    public void setEncounters(Collection<Encounter> encounters) {
        this.encounters = encounters;
    }
}
