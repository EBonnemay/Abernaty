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

import java.rmi.NoSuchObjectException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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
    public void getPatientsWithNoContentTest() {
        Iterable<Patient>list1 = new ArrayList<>();
        when(patientsService.getPatients()).thenReturn(list1);
        ResponseEntity<Iterable<Patient>> responseWithList2 = patientsController.getPatients();

        Assertions.assertEquals(ResponseEntity.noContent().build(),responseWithList2);
    }
    @Test
    public void getPatientsWithContentTest() {
        Iterable<Patient>iteratorPatients = new ArrayList<>();
        List<Patient> listPatients = new ArrayList();
        Patient patient1 = new Patient();
        Patient patient2 = new Patient();
        listPatients.add(patient1);
        listPatients.add(patient2);

        when(patientsService.getPatients()).thenReturn(listPatients);
        ResponseEntity<Iterable<Patient>> responseWithList2 = patientsController.getPatients();

        Assertions.assertEquals(ResponseEntity.ok(listPatients),responseWithList2);
    }

    @Test
    public void getExistingPatientByIdTest(){
        Patient patient1 = new Patient();
        patient1.setPatient_id(1);
        when(patientsService.getPatientById(1)).thenReturn(patient1);
        ResponseEntity<Patient> response = patientsController.getPatientById(1);
        verify(patientsService, times(1)).getPatientById(1);
        Assertions.assertTrue(response.getBody() ==patient1);
    }
    @Test
    public void getNonExistingPatientByIdTest(){
        Patient patient1 = new Patient();
        patient1.setPatient_id(1);
        when(patientsService.getPatientById(1)).thenThrow(NoSuchElementException.class);
        Assertions.assertEquals(ResponseEntity.notFound().build(),patientsController.getPatientById(1));

    }
    @Test
    public void getPatIdFromFamilyAndGivenTest() {
        Patient patient1 = new Patient();
        patient1.setFamily("family");
        patient1.setGiven("given");
        patient1.setPatient_id(1);
        when(patientsService.getPatientByFullName(any(String.class), any(String.class))).thenReturn(patient1);
        assertEquals(1, patientsController.getPatIdFromFamilyAndGiven("family", "given").getBody());


    }
    @Test
    public void getPatIdFromNonExistingFamilyAndGivenTest() {
        Patient patient1 = new Patient();
        patient1.setFamily("family");
        patient1.setGiven("given");
        patient1.setPatient_id(1);
        when(patientsService.getPatientByFullName(any(String.class), any(String.class))).thenThrow(NoSuchElementException.class);
        assertEquals(ResponseEntity.notFound().build(), patientsController.getPatIdFromFamilyAndGiven("family", "given"));


    }
    @Test
    public void updatePatientTest(){
        Patient patient = new Patient();
        patient.setPatient_id(1);
        when(patientsService.existsById(1)).thenReturn(true);
        when(patientsService.updatePatient("1", patient)).thenReturn(patient);

        patientsController.updatePatient("1", patient);

        verify(patientsService, times(1)).updatePatient("1", patient);
        verify(patientsService, times(1)).existsById(1);
        }
    @Test
    public void addPatientTest() throws Exception {
        Patient patient = new Patient();
        //Mockito.doNothing().when(patientsService).addPatient(patient);
        when (patientsService.addPatient(patient)).thenReturn(patient);
        ResponseEntity response = patientsController.addPatient(patient);
        verify(patientsService, times(1)).addPatient(patient);
        assertEquals(patient, response.getBody());
        }
    @Test
    public void addNonExistingPatientTest() throws Exception {
        Patient patient = new Patient();
        //Mockito.doNothing().when(patientsService).addPatient(patient);
        when (patientsService.addPatient(patient)).thenThrow(Exception.class);
        patientsController.addPatient(patient);
        verify(patientsService, times(1)).addPatient(patient);
        assertEquals(ResponseEntity.badRequest().build(), patientsController.addPatient(patient));
    }
    @Test
        public void deletePatientTest(){
        Patient patient = new Patient();
    // Mockito.doNothing().when(repositoryNote).save(any(Note.class));
        when (patientsService.existsById(1)).thenReturn(true);
        Mockito.doNothing().when(patientsService).deletePatient("1");

        patientsController.deletePatient("1");
//doNothing().when(myList).add(isA(Integer.class),
        verify(patientsService, times(1)).existsById(1);
        verify(patientsService, times(1)).deletePatient("1");


        }
    @Test
    public void deleteNonExistingPatientTest(){
        Patient patient = new Patient();
        // Mockito.doNothing().when(repositoryNote).save(any(Note.class));
        when (patientsService.existsById(1)).thenReturn(false);
        Mockito.doNothing().when(patientsService).deletePatient("1");

        patientsController.deletePatient("1");
//doNothing().when(myList).add(isA(Integer.class),
        verify(patientsService, times(1)).existsById(1);
        assertEquals(ResponseEntity.notFound().build(), patientsController.deletePatient("1"));


    }


}