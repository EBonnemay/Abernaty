package com.mediscreen.mspatients.service;

import com.mediscreen.mspatients.model.Patient;
import org.springframework.stereotype.Service;
import com.mediscreen.mspatients.repository.PatientsRepository;
import org.springframework.web.bind.annotation.RequestParam;


import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class PatientsService {

    private PatientsRepository patientsRepository;
    public PatientsService(PatientsRepository patientsRepository){
        this.patientsRepository = patientsRepository;

    }
    public boolean existsById(Integer id){
        return patientsRepository.existsById(id);
    }

    public Iterable<Patient> getPatients(){
        return patientsRepository.findAll();
    }
    public Patient getPatientById(int id){
       Optional< Patient > opt = patientsRepository.findById(id);
       Patient patient = opt.get();
       return patient;
    }

    public Patient getPatientByFullName(String family, String given){
        Optional <Patient> opt = patientsRepository.findByFullName(family, given);
        return opt.get();
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
    public Patient updatePatient(String id, Patient updatedPatient){
        Optional<Patient> optPatient = patientsRepository.findById(Integer.parseInt(id));
        Patient existingPatient = optPatient.get();

        existingPatient.setFamily(updatedPatient.getFamily());
        existingPatient.setGiven(updatedPatient.getGiven());
        existingPatient.setDate_of_birth(updatedPatient.getDate_of_birth());
        existingPatient.setSex(updatedPatient.getSex());
        existingPatient.setAddress(updatedPatient.getAddress());
        existingPatient.setPhone(updatedPatient.getPhone());

        // Save the updated patient
        patientsRepository.save(existingPatient);
        return existingPatient;
    }
    /*public Iterable<Patient> addPatient(Patient newPatient){
        //Iterable<Patient> listOfPatients
        patientsRepository.save(newPatient);
        return patientsRepository.findAll();
    }*/
    public Patient addPatient(Patient patient) throws Exception {

            Optional<Patient> opt = patientsRepository.findByFullName(patient.getFamily(), patient.getGiven());
            try{
                opt.get();//si ce patient existe déjà
                throw new Exception();
            }catch(NoSuchElementException noSuchElementException){
                return patientsRepository.save(patient);
            }







    }
    public void deletePatient(String i){
        Integer idInteger = Integer.parseInt(i);
        patientsRepository.deleteById(idInteger);
    }



}
