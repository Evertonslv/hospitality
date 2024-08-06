package com.hospitality.api.infraestructure.repository.postgres;

import com.hospitality.api.domain.entities.Reservation;
import com.hospitality.api.domain.entities.ReservationStatus;
import com.hospitality.api.domain.usecases.gateways.ReservationGateway;
import com.hospitality.api.infraestructure.persistence.GuestEntity;
import com.hospitality.api.infraestructure.persistence.ReservationEntity;
import com.hospitality.api.infraestructure.persistence.ReservationRepository;
import com.hospitality.api.infraestructure.repository.mappers.GuestEntityMapper;
import com.hospitality.api.infraestructure.repository.mappers.ReservationEntityMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class ReservationPostgresRepository implements ReservationGateway {

    private final ReservationRepository reservationRepository;
    private final ReservationEntityMapper reservationEntityMapper;
    private final GuestEntityMapper guestEntityMapper;

    @Override
    public Reservation save(Reservation reservation) {
        GuestEntity guestEntity = this.guestEntityMapper.toEntity(reservation.getGuest());
        ReservationEntity reservationEntity = this.reservationEntityMapper.toEntity(reservation, guestEntity);
        ReservationEntity reservationEntitySave = this.reservationRepository.save(reservationEntity);
        return this.reservationEntityMapper.toReservation(reservationEntitySave);
    }

    @Override
    public Reservation findById(Long reservationId) {
        Optional<ReservationEntity> reservationEntity = this.reservationRepository.findById(reservationId);
        return reservationEntity.map(this.reservationEntityMapper::toReservation).orElse(null);
    }

    @Override
    public List<Reservation> findAllByCheckedOut(ReservationStatus status) {
        Optional<List<ReservationEntity>> reservationsEntity = this.reservationRepository.findAllByStatus(status);
        return reservationsEntity
                .map(entities -> entities.stream()
                        .map(reservationEntityMapper::toReservation)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }
}
