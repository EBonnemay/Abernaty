package com.mediscreen.msrisk.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
@Data
@RequiredArgsConstructor

public class RiskFactorDto {
    @NonNull
    private String sex;
    @NonNull
    private LocalDate dob;
    @NonNull

    private List<String> listOfOnePatientsMessages;
    private int numberOfTriggers;

    private String levelOfRisk;

}
