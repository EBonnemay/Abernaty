package com.mediscreen.mspractitioners.controller;

import com.mediscreen.mspractitioners.Model.Note;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.mediscreen.mspractitioners.service.ServiceNote;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping(path = "/note")

public class ControllerNote {
    static final Logger logger = LogManager.getLogger();
    private ServiceNote serviceNote;
    public ControllerNote(ServiceNote serviceNote) {
        this.serviceNote = serviceNote;
    }
    /**api request : all notes for all patients__tested postman*/
    @GetMapping("/all")
    public ResponseEntity<List<Note>> retrieveAllNotes(){
        List<Note>allNotes = serviceNote.findAll();
        if(allNotes.iterator().hasNext()){
            return ResponseEntity.ok(allNotes);
        } else{
            return ResponseEntity.noContent().build();
        }
    }
    /**api request : retrieve one note by its id*/
    @GetMapping("/{id}")
    public ResponseEntity<Note> findNoteById(@PathVariable("id")String id){
        try {
            Note note = serviceNote.findNoteById(id);
            return ResponseEntity.ok(note);
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    /**api request : all notes for one patient__tested postman*/
    @GetMapping("/all/{patId}")
    public ResponseEntity<List<Note>>retrieveOnePatientsNotes(@PathVariable ("patId")String patId){
        List<Note> list = serviceNote.retrieveOnePatientsNotes(patId);
        //if(list.size()==0){
            //return ResponseEntity.notFound().build();
        //}else{
            return ResponseEntity.ok(list);
        }

        //return new ResponseEntity<>(list, HttpStatus.OK);

    /**api request : add note for one patient -- tested postman*/
    @PostMapping("/add/{patId}")
    public ResponseEntity<Note>addPatientNote(@PathVariable ("patId") String patId, @RequestParam ("contentNote") String contentNote){
        if(contentNote.length()==0){
            return ResponseEntity.badRequest().build();
        }
        try{
            Note newNote = serviceNote.addNote(patId, contentNote);
            logger.info("note added successfully");
            URI location = ServletUriComponentsBuilder.fromCurrentRequestUri().replacePath("/note/add/{patId}").buildAndExpand(newNote.getId()).toUri();
            logger.info("uri location is " + location.toString());
            //return ResponseEntity.created(location).build();
            return ResponseEntity.created(location).body(newNote);
            //return ResponseEntity.
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    /**api request : delete note for one patient - tested postman*/
    @GetMapping("/delete/{id}")
    public ResponseEntity<Void>deletePatientNote(@PathVariable("id") String id){

        if(!serviceNote.existsById(id)){
            return ResponseEntity.notFound().build();
        }else{
            serviceNote.deletePatientNote(id);
            logger.info("note deleted successfully");
            return ResponseEntity.noContent().build();
        }
    }
}
