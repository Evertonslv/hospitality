package com.hospitality.api.domain.usecases;

import com.hospitality.api.domain.entities.Guest;
import com.hospitality.api.domain.usecases.dtos.CreateGuestRequest;
import com.hospitality.api.domain.usecases.dtos.GuestResponse;
import com.hospitality.api.domain.usecases.gateways.GuestGateway;
import com.hospitality.api.domain.usecases.mappers.GuestDtoMapper;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CreateGuestUseCase {
    private final GuestGateway guestGateway;
    private final GuestDtoMapper guestDtoMapper;

    public GuestResponse execute(CreateGuestRequest request) {
        Guest guest = new Guest(
                request.name(),
                request.document(),
                request.phone()
        );

        Guest guestSave = this.guestGateway.save(guest);
        return this.guestDtoMapper.toResponse(guestSave);
    }

}
