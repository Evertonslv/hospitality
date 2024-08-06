package com.hospitality.api.infraestructure.repository.postgres;

import com.hospitality.api.domain.entities.Guest;
import com.hospitality.api.domain.usecases.dtos.GuestFilterRequest;
import com.hospitality.api.domain.usecases.gateways.GuestGateway;
import com.hospitality.api.infraestructure.persistence.GuestEntity;
import com.hospitality.api.infraestructure.persistence.GuestRepository;
import com.hospitality.api.infraestructure.repository.mappers.GuestEntityMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class GuestPostgresRepository implements GuestGateway {

    private final GuestRepository guestRepository;
    private final GuestEntityMapper guestEntityMapper;

    @Override
    public Guest save(Guest guest) {
        GuestEntity guestEntity = this.guestEntityMapper.toEntity(guest);
        this.guestRepository.save(guestEntity);
        return guest;
    }

    @Override
    public Guest findById(Long id) {
        Optional<GuestEntity> guestEntity = this.guestRepository.findById(id);
        return guestEntity.map(this.guestEntityMapper::toGuest).orElse(null);
    }

    @Override
    public List<Guest> findByFilter(GuestFilterRequest filterRequest) {
        Optional<List<GuestEntity>> guestEntity = this.guestRepository.findByNameOrDocumentOrPhone(filterRequest.filter(), filterRequest.filter(), filterRequest.filter());
        return guestEntity
                .map(entities -> entities.stream()
                        .map(this.guestEntityMapper::toGuest)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }
}
