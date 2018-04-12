package com.example.fhirdemo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hl7.fhir.dstu3.model.Enumerations;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
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
    private Enumerations.AdministrativeGender gender;
    private Date birthDate;
    @NotNull
    private Boolean deceased;
    private Date deceasedOn;

}
