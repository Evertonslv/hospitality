package com.hospitality.api.domain.usecases.dtos;

import java.time.LocalDate;

public record CreateReservationRequest(Long guestId, LocalDate checkInDate, LocalDate checkOutDate, boolean hasCar) {
}
