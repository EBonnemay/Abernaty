package com.mediscreen.mspatients.patientservicetest;

import com.mediscreen.mspatients.model.Patient;
import com.mediscreen.mspatients.repository.PatientsRepository;
import com.mediscreen.mspatients.service.PatientsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource("/application-test.properties")
public class PatientServiceTest {
    @Mock
    private PatientsRepository patientsRepository;

    private PatientsService patientsService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        patientsService = new PatientsService(patientsRepository);
    }

    @Test
    public void existsByIdTest(){
        when(patientsRepository.existsById(1)).thenReturn(true);
        patientsService.existsById(1);
        verify(patientsRepository, times(1)).existsById(1);
        assertEquals(true, patientsService.existsById(1));
    }
    @Test
    public void getPatientsTest() {
        Iterable<Patient> listOfPatients = new ArrayList<>();
        when(patientsRepository.findAll()).thenReturn(listOfPatients);
        //ASSERT
        Iterable<Patient> listOfPatients2 = patientsService.getPatients();
        assertTrue(listOfPatients == listOfPatients2);
    }

    @Test
    public void getPatientByIdTest() {
        //assert
        Patient patient = new Patient();
        patient.setPatient_id(1);
        when(patientsRepository.findById(1)).thenReturn(Optional.of(patient));

        assertEquals(patient, patientsService.getPatientById(1));

    }

    @Test
    public void getPatientByFullNameTest() {
        Patient patientTest = new Patient();
        patientTest.setFamily("testFamily");
        patientTest.setGiven("testGiven");
        when(patientsRepository.findByFullName("testFamily", "testGiven")).thenReturn(Optional.of(patientTest));
        patientsService.getPatientByFullName("testFamily", "testGiven");
        verify(patientsRepository, times(1)).findByFullName(any(String.class), any(String.class));
    }


    /* public Patient updatePatient(String id, String family, String given, Date dateOfBirth, String sex, String address, String phone){
         Optional<Patient> optPatient = patientsRepository.findById(Integer.parseInt(id));
         Patient patient = optPatient.get();
         patient.setFamily(family);
         patient.setGiven(given);
         patient.setDate_of_birth(dateOfBirth);
         patient.setSex(sex);
         patient.setAddress(address);
         patient.setPhone(phone);
         patientsRepository.save(patient);
         return patient;

     }*/
    @Test
    public void updatePatientTest(){
        Patient patient = new Patient();
        patient.setPatient_id(1);
        Patient updatedPatient = new Patient();
        updatedPatient.setFamily("test");

        when(patientsRepository.findById(any(Integer.class))).thenReturn(Optional.of(patient));
        //when(patientsRepository.findById(any(Integer.class))).thenReturn(Optional.of(patient));
        when(patientsRepository.save(patient)).thenReturn(patient);

        patientsService.updatePatient("1", updatedPatient);


        // Save the updated patient
        verify(patientsRepository, times(1)).findById(1);
        verify(patientsRepository, times(1)).save(patient);
        assertEquals("test", patient.getFamily());
    }
    /*public Iterable<Patient> addPatient(Patient newPatient){
        //Iterable<Patient> listOfPatients
        patientsRepository.save(newPatient);
        return patientsRepository.findAll();
    }*/
    @Test
    public void addPatientTest() throws Exception {
        Patient patient = new Patient();
        patient.setPatient_id(1);

        // Mockito.doNothing().when(repositoryNote).save(any(Note.class));
        when(patientsRepository.save(any(Patient.class))).thenReturn(patient);
        patientsService.addPatient(patient);
//doNothing().when(myList).add(isA(Integer.class),
        verify(patientsRepository, times(1)).save(patient);


    }
    @Test
    public void deletePatientTest(){


        Patient patient = new Patient();
        patient.setPatient_id(2);
        Mockito.doNothing().when(patientsRepository).deleteById(any(Integer.class));
        //when(repositoryNote.delete(note)).thenReturn(note);
        patientsService.deletePatient("2");
//doNothing().when(myList).add(isA(Integer.class),
        verify(patientsRepository, times(1)).deleteById(2);

    }
}
