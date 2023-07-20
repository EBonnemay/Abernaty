package com.mediscreen.abernatyclient.controller;

import com.google.common.annotations.Beta;
import com.mediscreen.abernatyclient.beans.NoteBean;
import com.mediscreen.abernatyclient.beans.PatientBean;
import com.mediscreen.abernatyclient.beans.RiskFactorDtoBean;
import com.mediscreen.abernatyclient.proxies.MicroservicePatientsProxy;
import com.mediscreen.abernatyclient.proxies.MicroservicePractitionersProxy;
import com.mediscreen.abernatyclient.proxies.MicroserviceRiskProxy;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ClientController {
    private final MicroservicePatientsProxy patientsProxy;
    private final MicroservicePractitionersProxy practitionersProxy;
    private final MicroserviceRiskProxy riskProxy;
    public ClientController(MicroservicePatientsProxy patientsProxy, MicroservicePractitionersProxy practitionersProxy, MicroserviceRiskProxy riskProxy){
        this.patientsProxy = patientsProxy;
        this.practitionersProxy = practitionersProxy;
        this.riskProxy = riskProxy;
    }

/**display HomePage/'get patient' form, including redirection case*/
    @RequestMapping("/")
    public String displayPatientRequestForm(@RequestParam (name = "error", required = false)String error, Model model){

        if (error != null) {
            model.addAttribute("error", true);
        }
        return "HomePage";
    }
    /**post method to get patient file, handling success or fail*/
    @GetMapping("/patient")
    public String showOnePatientsPageFromFamilyAndGiven(@RequestParam ("family")String family, @RequestParam ("given")String given){
        int patId;
        try {
            patId = patientsProxy.getPatIdFromFamilyAndGiven(family, given);
           // model.addAttribute("error", false);

        }
        catch(Exception e){
            //model.addAttribute("error");
            return "redirect:/?error=true";
        }
        return String.format("redirect:/note/all/%s",patId);
    }


        /**display list of patients on Patients page*/
    @GetMapping("/patient/all")
    public String displayPatients(Model model){
        List<PatientBean> patients = patientsProxy.getPatients();
        model.addAttribute("patients", patients);
        return "Patients";
    }
    /**display 'update' form on UpdatePatient page*/
    @GetMapping("/patient/update/{id}")
    public String displayUpdateForm(@RequestParam (name = "error", required = false)String error, @PathVariable("id")String id, Model model){
        PatientBean patient = patientsProxy.getPatientById(Integer.parseInt(id));
        if (error != null) {
            model.addAttribute("error", true);
        }
        model.addAttribute("patient", patient);
        return "UpdatePatient";
    }
    /**display the 'add patient' form on AddPatient page*/
    @GetMapping("/patient/add")
    public String displayAddForm(@RequestParam (name = "error", required = false)String error, Model model){
        if (error != null) {
            model.addAttribute("error", true);
        }
        return "AddPatient";
    }




    /**update a patient and return Patients*/
   @PostMapping("/patient/update/{id}")
    public String updatePatient(@PathVariable("id") String id, @Valid @ModelAttribute ("patient") PatientBean patient, BindingResult bindingResult){
        try {
            patientsProxy.updatePatient(id, patient);
        }
        catch(Exception e){
            return String.format("redirect:/patient/update/%s?error=true", id);
        }
        return "redirect:/patient/all";
    }
    /**add a patient and return Patients*/
    @PostMapping("/patient/add")
    public String addPatient(@Valid @ModelAttribute ("patient")PatientBean patient, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return String.format("redirect:/patient/add?error=true");
        }
        try{
            patientsProxy.addPatient(patient);
        }catch(Exception e){
                return String.format("redirect:/patient/add?error=true");
        }

        return "redirect:/patient/all";
    }
    /**delete a patient and return Patients*/
   @GetMapping("/patient/delete/{id}")
    public String deletePatient(@PathVariable ("id")String id){
        patientsProxy.deletePatient(id);
        return "redirect:/patient/all";
    }

    /**display the list of notes on one patient on NotesPage page__*/
   @GetMapping("/note/all/{patId}")
    public String displayNotesPage(@PathVariable("patId")String patId, Model model){
        //PatientBean patient = patientsProxy.retrievePatient(Integer.parseInt(id));
        List<NoteBean> listOfOnePatientsNotes = practitionersProxy.retrieveOnePatientsNotes(patId);

        PatientBean patient = patientsProxy.getPatientById(Integer.parseInt(patId));
       LocalDate localDateOfBirth = patient.getDate_of_birth();
       LocalDate today = LocalDate.now();
        int age = Period.between(localDateOfBirth, today).getYears();

        List<String>listOfOnePatientMessages = new ArrayList<>();

        for (NoteBean note : listOfOnePatientsNotes) {
            listOfOnePatientMessages.add(note.getContentNote());
        }

        LocalDate dateOfBirth = patient.getDate_of_birth();
       RiskFactorDtoBean riskFactorDtoBean = new RiskFactorDtoBean(patient.getSex(), patient.getDate_of_birth(), listOfOnePatientMessages);


       RiskFactorDtoBean riskFactorDtoBean1 = riskProxy.calculateRiskFactors(riskFactorDtoBean);
       model.addAttribute("age", age);
       model.addAttribute("listOfNotes", listOfOnePatientsNotes);
        model.addAttribute("patient", patient);
        model.addAttribute("riskFactorDtoBean1", riskFactorDtoBean1);




        return "NotesPage";
    }

    /**display 'add note' form on AddNote page_calling patientsProxy, not practitioners' proxy__*/
   @GetMapping("/note/add/{id}")
   public String displayAddNoteForm(@RequestParam (name = "error", required = false)String error, @PathVariable("id")String id, Model model){
        PatientBean patient = patientsProxy.getPatientById(Integer.parseInt(id));
       if (error != null) {
           model.addAttribute("error", true);
       }
        model.addAttribute("patient", patient);
        return "AddNote";
    }


    /**add a note and return notesPage with patient's id as param__*/
    @PostMapping("/note/add/{patId}")
    public String addNote(@PathVariable("patId")String patId, @RequestParam String contentNote){
        try {
            practitionersProxy.addNote(patId, contentNote);
        } catch(Exception e){
            return String.format("redirect:/note/add/%s?error=true", patId);
            }
        return String.format("redirect:/note/all/%s",patId);
    }
    /**delete a note and return notesPage__*/
   @GetMapping("/note/delete/{id}")
    public String deleteNote(@PathVariable("id")String id){
       NoteBean note = practitionersProxy.findNoteById(id);
       String patId = note.getPatId();
       practitionersProxy.deleteNote(id);
       return String.format("redirect:/note/all/%s",patId);
    }
}
