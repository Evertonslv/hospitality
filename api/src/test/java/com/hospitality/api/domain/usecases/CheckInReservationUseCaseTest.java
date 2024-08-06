package com.hospitality.api.domain.usecases;

import com.hospitality.api.domain.entities.Guest;
import com.hospitality.api.domain.entities.Reservation;
import com.hospitality.api.domain.entities.ReservationStatus;
import com.hospitality.api.domain.exceptions.InvalidInformationException;
import com.hospitality.api.domain.exceptions.ReservationNotFoundException;
import com.hospitality.api.domain.usecases.dtos.ReservationCheckInOutRequest;
import com.hospitality.api.domain.usecases.dtos.ReservationResponse;
import com.hospitality.api.domain.usecases.gateways.ReservationGateway;
import com.hospitality.api.domain.usecases.mappers.ReservationDtoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CheckInReservationUseCaseTest {
    @Mock
    private ReservationGateway reservationGateway;
    @Mock
    private ReservationDtoMapper reservationDtoMapper;
    @InjectMocks
    private CheckInReservationUseCase checkInReservationUseCase;
    private Reservation reservation;
    private ReservationResponse reservationResponse;
    private ReservationCheckInOutRequest reservationCheckInOutRequest;
    private Guest guest;
    private final Long reservationId = 1L;

    @BeforeEach
    void setUp() {
        guest = new Guest(1L, "Tony Stark", "123456789", "(47) 99999-9999");
        reservationCheckInOutRequest = new ReservationCheckInOutRequest(reservationId, LocalDateTime.of(2024, 8, 4, 14, 0));

        reservation = new Reservation(
                reservationId,
                guest,
                LocalDate.of(2024, 8, 4),
                LocalDate.of(2024, 8, 6),
                LocalDateTime.of(2024, 8, 4, 15, 0),
                LocalDateTime.of(2024, 8, 6, 10, 0),
                true,
                LocalDateTime.of(2024, 8, 3, 10, 0),
                ReservationStatus.PENDING
        );

        reservationResponse = new ReservationResponse(
                reservationId,
                guest,
                LocalDate.of(2024, 8, 4),
                LocalDate.of(2024, 8, 6),
                LocalDateTime.of(2024, 8, 4, 15, 0),
                LocalDateTime.of(2024, 8, 6, 11, 0),
                true,
                LocalDateTime.of(2024, 8, 3, 20, 0),
                ReservationStatus.CHECKED_OUT
        );
    }

    @Test
    void shouldCheckOutReservationSuccessfully() {
        when(reservationGateway.findById(reservationId)).thenReturn(reservation);
        when(reservationDtoMapper.toResponse(reservation)).thenReturn(reservationResponse);

        ReservationResponse response = checkInReservationUseCase.execute(reservationCheckInOutRequest);

        assertNotNull(response);
        assertEquals(ReservationStatus.CHECKED_IN, reservation.getStatus());
        assertEquals(reservationCheckInOutRequest.checkInOutDateTime(), reservation.getCheckInTime());
        verify(reservationGateway).findById(reservationId);
        verify(reservationGateway).save(reservation);
        verify(reservationDtoMapper).toResponse(reservation);
    }

    @Test
    void shouldThrowReservationNotFoundExceptionWhenReservationDoesNotExist() {
        when(reservationGateway.findById(reservationId)).thenReturn(null);
        assertThrows(ReservationNotFoundException.class, () -> checkInReservationUseCase.execute(reservationCheckInOutRequest));
        verify(reservationGateway).findById(reservationId);
        verifyNoMoreInteractions(reservationGateway, reservationDtoMapper);
    }

    @Test
    void shouldThrowInvalidInformationExceptionWhenCheckinInvalid() {
        reservation = new Reservation(
                reservationId,
                guest,
                LocalDate.of(2024, 8, 4),
                LocalDate.of(2024, 8, 6),
                LocalDateTime.of(2024, 8, 4, 15, 0),
                LocalDateTime.of(2024, 8, 6, 10, 0),
                true,
                LocalDateTime.of(2024, 8, 3, 10, 0),
                ReservationStatus.CHECKED_IN
        );
        when(reservationGateway.findById(reservationId)).thenReturn(reservation);

        assertThatThrownBy(() -> checkInReservationUseCase.execute(reservationCheckInOutRequest))
                .isInstanceOf(InvalidInformationException.class)
                .hasMessage("Check-in has already been done");

        verify(reservationGateway).findById(reservationId);
        verifyNoMoreInteractions(reservationGateway, reservationDtoMapper);
    }

    @Test
    void shouldThrowInvalidInformationExceptionWhenCheckinBefore() {
        when(reservationGateway.findById(reservationId)).thenReturn(reservation);
        reservationCheckInOutRequest = new ReservationCheckInOutRequest(reservationId, LocalDateTime.of(2024, 8, 3, 16, 0));

        assertThatThrownBy(() -> checkInReservationUseCase.execute(reservationCheckInOutRequest))
                .isInstanceOf(InvalidInformationException.class)
                .hasMessage("Check-in cannot be done before check-in date");

        verify(reservationGateway).findById(reservationId);
        verifyNoMoreInteractions(reservationGateway, reservationDtoMapper);
    }

    @Test
    void shouldThrowInvalidInformationExceptionWhenCheckinBeforeTwoPM() {
        when(reservationGateway.findById(reservationId)).thenReturn(reservation);
        reservationCheckInOutRequest = new ReservationCheckInOutRequest(reservationId, LocalDateTime.of(2024, 8, 4, 10, 0));

        assertThatThrownBy(() -> checkInReservationUseCase.execute(reservationCheckInOutRequest))
                .isInstanceOf(InvalidInformationException.class)
                .hasMessage("Check-in cannot be done before 2 PM on the check-in date");

        verify(reservationGateway).findById(reservationId);
        verifyNoMoreInteractions(reservationGateway, reservationDtoMapper);
    }

}
