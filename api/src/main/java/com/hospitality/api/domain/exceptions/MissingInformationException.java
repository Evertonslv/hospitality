package com.hospitality.api.domain.exceptions;

public class MissingInformationException extends RuntimeException {
    public MissingInformationException(String message) {
        super(message);
    }
}
