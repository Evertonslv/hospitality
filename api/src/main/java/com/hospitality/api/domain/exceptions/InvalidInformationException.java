package com.hospitality.api.domain.exceptions;

public class InvalidInformationException extends RuntimeException {
    public InvalidInformationException(String message) {
        super(message);
    }
}
