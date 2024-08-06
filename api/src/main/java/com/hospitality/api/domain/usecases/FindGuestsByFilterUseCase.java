package com.hospitality.api.domain.usecases;

import com.hospitality.api.domain.entities.Guest;
import com.hospitality.api.domain.usecases.dtos.GuestFilterRequest;
import com.hospitality.api.domain.usecases.dtos.GuestResponse;
import com.hospitality.api.domain.usecases.gateways.GuestGateway;
import com.hospitality.api.domain.usecases.mappers.GuestDtoMapper;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class FindGuestsByFilterUseCase {
    private final GuestGateway guestGateway;
    private final GuestDtoMapper guestDtoMapper;

    public List<GuestResponse> execute(GuestFilterRequest filterRequest) {
        List<Guest> guests = guestGateway.findByFilter(filterRequest);
        return guests.stream()
                .map(guestDtoMapper::toResponse)
                .collect(Collectors.toList());
    }
}