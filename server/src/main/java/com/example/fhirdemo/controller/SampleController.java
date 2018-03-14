package com.example.fhirdemo.controller;

import com.example.fhirdemo.service.FhirDataService;
import org.hl7.fhir.exceptions.FHIRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/sample")
public class SampleController {

    @Autowired
    private FhirDataService dataService;

    @GetMapping
    public ResponseEntity populate() {
        try {
            dataService.populate();
            return ResponseEntity.ok().build();
        } catch (FHIRException  e) {
            Logger.getLogger("SampleController").log(Level.SEVERE, e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
