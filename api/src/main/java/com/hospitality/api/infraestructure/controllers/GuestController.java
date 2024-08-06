package com.hospitality.api.infraestructure.controllers;

import com.hospitality.api.domain.usecases.CreateGuestUseCase;
import com.hospitality.api.domain.usecases.FindGuestsByFilterUseCase;
import com.hospitality.api.domain.usecases.dtos.CreateGuestRequest;
import com.hospitality.api.domain.usecases.dtos.GuestFilterRequest;
import com.hospitality.api.domain.usecases.dtos.GuestResponse;
import com.hospitality.api.infraestructure.response.HospitalityResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("guest")
public class GuestController {

    private final CreateGuestUseCase createReservationUseCase;
    private final FindGuestsByFilterUseCase findGuestsByFilterUseCase;

    @PostMapping
    public HospitalityResponse<GuestResponse> createGuets(@RequestBody CreateGuestRequest request) {
        GuestResponse guestResponse = createReservationUseCase.execute(request);
        return new HospitalityResponse<>(HttpStatus.CREATED, guestResponse);
    }

    @PostMapping("/find-filter")
    public HospitalityResponse<List<GuestResponse>> find(@RequestBody GuestFilterRequest request) {
        List<GuestResponse> guestResponse = findGuestsByFilterUseCase.execute(request);
        return new HospitalityResponse<>(HttpStatus.OK, guestResponse);
    }

}
