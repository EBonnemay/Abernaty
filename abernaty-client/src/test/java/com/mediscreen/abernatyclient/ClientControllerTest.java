package com.mediscreen.abernatyclient;

import com.mediscreen.abernatyclient.beans.NoteBean;
import com.mediscreen.abernatyclient.beans.PatientBean;
import com.mediscreen.abernatyclient.beans.RiskFactorDtoBean;
import com.mediscreen.abernatyclient.controller.ClientController;
import com.mediscreen.abernatyclient.proxies.MicroservicePatientsProxy;
import com.mediscreen.abernatyclient.proxies.MicroservicePractitionersProxy;
import com.mediscreen.abernatyclient.proxies.MicroserviceRiskProxy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource("/application-test.properties")
public class ClientControllerTest {
    @Mock
    private MicroservicePatientsProxy patientsProxy;
    @Mock
    private MicroservicePractitionersProxy practitionersProxy;
    @Mock
    private MicroserviceRiskProxy riskProxy;
    @Mock
    private BindingResult bindingResult;

    ClientController clientController;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        clientController = new ClientController(patientsProxy, practitionersProxy, riskProxy);
    }

    @Test
    public void displayPatientsTest(){
        List<PatientBean> patients = new ArrayList<>();
        Model model = new ConcurrentModel();
        when(patientsProxy.getPatients()).thenReturn(patients);
        clientController.displayPatients(model);
        verify(patientsProxy, times(1)).getPatients();
        assertTrue(model.containsAttribute("patients"));
    }

    @Test
    public void displayUpdateFormTest(){
        PatientBean patient = new PatientBean();
        Model model = new ConcurrentModel();

        when(patientsProxy.getPatientById(1)).thenReturn(patient);
        clientController.displayUpdateForm(null, "1", model);
        verify(patientsProxy, times(1)).getPatientById(1);
        assertTrue(model.containsAttribute("patient"));
        assertFalse(model.containsAttribute("error"));

    }
    @Test
    public void displayUpdateFormIfErrorTest(){
        PatientBean patient = new PatientBean();
        Model model = new ConcurrentModel();
        String error = "error";
        when(patientsProxy.getPatientById(1)).thenReturn(patient);
        clientController.displayUpdateForm("error", "1", model);
        verify(patientsProxy, times(1)).getPatientById(1);
        assertTrue(model.containsAttribute("patient"));
        assertTrue(model.containsAttribute("error"));

    }
    @Test
    public void updatePatientTest(){
        PatientBean patient = new PatientBean();
        patient.setPatient_id(1);

        when(bindingResult.hasErrors()).thenReturn(false);
        when(patientsProxy.updatePatient("1", patient)).thenReturn(patient);
        clientController.updatePatient("1", patient,  bindingResult);
        verify(patientsProxy, times(1)).updatePatient("1", patient);
        assertEquals ("redirect:/patient/all", clientController.updatePatient("1", patient,  bindingResult));

    }
    @Test
    public void addPatientTest(){
        PatientBean patient = new PatientBean();
        patient.setPatient_id(1);

        when(bindingResult.hasErrors()).thenReturn(false);
        when(patientsProxy.addPatient(patient)).thenReturn(patient);
        clientController.addPatient(patient,  bindingResult);
        verify(patientsProxy, times(1)).addPatient(patient);
        assertEquals ("redirect:/patient/all", clientController.addPatient(patient,  bindingResult));

    }
    @Test
    public void deletePatientTest(){
        PatientBean patient = new PatientBean();
        patient.setPatient_id(1);
        BindingResult bindingResult = null;
        Mockito.doNothing().when(patientsProxy).deletePatient("1");
        clientController.deletePatient("1");
        verify(patientsProxy, times(1)).deletePatient("1");
        assertEquals ("redirect:/patient/all", clientController.deletePatient("1"));
    }

    @Test
    public void displayNotesPageTest(){
        PatientBean patient = new PatientBean();
        patient.setSex("F");
        LocalDate localDate = LocalDate.of(1977, 10, 20);
        patient.setDate_of_birth(localDate);
        NoteBean note1 = new NoteBean("1", "first note");
        NoteBean note2 = new NoteBean("1", "second note");
        List<NoteBean> listOfOnePatientsNotes = new ArrayList<NoteBean>();
        listOfOnePatientsNotes.add(note1);
        listOfOnePatientsNotes.add(note2);

        List<String> listOfOnePatientsMessages = new ArrayList<>();
        listOfOnePatientsMessages.add(note1.getContentNote());
        listOfOnePatientsMessages.add(note2.getContentNote());


        RiskFactorDtoBean riskFactorDtoBean = new RiskFactorDtoBean("F",localDate, listOfOnePatientsMessages );
        String risk = "Borderline";
        Model model = new ConcurrentModel();
        when(practitionersProxy.retrieveOnePatientsNotes("1")).thenReturn(listOfOnePatientsNotes);
        when(patientsProxy.getPatientById(1)).thenReturn(patient);
        when(riskProxy.calculateRiskFactors(any(RiskFactorDtoBean.class))).thenReturn(riskFactorDtoBean);



        clientController.displayNotesPage("1", model);

        verify(practitionersProxy, times(1)).retrieveOnePatientsNotes("1");
        verify(patientsProxy, times(1)).getPatientById(1);
        verify(riskProxy, times(1)).calculateRiskFactors(any(RiskFactorDtoBean.class));

        assertTrue(model.containsAttribute("age"));
        assertTrue(model.containsAttribute("listOfNotes"));
        assertTrue(model.containsAttribute("patient"));
        assertTrue(model.containsAttribute("riskFactorDtoBean1"));

    }
    @Test
    public void displayAddNoteFormTest(){
        Model model = new ConcurrentModel();
        PatientBean patient = new PatientBean();
        when(patientsProxy.getPatientById(1)).thenReturn(patient);
        clientController.displayAddNoteForm(null, "1", model);
        verify(patientsProxy, times(1)).getPatientById(1);
        assertTrue(model.containsAttribute("patient"));
        assertFalse(model.containsAttribute("error"));


    }
    @Test
    public void displayUpdateNoteFormWithErrorsTest(){
        Model model = new ConcurrentModel();
        NoteBean note = new NoteBean("1", "new note");
        PatientBean patient = new PatientBean();
        when(practitionersProxy.findNoteById("1")).thenReturn(note);
        when(patientsProxy.getPatientById(1)).thenReturn(patient);
        clientController.displayAddNoteForm("error", "1", model);

        assertTrue(model.containsAttribute("error"));
        //assertTrue(model.containsAttribute("note")); POURQUOI??
        assertTrue(model.containsAttribute("patient"));

    }

    @Test
    public void addNoteTest(){
        NoteBean note = new NoteBean("1", "new content note");
        Mockito.doNothing().when(practitionersProxy).addNote("1", "new content note");

        //clientController.addNote("1", "new content note");
        assertEquals("redirect:/note/all/1", clientController.addNote("1", "new content note"));
    }
    @Test
    public void deleteNoteTest(){
        NoteBean note = new NoteBean("1", "new content note");
        note.setNoteId("3");
        when(practitionersProxy.findNoteById("3")).thenReturn(note);
        Mockito.doNothing().when(practitionersProxy).deleteNote("3");

        //clientController.addNote("1", "new content note");
        assertEquals("redirect:/note/all/1", clientController.deleteNote("3"));
       // clientController.deleteNote("1")
    }



    @Test
    public void displayPatientRequestFormWithErrorsTest(){
        Model model = new ConcurrentModel();
        clientController.displayPatientRequestForm("error", model);
        assertTrue(model.containsAttribute("error"));
        assertEquals("HomePage", clientController.displayPatientRequestForm("error", model));



    }
    @Test
    public void displayAddForm(){
        Model model = new ConcurrentModel();
        clientController.displayAddForm("error", model);
        assertTrue(model.containsAttribute("error"));
        assertEquals("AddPatient", clientController.displayAddForm("error", model));

    }

}
