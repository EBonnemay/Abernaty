package com.mediscreen.msrisk.controller;

import com.mediscreen.msrisk.model.RiskFactorDto;
import com.mediscreen.msrisk.service.RiskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
public class RiskController {
    private RiskService riskService;
    public RiskController(RiskService riskService){
        this.riskService = riskService;
    }
    @PostMapping("/risk/calculate")//doit correspondre
    public ResponseEntity<RiskFactorDto> calculateRiskFactors(@RequestBody RiskFactorDto riskFactorDto){
        try {
            /**RiskFactorDTO has date of birth, sex, list of messages*/
            LocalDate dob = riskFactorDto.getDob();
            int age = riskService.getAge(dob);

            List listOfRiskFactorsForOnePatient = riskService.getListOfRiskFactorsForOnePatient(riskFactorDto.getListOfOnePatientsMessages());
            /**RiskFactorDTO has also numberOfTriggers and levelOfRisk*/
            int numberOfRiskFactors = listOfRiskFactorsForOnePatient.size();
            riskFactorDto.setNumberOfTriggers(numberOfRiskFactors);//displayed on notesPage

            String levelOfRisk = riskService.calculateRisk(riskFactorDto.getSex(), age, listOfRiskFactorsForOnePatient);
            riskFactorDto.setLevelOfRisk(levelOfRisk);
            return ResponseEntity.ok(riskFactorDto);
        }catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

}
