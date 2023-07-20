package com.mediscreen.msrisk.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;

@Service
public class RiskService {

    private ResourceLoader resourceLoader;
    private List<String> listOfTriggers;
    private List<String> correctedListOfTriggers = new ArrayList<>();
    public RiskService(ResourceLoader resourceLoader) throws IOException{
        this.resourceLoader = resourceLoader;
        Resource resource = resourceLoader.getResource("classpath:symptoms/symptoms.txt");
        byte[] bytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
        String symptomsContentMixedCases = new String(bytes, StandardCharsets.UTF_8);
        //String symptomsContent = symptomsContentMixedCases.toLowerCase();
        listOfTriggers =  Arrays.asList((symptomsContentMixedCases.split("\\R")));
        for(String trigger  : listOfTriggers){
            trigger = processString(trigger);
            correctedListOfTriggers.add(trigger);
        }

    }
    public List<String> correctListOfSymptoms(List<String> listOfTriggers){
        List<String>result = new ArrayList<>();
        for(String trigger : listOfTriggers){
            String newTrigger = processString(trigger);
            result.add(newTrigger);
        }
    return result;
    }
    public int getAge(LocalDate localDateOfBirth){
        LocalDate today = LocalDate.now();
        return Period.between(localDateOfBirth, today).getYears();
    }
    public List getListOfRiskFactorsForOnePatient(List<String>listOfNotes){

        List<String> listOfRiskFactorsForOnePatient = new ArrayList<String>() ;
        listOfNotes.forEach(note -> {

            //
            String processedNote = processString(note);

            for (String trigger:correctedListOfTriggers) {
                if (processedNote.contains(trigger)) {
                    if(!listOfRiskFactorsForOnePatient.contains(trigger)) {
                        listOfRiskFactorsForOnePatient.add(trigger);
                    }
                }
            }
        });
        return listOfRiskFactorsForOnePatient;
    }
    public String processComposedWordTrigger(String partOne, String partTwo, List words){

            int index = words.indexOf(partOne);
            if(words.indexOf(partOne)!=words.size()-1) {
                if (words.get(index + 1).equals(partTwo)) {
                    partOne = partOne+" "+partTwo;
                }

            }

        return partOne;
    }


    public String calculateRisk(String sex, int age, List listOfRiskFactorsForOnePatient){
        String result = "";
        if(listOfRiskFactorsForOnePatient.size()<2) {
             result =  "None";
        }
        if(age>=30&&listOfRiskFactorsForOnePatient.size()>=2&&listOfRiskFactorsForOnePatient.size()<6) {
             result = "Borderline";
        }
        if(sex.equals("F")&&age<30&&listOfRiskFactorsForOnePatient.size()>=2&&listOfRiskFactorsForOnePatient.size()<4){
             result ="None";
        }
        if(sex.equals("M")&&age<30&&listOfRiskFactorsForOnePatient.size()==2){
            result ="None";
        }
        if(sex.equals("M")&&age<30&&listOfRiskFactorsForOnePatient.size()>2&&listOfRiskFactorsForOnePatient.size()<5){
             result = "In danger";
        }
        if(sex.equals("F")&&age<30&&listOfRiskFactorsForOnePatient.size()>=4&&listOfRiskFactorsForOnePatient.size()<7) {
             result = "In danger";
        }
        if(age>=30&&listOfRiskFactorsForOnePatient.size()>=6&&listOfRiskFactorsForOnePatient.size()<8) {
             result = "In danger";
        }
        if(sex.equals("M")&&age<30&&listOfRiskFactorsForOnePatient.size()>=5) {
             result = "Early onset";
        }
        if(sex.equals("F")&&age<30&&listOfRiskFactorsForOnePatient.size()>=7) {
             result = "Early onset";
        }
        if(age>=30&&listOfRiskFactorsForOnePatient.size()>=8) {
             result = "Early onset";
        }
        return result;
    }
    public String processString(String string){
       string = removeAccentsAndUpperCases(string);
        string = removePhraseMarks(string);
        return string;
    }
    public String removePhraseMarks(String word){
        String wordWithoutPhraseMarks = word.replaceAll("[.,;?!:']+|\\R", " ");
        return wordWithoutPhraseMarks;

    }
    public String removeAccentsAndUpperCases(String word){
        word = word.toLowerCase();
        String cleanedWord = Normalizer.normalize(word, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}", "");
        return cleanedWord;
    }


}
