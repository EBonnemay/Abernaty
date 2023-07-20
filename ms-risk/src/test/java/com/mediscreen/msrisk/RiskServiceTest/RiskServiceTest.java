package com.mediscreen.msrisk.RiskServiceTest;

import com.fasterxml.jackson.core.async.ByteArrayFeeder;
import com.mediscreen.msrisk.model.RiskFactorDto;
import com.mediscreen.msrisk.service.RiskService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.TestPropertySource;


import java.io.IOException;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource("/application-test.properties")


public class RiskServiceTest {
    private RiskService riskService;
    String testNoneNote1 = "Patient states that they are 'feeling terrific' Weight at or below recommended level";
    String testBorderlineNote1 = "Patient states that they are feeling a great deal of stress at work Patient also complains that their hearing seems Abnormal as of late";
    String testBorderlineNote2 = "Patient states that they have had a Reaction to medication within last 3 months Patient also complains that their hearing continues to be problematic" ;
    String testInDangerNote1 = "Patient states that they are short term Smoker";
    String testInDangerNote2 = "Patient states that they quit within last year Patient also complains that of Abnormal breathing spells Lab reports Cholesterol LDL high";
    String testEarlyOnsetNote1 = "Patient states that walking up stairs has become difficult Patient also complains that they are having shortness of breath Lab results indicate Antibodies present elevated Reaction to medication";
    String testEarlyOnsetNote2 = "Patient states that they are experiencing back pain when seated for a long time";
    String testEarlyOnsetNote3 = "Patient states that they are a short term Smoker Hemoglobin A1C above recommended level";
    String testEarlyOnsetNote4 = "Patient states that Body Height, Body Weight, Cholesterol, Dizziness and Reaction";
    List<String> testNoneListOfNotes = new ArrayList<>();
    List<String> testBorderlineListOfNotes = new ArrayList<>();
    List<String> testInDangerListOfNotes = new ArrayList<>();
    List<String> testEarlyOnsetListOfNotes = new ArrayList<>();


    LocalDate testNoneBirth = LocalDate.of(1966, 12, 31);
    LocalDate testBorderlineBirth = LocalDate.of(1945, 6, 24);
    LocalDate testInDangerBirth = LocalDate.of(2004, 6, 18);
    LocalDate testEarlyOnsetBirth = LocalDate.of(2002, 6, 28);

    List<String>listOfRiskFactorsForNone = new ArrayList<>();
    List<String>listOfRiskFactorsForBorderline = new ArrayList<>();

    List<String> listOfRiskFactorsForInDanger = new ArrayList<>();

    List<String> listOfRiskFactorsForEarlyOnset = new ArrayList<>();
    @BeforeAll
    public void setUp() throws IOException {
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        riskService = new RiskService(resourceLoader);
    }
    @BeforeAll

    public void setting() {
        testNoneListOfNotes.add(testNoneNote1);
        testBorderlineListOfNotes.add(testBorderlineNote1);
        testBorderlineListOfNotes.add(testBorderlineNote2);
        testInDangerListOfNotes.add(testInDangerNote1);
        testInDangerListOfNotes.add(testInDangerNote2);
        testEarlyOnsetListOfNotes.add(testEarlyOnsetNote1);
        testEarlyOnsetListOfNotes.add(testEarlyOnsetNote2);
        testEarlyOnsetListOfNotes.add(testEarlyOnsetNote3);
        testEarlyOnsetListOfNotes.add(testEarlyOnsetNote4);


        listOfRiskFactorsForNone.add("weight");



        listOfRiskFactorsForBorderline.add("abnormal");
        listOfRiskFactorsForBorderline.add("reaction");


        listOfRiskFactorsForInDanger.add("smoker");
        listOfRiskFactorsForInDanger.add("abnormal");
        listOfRiskFactorsForInDanger.add("cholesterol");


        listOfRiskFactorsForEarlyOnset.add("antibodies");
        listOfRiskFactorsForEarlyOnset.add("reaction");
        listOfRiskFactorsForEarlyOnset.add("smoker");
        listOfRiskFactorsForEarlyOnset.add("height");
        listOfRiskFactorsForEarlyOnset.add("weight");
        listOfRiskFactorsForEarlyOnset.add("cholesterol");
        listOfRiskFactorsForEarlyOnset.add("dizziness");
        listOfRiskFactorsForEarlyOnset.add("hemoglobin a1c");
    }

@Test
    public void getAgeTest(){
        //ARRANGE
        //ACT

        //ASSERT
        int age = riskService.getAge(testNoneBirth);
        assertEquals(56, age);
        age = riskService.getAge(testBorderlineBirth);
        assertEquals(78, age);
        age = riskService.getAge(testInDangerBirth);
        assertEquals(19, age);
        age = riskService.getAge(testEarlyOnsetBirth);
        assertEquals(21, age);
        //AssertThat patient TestNone is 52, patientTestBorderline is 73, patient testInDanger is 14, patient testEarlyOnset is 16
    }
    @Test
    public void getListOfRiskFactorsForOnePatientTest(){
        List<String>actualList = riskService.getListOfRiskFactorsForOnePatient(testNoneListOfNotes);
        assertTrue(listOfRiskFactorsForNone.size()==actualList.size()&&listOfRiskFactorsForNone.containsAll(actualList)&&actualList.containsAll(listOfRiskFactorsForNone));
        actualList = riskService.getListOfRiskFactorsForOnePatient(testBorderlineListOfNotes);
        assertTrue(listOfRiskFactorsForBorderline.size()==actualList.size()&&listOfRiskFactorsForBorderline.containsAll(actualList)&&actualList.containsAll(listOfRiskFactorsForBorderline));
        actualList = riskService.getListOfRiskFactorsForOnePatient(testInDangerListOfNotes);
        assertTrue(listOfRiskFactorsForInDanger.size()==actualList.size()&&listOfRiskFactorsForInDanger.containsAll(actualList)&&actualList.containsAll(listOfRiskFactorsForInDanger));
        actualList = riskService.getListOfRiskFactorsForOnePatient(testEarlyOnsetListOfNotes);
        assertTrue(listOfRiskFactorsForEarlyOnset.size()== actualList.size()&&listOfRiskFactorsForEarlyOnset.containsAll(actualList)&&actualList.containsAll(listOfRiskFactorsForEarlyOnset));

    }
        @Test
    public void calculateRiskTest(){
        assertEquals("None", riskService.calculateRisk("F", 52, listOfRiskFactorsForNone));
        assertEquals("Borderline", riskService.calculateRisk("M", 73, listOfRiskFactorsForBorderline));
        assertEquals("In danger", riskService.calculateRisk("M", 14, listOfRiskFactorsForInDanger));
        assertEquals("Early onset", riskService.calculateRisk("F", 16, listOfRiskFactorsForEarlyOnset));

    }
    @Test
    public void processWordTest(){
        //ARRANGE
        //ACT
        //ASSERT
    }
    @Test
    public void removePhraseMarksTest(){
        //ARRANGE
        String phrase = "how are you? fine, thank you;but i know it:this won't last!";
        //ACT
        //ASSERT
        assertEquals("how are you  fine  thank you but i know it this won t last ",  riskService.removePhraseMarks(phrase));
    }
    @Test
    public void removeAccentsAndUpperCases(){
        String phrase = "Ce patient a un taux d'Hémoglobine supérieur à la MOYENNE, et il a fumé des années";
        assertEquals("ce patient a un taux d'hemoglobine superieur a la moyenne, et il a fume des annees", riskService.removeAccentsAndUpperCases(phrase));

    }

    }
