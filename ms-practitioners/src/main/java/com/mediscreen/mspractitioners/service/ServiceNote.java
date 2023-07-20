package com.mediscreen.mspractitioners.service;

import com.mediscreen.mspractitioners.Model.Note;
import com.mediscreen.mspractitioners.repository.RepositoryNote;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceNote {
    RepositoryNote repositoryNote;
    public ServiceNote(RepositoryNote repositoryNote){
        this.repositoryNote = repositoryNote;
    }
    public Note addNote(String patId, String contentNote){
        Note note = new Note(patId, contentNote);
        repositoryNote.save(note);
        return note;
    }
    public List<Note> retrieveOnePatientsNotes(String patId)  {
        List<Note> notesForThisPatient =  repositoryNote.findByPatId(patId);
        return notesForThisPatient;


    }
    public List<Note> findAll(){
        return repositoryNote.findAll();
    }
    public Note findNoteById(String id){
        System.out.println("id of this note in service is "+ id);
        Optional<Note>optNote = repositoryNote.findById(id);
        System.out.println("opt note in service findNoteById is "+ optNote);
        return optNote.get();
    }
    public Boolean existsById(String id){
        return repositoryNote.existsById(id);
    }
    public void deletePatientNote(String id){
        //Optional<Note> optNote = repositoryNote.findById(id);
        //Note note = optNote.get();
        repositoryNote.deleteById(id);
    }

}
