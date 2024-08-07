package com.hospitality.api.domain.usecases;

import com.hospitality.api.domain.entities.Reservation;
import com.hospitality.api.domain.entities.ReservationStatus;
import com.hospitality.api.domain.usecases.dtos.ReservationResponse;
import com.hospitality.api.domain.usecases.gateways.ReservationGateway;
import com.hospitality.api.domain.usecases.mappers.ReservationDtoMapper;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class FindGuestsInHotelUseCase {
    private final ReservationGateway reservationGateway;
    private final ReservationDtoMapper reservationDtoMapper;

    public List<ReservationResponse> execute() {
        List<Reservation> reservations = reservationGateway.findAllByCheckedOut(ReservationStatus.CHECKED_IN);

        return reservations.stream()
                .map(reservationDtoMapper::toResponse)
                .collect(Collectors.toList());
    }
}
