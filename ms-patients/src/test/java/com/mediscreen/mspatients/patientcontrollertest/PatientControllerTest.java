package com.mediscreen.mspatients.patientcontrollertest;

import com.mediscreen.mspatients.Controller.PatientsController;
import com.mediscreen.mspatients.model.Patient;
import com.mediscreen.mspatients.service.PatientsService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource("/application-test.properties")
public class PatientControllerTest {
    @Mock
    PatientsService patientsService;

    PatientsController patientsController;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        patientsController = new PatientsController(patientsService);
    }
    @Test
    public void getPatientsTest() {
        Iterable<Patient>list1 = new ArrayList<>();
        when(patientsService.getPatients()).thenReturn(list1);
        //Iterable<Patient> list2 = patientsController.getPatients();

        //Assertions.assertTrue(list1==list2);
    }
    @Test
    public void getPatientByIdTest(){
        Patient patient1 = new Patient();
        patient1.setPatient_id(1);
        when(patientsService.getPatientById(1)).thenReturn(patient1);
       // Patient patient2 = patientsController.getPatientById(1);
       // verify(patientsService, times(1)).getPatientById(1);
        //Assertions.assertTrue(patient2 ==patient1);
    }
    @Test
    public void getPatientByNameTest() {
        Patient patient1 = new Patient();
        patient1.setFamily("family");
        patient1.setGiven("given");
        when(patientsService.getPatientByFullName(any(String.class), any(String.class))).thenReturn(patient1);
        //assertEquals(patient1, patientsController.getPatientByName("family", "given"));


    }
    @Test
    public void updatePatientTest(){
        Patient patient = new Patient();
        patient.setPatient_id(1);
        Mockito.doNothing().when(patientsService).updatePatient("1", patient);

        patientsController.updatePatient("1", patient);

        verify(patientsService, times(1)).updatePatient("1", patient);
        }
    @Test
    public void addPatientTest(){
        Patient patient = new Patient();
        //Mockito.doNothing().when(patientsService).addPatient(patient);
        ResponseEntity response = patientsController.addPatient(patient);
        //verify(patientsService, times(1)).addPatient(patient);
        }
    @Test
        public void deletePatientTest(){
            Patient patient = new Patient();
            // Mockito.doNothing().when(repositoryNote).save(any(Note.class));
            Mockito.doNothing().when(patientsService).deletePatient("1");
            patientsController.deletePatient("1");
//doNothing().when(myList).add(isA(Integer.class),
            verify(patientsService, times(1)).deletePatient("1");

        }


}