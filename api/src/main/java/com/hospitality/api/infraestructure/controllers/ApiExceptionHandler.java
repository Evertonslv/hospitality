package com.hospitality.api.infraestructure.controllers;

import com.hospitality.api.domain.exceptions.GuestNotFoundException;
import com.hospitality.api.domain.exceptions.InvalidInformationException;
import com.hospitality.api.domain.exceptions.MissingInformationException;
import com.hospitality.api.domain.exceptions.ReservationNotFoundException;
import com.hospitality.api.infraestructure.response.HospitalityResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(GuestNotFoundException.class)
    public HospitalityResponse<String> guestNotFoundException(GuestNotFoundException ex) {
        return new HospitalityResponse<>(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(ReservationNotFoundException.class)
    public HospitalityResponse<String> reservationNotFoundException(ReservationNotFoundException ex) {
        return new HospitalityResponse<>(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(InvalidInformationException.class)
    public HospitalityResponse<String> invalidInformationException(InvalidInformationException ex) {
        return new HospitalityResponse<>(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(MissingInformationException.class)
    public HospitalityResponse<String> missingInformationException(MissingInformationException ex) {
        return new HospitalityResponse<>(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

}
