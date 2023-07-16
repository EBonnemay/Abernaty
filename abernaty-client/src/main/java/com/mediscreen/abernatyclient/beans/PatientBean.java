package com.mediscreen.abernatyclient.beans;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class PatientBean {
        private int patient_id;


        private String family;


        private String given;
        private LocalDate date_of_birth;

        private String sex;

        private String address;

        private String phone;

        public PatientBean(String family, String given){
            this.family = family;
            this.given = given;
        }
        public PatientBean(String family, String given, LocalDate dob, String sex, String address, String phone){
            this.family = family;
            this.given = given;
            this.date_of_birth = dob;
            this.sex = sex;
            this.address = address;
            this.phone = phone;

        }

    }


