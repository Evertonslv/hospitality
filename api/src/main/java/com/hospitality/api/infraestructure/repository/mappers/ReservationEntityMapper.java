package com.hospitality.api.infraestructure.repository.mappers;

import com.hospitality.api.domain.entities.Guest;
import com.hospitality.api.domain.entities.Reservation;
import com.hospitality.api.infraestructure.persistence.GuestEntity;
import com.hospitality.api.infraestructure.persistence.ReservationEntity;

public class ReservationEntityMapper {
    public ReservationEntity toEntity(Reservation reservation, GuestEntity guestEntity) {
        return ReservationEntity.builder()
                .id(reservation.getId())
                .guest(guestEntity)
                .checkInDate(reservation.getCheckInDate())
                .checkOutDate(reservation.getCheckOutDate())
                .checkInTime(reservation.getCheckInTime())
                .checkOutTime(reservation.getCheckOutTime())
                .reservationDate(reservation.getReservationDate())
                .status(reservation.getStatus())
                .hasCar(reservation.isHasCar())
                .build();
    }

    public Reservation toReservation(ReservationEntity reservationEntity) {
        Guest guest = new Guest(
                reservationEntity.getGuest().getId(),
                reservationEntity.getGuest().getName(),
                reservationEntity.getGuest().getDocument(),
                reservationEntity.getGuest().getPhone());

        return new Reservation(
                reservationEntity.getId(),
                guest,
                reservationEntity.getCheckInDate(),
                reservationEntity.getCheckOutDate(),
                reservationEntity.getCheckInTime(),
                reservationEntity.getCheckOutTime(),
                reservationEntity.isHasCar(),
                reservationEntity.getReservationDate(),
                reservationEntity.getStatus());
    }
}
