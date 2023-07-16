package com.mediscreen.mspractitioners.Model;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.w3c.dom.Text;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
@Getter
@Setter
@Document(collection = "notesonpatients")
public class Note {
    @Id
    private String id ;

    private LocalDate date ;
    //String practitionerName;
    private String patId;

    private String contentNote;


    public Note(String patId, String contentNote) {
        this.patId = patId;
        this.contentNote = contentNote;
        this.date = LocalDate.now();

    }
}
