package com.mediscreen.mspatients.Controller;

import com.mediscreen.mspatients.exceptions.PatientNotFoundException;
import com.mediscreen.mspatients.model.Patient;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.mediscreen.mspatients.service.PatientsService;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.NoSuchElementException;

@RestController
public class PatientsController {

    static final Logger logger = LogManager.getLogger();
    private PatientsService patientsService;


    public PatientsController(PatientsService patientsService) {
        this.patientsService = patientsService;
    }

    /**
     * api request : all patients__tested postman
     */
    /*@GetMapping("/patient/all")
    public Iterable<Patient> getPatients() {
        logger.info("inside getPatients");
        return patientsService.getPatients();
    }*/
    @GetMapping("/patient/all")
    public ResponseEntity<Iterable<Patient>> getPatients() {

        Iterable<Patient> patients = patientsService.getPatients();
        if (patients.iterator().hasNext()) {
            return ResponseEntity.ok(patients);
        } else {
            return ResponseEntity.noContent().build();
        }
    }
    //@GetMapping("/patient/{id}")
    //public Patient getPatientById(@PathVariable Integer id){
    //return patientsService.getPatientById(id);
//

    /**
     * api request : one patient by id__tested postman
     */
    @GetMapping("/patient/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Integer id) {
        try {
            Patient patient = patientsService.getPatientById(id);
            return ResponseEntity.ok(patient);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }

    }

    /**
     * api request : one patient by family and given as PathVariables__tested postman
     */
    //@GetMapping("/ByFullName/{family}/{given}")
    @GetMapping("/patient")
    public ResponseEntity<Integer> getPatIdFromFamilyAndGiven(@RequestParam("family") String family, @RequestParam("given") String given) {
        try {
            Patient patient = patientsService.getPatientByFullName(family, given);
            int patId = patient.getPatient_id();
            return ResponseEntity.ok(patId);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }

    }

    /**
     * api request : update patient by id and Patient RequestBody__tested postman
     */
    @PostMapping("/patient/update/{id}")

    public ResponseEntity<Patient> updatePatient(@PathVariable("id") String id, @RequestBody Patient patient) {
        Patient updatedPatient = new Patient();
        if (!patientsService.existsById(Integer.parseInt(id))) {
            return ResponseEntity.notFound().build();//ok
        }

        try {
            updatedPatient = patientsService.updatePatient(id, patient);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();//ok
        }

        return ResponseEntity.ok(updatedPatient);//ok
    }

    /**
     * api request : add one patient with Patient object as parameter__tested postman
     */
    @PostMapping("/patient/add")
    public ResponseEntity<Patient> addPatient(@RequestBody Patient patient) {
        Patient newPatient = new Patient();
        try {
            newPatient = patientsService.addPatient(patient);
            logger.info("person added successfully");
            URI location = ServletUriComponentsBuilder.fromCurrentRequestUri().replacePath("/patient/{id}").buildAndExpand(patient.getPatient_id()).toUri();
            logger.info("uri location is " + location.toString());
            //return ResponseEntity.created(location).build();
            return ResponseEntity.created(location).body(newPatient);
            //return ResponseEntity.
        } catch (Exception e) {
            //logger.error("item not found");
            //throw new RuntimeException("not possible to add this patient");
            return ResponseEntity.badRequest().build();
        }

    }

    /**
     * api request : delete one patient by id__tested postman__ be careful about handling errors, for now 200 OK!! and person deleted successfully even if non existing id
     */
    @GetMapping("/patient/delete/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable("id") String id) {
        System.out.println("in PatientController, patient's id to delete : " + id);
        if (!patientsService.existsById(Integer.valueOf(id))) {
            return ResponseEntity.notFound().build();
        } else {
            patientsService.deletePatient(id);
            logger.info("person deleted successfully");
            return ResponseEntity.noContent().build();
        }

    }
}
