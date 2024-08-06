package com.hospitality.api.domain.usecases.gateways;

import com.hospitality.api.domain.entities.Guest;
import com.hospitality.api.domain.usecases.dtos.GuestFilterRequest;

import java.util.List;

public interface GuestGateway {

    Guest save(Guest guest);
    Guest findById(Long id);
    List<Guest> findByFilter(GuestFilterRequest filterRequest);
}