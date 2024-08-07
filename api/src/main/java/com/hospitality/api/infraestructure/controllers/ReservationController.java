package com.hospitality.api.infraestructure.controllers;

import com.hospitality.api.domain.usecases.*;
import com.hospitality.api.domain.usecases.dtos.*;
import com.hospitality.api.infraestructure.response.HospitalityResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("reservation")
public class ReservationController {

    private final CreateReservationUseCase createReservationUseCase;
    private final CheckOutReservationUseCase checkOutReservationUseCase;
    private final CheckInReservationUseCase checkInReservationUseCase;
    private final FindGuestsInHotelUseCase findGuestsInHotelUseCase;
    private final FindGuestsWithPendingCheckInUseCase findGuestsWithPendingCheckInUseCase;

    @PostMapping
    public HospitalityResponse<ReservationResponse> createReservation(@RequestBody CreateReservationRequest request) {
        ReservationResponse reservationResponse = createReservationUseCase.execute(request);
        return new HospitalityResponse<>(HttpStatus.CREATED, reservationResponse);
    }

    @PostMapping("/checkout")
    public HospitalityResponse<CheckOutResponse> checkOutReservation(@RequestBody ReservationCheckInOutRequest reservationCheckInOutRequest) {
        CheckOutResponse checkOutResponse = checkOutReservationUseCase.execute(reservationCheckInOutRequest);
        return new HospitalityResponse<>(HttpStatus.OK, checkOutResponse);
    }

    @PostMapping("/checkin")
    public HospitalityResponse<ReservationResponse> checkInReservation(@RequestBody ReservationCheckInOutRequest reservationCheckInOutRequest) {
        ReservationResponse reservation = checkInReservationUseCase.execute(reservationCheckInOutRequest);
        return new HospitalityResponse<>(HttpStatus.OK, reservation);
    }

    @GetMapping("/find-checkin")
    public HospitalityResponse<List<ReservationResponse>> findGuestsInHotel() {
        List<ReservationResponse> reservationResponses = findGuestsInHotelUseCase.execute();
        return new HospitalityResponse<>(HttpStatus.OK, reservationResponses);
    }

    @GetMapping("/find-pending")
    public HospitalityResponse<List<ReservationResponse>> findGuestsWithPendingCheckIn() {
        List<ReservationResponse> reservationResponses = findGuestsWithPendingCheckInUseCase.execute();
        return new HospitalityResponse<>(HttpStatus.OK, reservationResponses);
    }

}
