package com.mediscreen.abernatyclient.proxies;

import com.mediscreen.abernatyclient.beans.PatientBean;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "microservice-patients", url = "${app.services.patients.url}")
public interface MicroservicePatientsProxy {
    //@Operation(value="récupère la liste des patients")
    @GetMapping(value = "/patient/all")
    List<PatientBean> getPatients();


    @GetMapping(value = "/patient/{patId}")
    PatientBean getPatientById(@PathVariable("patId") int id);

   // @GetMapping(value = "/patient/ByFullName/{family}/{given}")
    //PatientBean retrievePatient(@PathVariable("family") String family, @PathVariable("given") String given);

    @PostMapping(value = "/patient/update/{patId}")
    public PatientBean updatePatient(@PathVariable("patId") String patId, PatientBean patient);

    @PostMapping(value = "/patient/add")
    public PatientBean addPatient( PatientBean patient);
    @GetMapping(value = "/patient/delete/{patId}")
    public void deletePatient(@PathVariable("patId")String patId);
    @GetMapping("/patient")
    public int getPatIdFromFamilyAndGiven(@RequestParam ("family") String family, @RequestParam("given")String given);
}
