package com.hospitality.api.domain.usecases.dtos;

import com.hospitality.api.domain.entities.Guest;
import com.hospitality.api.domain.entities.ReservationStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ReservationResponse(
        Long id,
        Guest guest,
        LocalDate checkInDate,
        LocalDate checkOutDate,
        LocalDateTime checkInTime,
        LocalDateTime checkOutTime,
        boolean hasCar,
        LocalDateTime reservationDate,
        ReservationStatus status) {
}
