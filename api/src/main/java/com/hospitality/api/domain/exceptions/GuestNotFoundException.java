package com.hospitality.api.domain.exceptions;

public class GuestNotFoundException extends RuntimeException {
    public GuestNotFoundException() {
        super("Guest not found");
    }
}
