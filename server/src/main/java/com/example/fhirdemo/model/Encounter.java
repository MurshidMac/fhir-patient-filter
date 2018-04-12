package com.example.fhirdemo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
public class Encounter implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @NotNull
    private String id;
    @NotNull
    private String fullId;
    @NotNull
    private org.hl7.fhir.dstu3.model.Encounter.EncounterStatus status;
    @NotNull
    private String type;
    @NotNull
    private Date startPeriod;
    private String reason;
    private Patient patient;
}
