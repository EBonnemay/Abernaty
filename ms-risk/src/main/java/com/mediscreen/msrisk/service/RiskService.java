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
        System.out.println("arrays as list is "+ listOfTriggers);
        for(String trigger  : listOfTriggers){
            trigger = processString(trigger);
            correctedListOfTriggers.add(trigger);
            System.out.println("here is element of listOfTrigger "+trigger);
        }

    }
    public List<String> correctListOfSymptoms(List<String> listOfTriggers){
        List<String>result = new ArrayList<>();
        for(String trigger : listOfTriggers){
            String newTrigger = processString(trigger);
            System.out.println("new spelling for symptom "+newTrigger);
            result.add(newTrigger);
        }
    System.out.println(result);
    return result;
    }
    public int getAge(LocalDate localDateOfBirth){
        //System.out.println("dateString is" + dateString);
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        //LocalDate localDateOfBirth = LocalDate.parse(dateString, formatter);
        System.out.println("localDate of birth is "+ localDateOfBirth);
        LocalDate today = LocalDate.now();
        return Period.between(localDateOfBirth, today).getYears();
    }
    public List getListOfRiskFactorsForOnePatient(List<String>listOfNotes){

        System.out.println("list of notes is this : "+listOfNotes);
        //create new list called result
        List<String> listOfRiskFactorsForOnePatient = new ArrayList<String>() ;
        listOfNotes.forEach(note -> {

            //
            String processedNote = processString(note);

            System.out.println("listOfTriggers before looping is "+ correctedListOfTriggers);
            System.out.println("processed Note is " + processedNote);
            for (String trigger:correctedListOfTriggers) {
                System.out.println("trigger in loop is "+trigger);
                if (processedNote.contains(trigger)) {
                    if(!listOfRiskFactorsForOnePatient.contains(trigger)) {
                        listOfRiskFactorsForOnePatient.add(trigger);
                    }
                }
            }
        });
        System.out.println("list of risk factors for this patient is "+listOfRiskFactorsForOnePatient);
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
        System.out.println("parameters are sex = "+ sex + " age is "+age+" listofriskfactorsis "+ listOfRiskFactorsForOnePatient);

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
            System.out.println("in danger matching condition");
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
        System.out.println("level of risk is "+result);
        return result;
    }
    public String processString(String string){
        //System.out.println("mot avant stem "+ word);
        //passe en minuscules
        //enleve les accents
        string = removeAccentsAndUpperCases(string);
        //si le mot a un point, point virgule, enlève-le
        string = removePhraseMarks(string);
        //word = stemWord(word);
        //System.out.println("mot après stem "+ word);
        //passe le mot au stemmer
        System.out.println(string);
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
   /* public String stemWord(String word){
        Analyzer analyzer = new FrenchAnalyzer();
        String stemmedWord = analyzer.normalize("dummy", word).utf8ToString();

        return stemmedWord;

    }*/


}
