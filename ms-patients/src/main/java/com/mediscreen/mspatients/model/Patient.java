package com.mediscreen.mspatients.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "table_patients")

public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int patient_id;

    @NotBlank(message = "Family is mandatory")
    @Column(name = "family")
    private String family;

    @NotBlank(message = "Given is mandatory")
    @Column(name = "given")
    private String given;
    @NotNull(message = "Date of Birth is mandatory")
    @Column(name = "date_of_birth")
    private LocalDate date_of_birth;

    @NotBlank(message = "Sex is mandatory")
    @Column(name = "sex")
    private String sex;

   @Column(name = "address")
    private String address;

   @Column(name = "phone")
    private String phone;

   public Patient(String family, String given,LocalDate dob, String sex ){
       this.family = family;
       this.given = given;
       this.date_of_birth = dob;
       this.sex = sex;
   }
   public Patient(String family, String given, LocalDate dob, String sex, String address, String phone){
       this.family = family;
       this.given = given;
       this.date_of_birth = dob;
       this.sex = sex;
       this.address = address;
       this.phone = phone;

   }

}
