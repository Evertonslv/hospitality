package com.hospitality.api.domain.usecases.dtos;

import java.time.LocalDateTime;

public record ReservationCheckInOutRequest(Long reservationId, LocalDateTime checkInOutDateTime) {
}
