package com.hospitality.api.domain.usecases;

import com.hospitality.api.domain.entities.Reservation;
import com.hospitality.api.domain.exceptions.ReservationNotFoundException;
import com.hospitality.api.domain.usecases.dtos.ChargeDetail;
import com.hospitality.api.domain.usecases.dtos.CheckOutResponse;
import com.hospitality.api.domain.usecases.dtos.ReservationCheckInOutRequest;
import com.hospitality.api.domain.usecases.dtos.ReservationResponse;
import com.hospitality.api.domain.usecases.gateways.ReservationGateway;
import com.hospitality.api.domain.usecases.mappers.ReservationDtoMapper;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class CheckOutReservationUseCase {
    private final ReservationGateway reservationGateway;
    private final ReservationDtoMapper reservationDtoMapper;

    public CheckOutResponse execute(ReservationCheckInOutRequest reservationCheckInOutRequest) {
        Reservation reservation = reservationGateway.findById(reservationCheckInOutRequest.reservationId());

        if (reservation == null) {
            throw new ReservationNotFoundException();
        }

        reservation.checkOut(reservationCheckInOutRequest.checkInOutDateTime());
        reservationGateway.save(reservation);

        List<ChargeDetail> totalAmount = reservation.calculateTotalAmount();

        return reservationDtoMapper.toCheckOutResponse(reservation, totalAmount);
    }
}
