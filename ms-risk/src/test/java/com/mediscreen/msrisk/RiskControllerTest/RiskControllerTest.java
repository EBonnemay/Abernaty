package com.mediscreen.msrisk.RiskControllerTest;

import com.mediscreen.msrisk.controller.RiskController;
import com.mediscreen.msrisk.model.RiskFactorDto;
import com.mediscreen.msrisk.service.RiskService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource("/application-test.properties")
public class RiskControllerTest {
    @Mock
    private RiskService riskService;
    @Mock
    private ResourceLoader resourceLoader;
    private RiskController riskController ;



    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        riskController = new RiskController(riskService);

    }

    @Test
    public void calculateRiskTest(){
        LocalDate dob = LocalDate.of(1971, 12, 31);
        String testNoneNote1 = "Patient states that they are 'feeling terrific' Weight at or below recommended level";
        List<String> testNoneListOfNotes = new ArrayList<>();
        testNoneListOfNotes.add(testNoneNote1);
        List<String> listOfRiskFactorsForTestPatient = new ArrayList<>();
        listOfRiskFactorsForTestPatient.add("weight");
        RiskFactorDto riskFactorDto = new RiskFactorDto("F", dob,testNoneListOfNotes );


        when(riskService.getAge(dob)).thenReturn(51);
        when (riskService.getListOfRiskFactorsForOnePatient(testNoneListOfNotes)).thenReturn(listOfRiskFactorsForTestPatient);
        when(riskService.calculateRisk("F", 51, listOfRiskFactorsForTestPatient)).thenReturn("None");


        riskController.calculateRiskFactors(riskFactorDto);

        verify(riskService, times(1)).getAge(dob);
        verify(riskService, times(1)).getListOfRiskFactorsForOnePatient(testNoneListOfNotes);
        verify(riskService, times(1)).calculateRisk("F", 51, listOfRiskFactorsForTestPatient);

    }
}
