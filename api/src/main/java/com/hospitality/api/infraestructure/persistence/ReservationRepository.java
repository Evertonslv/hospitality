package com.hospitality.api.infraestructure.persistence;

import com.hospitality.api.domain.entities.ReservationStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends CrudRepository<ReservationEntity, Long> {

    Optional<List<ReservationEntity>> findAllByStatus(ReservationStatus status);
}
