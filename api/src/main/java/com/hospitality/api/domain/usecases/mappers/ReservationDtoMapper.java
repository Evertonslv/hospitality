package com.hospitality.api.domain.usecases.mappers;

import com.hospitality.api.domain.entities.Reservation;
import com.hospitality.api.domain.usecases.dtos.ChargeDetail;
import com.hospitality.api.domain.usecases.dtos.CheckOutResponse;
import com.hospitality.api.domain.usecases.dtos.ReservationResponse;

import java.util.List;

public class ReservationDtoMapper {

    public ReservationResponse toResponse(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getGuest(),
                reservation.getCheckInDate(),
                reservation.getCheckOutDate(),
                reservation.getCheckInTime(),
                reservation.getCheckOutTime(),
                reservation.isHasCar(),
                reservation.getReservationDate(),
                reservation.getStatus()
        );
    }

    public CheckOutResponse toCheckOutResponse(Reservation reservation, List<ChargeDetail> totalAmount) {
        return new CheckOutResponse(
                reservation.getId(),
                reservation.getGuest(),
                totalAmount,
                reservation.getCheckInDate(),
                reservation.getCheckOutDate(),
                reservation.getCheckInTime(),
                reservation.getCheckOutTime(),
                reservation.isHasCar(),
                reservation.getReservationDate(),
                reservation.getStatus()
        );
    }
}
