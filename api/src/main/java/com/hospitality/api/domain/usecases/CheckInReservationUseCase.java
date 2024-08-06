package com.hospitality.api.domain.usecases;

import com.hospitality.api.domain.entities.Reservation;
import com.hospitality.api.domain.exceptions.ReservationNotFoundException;
import com.hospitality.api.domain.usecases.dtos.ReservationCheckInOutRequest;
import com.hospitality.api.domain.usecases.dtos.ReservationResponse;
import com.hospitality.api.domain.usecases.gateways.ReservationGateway;
import com.hospitality.api.domain.usecases.mappers.ReservationDtoMapper;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CheckInReservationUseCase {
    private final ReservationGateway reservationGateway;
    private final ReservationDtoMapper reservationDtoMapper;

    public ReservationResponse execute(ReservationCheckInOutRequest reservationCheckInOutRequest) {
        Reservation reservation = reservationGateway.findById(reservationCheckInOutRequest.reservationId());

        if (reservation == null) {
            throw new ReservationNotFoundException();
        }

        reservation.checkIn(reservationCheckInOutRequest.checkInOutDateTime());
        reservationGateway.save(reservation);

        return reservationDtoMapper.toResponse(reservation);
    }
}
