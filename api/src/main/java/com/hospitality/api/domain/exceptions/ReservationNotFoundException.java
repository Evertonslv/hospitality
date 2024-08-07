package com.hospitality.api.domain.exceptions;

public class ReservationNotFoundException extends RuntimeException {
    public ReservationNotFoundException() {
        super("Reserva não encontrada");
    }
}
