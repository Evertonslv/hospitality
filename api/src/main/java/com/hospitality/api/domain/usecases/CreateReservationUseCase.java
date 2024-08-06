package com.hospitality.api.domain.usecases;

import com.hospitality.api.domain.entities.Guest;
import com.hospitality.api.domain.entities.Reservation;
import com.hospitality.api.domain.entities.ReservationStatus;
import com.hospitality.api.domain.exceptions.GuestNotFoundException;
import com.hospitality.api.domain.exceptions.InvalidInformationException;
import com.hospitality.api.domain.usecases.dtos.CreateReservationRequest;
import com.hospitality.api.domain.usecases.dtos.ReservationResponse;
import com.hospitality.api.domain.usecases.gateways.GuestGateway;
import com.hospitality.api.domain.usecases.gateways.ReservationGateway;
import com.hospitality.api.domain.usecases.mappers.ReservationDtoMapper;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
public class CreateReservationUseCase {
    private final GuestGateway guestGateway;
    private final ReservationGateway reservationGateway;
    private final ReservationDtoMapper reservationDtoMapper;

    public ReservationResponse execute(CreateReservationRequest request) {
        validateDates(request.checkInDate(), request.checkOutDate());

        Guest guest = guestGateway.findById(request.guestId());

        if (guest == null) throw new GuestNotFoundException();

        Reservation reservation = new Reservation(
                guest,
                request.checkInDate(),
                request.checkOutDate(),
                request.hasCar(),
                LocalDateTime.now(),
                ReservationStatus.PENDING
        );

        Reservation reservationSave = reservationGateway.save(reservation);
        return reservationDtoMapper.toResponse(reservationSave);
    }

    private void validateDates(LocalDate checkInDate, LocalDate checkOutDate) {
        if (checkInDate.isAfter(checkOutDate)) {
            throw new InvalidInformationException("Check-out date must be after check-in date");
        }
        if (checkInDate.isBefore(LocalDate.now())) {
            throw new InvalidInformationException("Check-in date cannot be in the past");
        }
    }
}
