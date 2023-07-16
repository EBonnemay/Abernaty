package com.mediscreen.mspatients.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.NoSuchElementException;
@ResponseStatus(HttpStatus.NOT_FOUND)
public class PatientNotFoundException extends NoSuchElementException {
    String message;
    public PatientNotFoundException(String message) {
        this.message = message;
    }
}
