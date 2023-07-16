package com.mediscreen.mspractitioners.notecontrollertest;

import com.mediscreen.mspractitioners.Model.Note;
import com.mediscreen.mspractitioners.controller.ControllerNote;
import com.mediscreen.mspractitioners.service.ServiceNote;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource("/application-test.properties")
public class ControllerNoteTest {
    @Mock
    ServiceNote serviceNote;
    ControllerNote controllerNote;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        controllerNote = new ControllerNote(serviceNote);

    }
    @Test
    public void retrieveAllNotesTest(){
        List<Note>listOfNotes = new ArrayList<>();

        when(serviceNote.findAll()).thenReturn(listOfNotes);
        ResponseEntity response = controllerNote.retrieveAllNotes();
        verify(serviceNote, times(1)).findAll();
    }

    @Test
    public void findNoteByIdTest(){
        Note note1 = new Note("1", "new note");
        when(serviceNote.findNoteById("1")).thenReturn(note1);
        //Note note2 = controllerNote.findNoteById("1");
        verify(serviceNote, times(1)).findNoteById("1");

    }


    @Test
    public  void retrieveOnePatientsNotesTest() {
        List<Note> listOfNotes = new ArrayList<>();
        when(serviceNote.retrieveOnePatientsNotes("1")).thenReturn(listOfNotes);
        ResponseEntity response = controllerNote.retrieveOnePatientsNotes("1");

        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());
        verify(serviceNote, times(1)).retrieveOnePatientsNotes("1");
        //HttpStatusCode newStatus = new
        }
        //assertEquals("200 OK", response.getStatusCode().getM);



    @Test
    public void addPatientNoteTest() {
        Note note = new Note("1", "new note");

        // Mockito.doNothing().when(repositoryNote).save(any(Note.class));
        when(serviceNote.addNote("1", "new note")).thenReturn(note);
        ResponseEntity response = controllerNote.addPatientNote(note.getPatId(), note.getContentNote());
//doNothing().when(myList).add(isA(Integer.class),
        verify(serviceNote, times(1)).addNote("1", "new note");
    }
    @Test
    public void deletePatientNoteTest() {
        Note note = new Note("1", "new note");

        // Mockito.doNothing().when(repositoryNote).save(any(Note.class));
        Mockito.doNothing().when(serviceNote).deletePatientNote("1");
        ResponseEntity response = controllerNote.deletePatientNote("1");
//doNothing().when(myList).add(isA(Integer.class),
        verify(serviceNote, times(1)).deletePatientNote("1");

    }


    }
