package com.hospitality.api.infraestructure.repository.mappers;

import com.hospitality.api.domain.entities.Guest;
import com.hospitality.api.infraestructure.persistence.GuestEntity;

public class GuestEntityMapper {
    public GuestEntity toEntity(Guest guest) {
        return GuestEntity.builder()
                .id(guest.getId())
                .name(guest.getName())
                .document(guest.getDocument())
                .phone(guest.getPhone())
                .build();
    }

    public Guest toGuest(GuestEntity guestEntity) {
        return new Guest(guestEntity.getId(), guestEntity.getName(), guestEntity.getDocument(), guestEntity.getPhone());
    }
}
