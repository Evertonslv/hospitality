package com.hospitality.api.domain.usecases.gateways;

import com.hospitality.api.domain.entities.Reservation;
import com.hospitality.api.domain.entities.ReservationStatus;

import java.util.List;

public interface ReservationGateway {
    Reservation save(Reservation reservation);

    Reservation findById(Long reservationId);

    List<Reservation> findAllByCheckedOut(ReservationStatus status);
}