package com.hospitality.api.domain.usecases.mappers;

import com.hospitality.api.domain.entities.Guest;
import com.hospitality.api.domain.usecases.dtos.GuestResponse;

public class GuestDtoMapper {

    public GuestResponse toResponse(Guest guest) {
        return new GuestResponse(
                guest.getId(), 
                guest.getName(),
                guest.getDocument(),
                guest.getPhone()
        );
    }

}
