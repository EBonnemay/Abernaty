package com.mediscreen.mspractitioners.noteservicetest;

import com.mediscreen.mspractitioners.Model.Note;
import com.mediscreen.mspractitioners.repository.RepositoryNote;
import com.mediscreen.mspractitioners.service.ServiceNote;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource("/application-test.properties")
public class ServiceNoteTest {
    @Mock
    private RepositoryNote repositoryNote;

    private ServiceNote serviceNote;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        serviceNote = new ServiceNote(repositoryNote);

    }
    @Test
    public void addNoteTest(){
        Note note = new Note("1", "new note from practitionner");

       // Mockito.doNothing().when(repositoryNote).save(any(Note.class));
        when(repositoryNote.save(note)).thenReturn(note);
        serviceNote.addNote(note.getPatId(), note.getContentNote());
//doNothing().when(myList).add(isA(Integer.class),
        verify(repositoryNote, times(1)).save(any(Note.class));

    }
    @Test
    public void retrieveOnePatientsNotesTest(){
        List<Note> notes = new ArrayList<>();
        when(repositoryNote.findByPatId("1")).thenReturn(notes);
        serviceNote.retrieveOnePatientsNotes("1");
        verify(repositoryNote, times(1)).findByPatId("1");


    }
    @Test
    public void findAllTest(){
        List<Note> notes = new ArrayList<>();
        when(repositoryNote.findAll()).thenReturn(notes);
        List<Note> notes2 = serviceNote.findAll();

        assertTrue(notes==notes2);
    }
    @Test
    public void findNoteByIdTest(){
        Note note = new Note("2", "new note from my doctor");
        note.setId("1");
        when(repositoryNote.findById("1")).thenReturn(Optional.of(note));
        serviceNote.findNoteById("1");
        verify(repositoryNote, times(1)).findById("1");
    }
    @Test
    public void deletePatientNoteTest(){

        Note note = new Note("1", "new note from practitionner");
        note.setId("2");
        Mockito.doNothing().when(repositoryNote).deleteById("2");
        //when(repositoryNote.delete(note)).thenReturn(note);
        serviceNote.deletePatientNote("2");
//doNothing().when(myList).add(isA(Integer.class),
        verify(repositoryNote, times(1)).deleteById("2");


    }

}