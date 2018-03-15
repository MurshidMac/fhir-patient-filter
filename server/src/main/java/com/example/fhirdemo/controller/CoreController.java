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
@RequestMapping("/core")
public class CoreController {

    private final FhirDataService dataService;

    @Autowired
    public CoreController(FhirDataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping("/restock")
    public ResponseEntity populate() {
        try {
            dataService.populate();
            return ResponseEntity.ok().build();
        } catch (FHIRException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
