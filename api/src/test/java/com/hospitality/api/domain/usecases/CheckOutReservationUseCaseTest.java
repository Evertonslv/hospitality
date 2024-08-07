package com.hospitality.api.domain.usecases;

import com.hospitality.api.domain.entities.Guest;
import com.hospitality.api.domain.entities.Reservation;
import com.hospitality.api.domain.entities.ReservationStatus;
import com.hospitality.api.domain.exceptions.InvalidInformationException;
import com.hospitality.api.domain.exceptions.ReservationNotFoundException;
import com.hospitality.api.domain.usecases.dtos.ChargeDetail;
import com.hospitality.api.domain.usecases.dtos.CheckOutResponse;
import com.hospitality.api.domain.usecases.dtos.ReservationCheckInOutRequest;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CheckOutReservationUseCaseTest {
    @Mock
    private ReservationGateway reservationGateway;
    @Mock
    private ReservationDtoMapper reservationDtoMapper;
    @InjectMocks
    private CheckOutReservationUseCase checkOutReservationUseCase;
    private Reservation reservation;
    private CheckOutResponse checkOutResponse;
    private ReservationCheckInOutRequest reservationCheckInOutRequest;
    private Guest guest;
    private final Long reservationId = 1L;

    private List<ChargeDetail> details;

    @BeforeEach
    void setUp() {
        guest = new Guest(1L, "Peter Parker", "123456789", "(47) 99999-9999");
        reservationCheckInOutRequest = new ReservationCheckInOutRequest(reservationId, LocalDateTime.of(2024, 8, 4, 14, 0));

        reservation = new Reservation(
                reservationId,
                guest,
                LocalDate.of(2024, 8, 4),
                LocalDate.of(2024, 8, 6),
                null,
                null,
                true,
                LocalDateTime.of(2024, 8, 3, 10, 0),
                ReservationStatus.PENDING
        );

        details = Arrays.asList(
                new ChargeDetail("Taxa de estacionamento (dia de semana)", 15.0, 1),
                new ChargeDetail("Taxa de estacionamento (final de semana)", 20.0, 1),
                new ChargeDetail("Diária (dia de semana)", 120.0, 1),
                new ChargeDetail("Diária (final de semana)", 180.0, 1)
        );

        checkOutResponse = new CheckOutResponse(
                reservationId,
                guest,
                details,
                LocalDate.of(2024, 8, 4),
                LocalDate.of(2024, 8, 6),
                LocalDateTime.of(2024, 8, 4, 15, 0),
                LocalDateTime.of(2024, 8, 6, 11, 0),
                true,
                LocalDateTime.of(2024, 8, 3, 10, 0),
                ReservationStatus.CHECKED_OUT
        );
    }

    @Test
    void shouldCheckOutReservationSuccessfully() {
        reservation.checkIn(LocalDateTime.of(2024, 8, 4, 15, 0));

        when(reservationGateway.findById(reservationId)).thenReturn(reservation);
        when(reservationDtoMapper.toCheckOutResponse(any(Reservation.class), anyList())).thenReturn(checkOutResponse);
        reservationCheckInOutRequest = new ReservationCheckInOutRequest(reservationId, LocalDateTime.of(2024, 8, 6, 11, 0));

        CheckOutResponse response = checkOutReservationUseCase.execute(reservationCheckInOutRequest);

        assertNotNull(response);
        assertEquals(ReservationStatus.CHECKED_OUT, response.status());
        assertEquals(reservationCheckInOutRequest.checkInOutDateTime(), response.checkOutTime());

        verify(reservationGateway).findById(reservationId);
        verify(reservationGateway).save(reservation);
    }

    @Test
    void shouldThrowReservationNotFoundExceptionWhenReservationDoesNotExist() {
        when(reservationGateway.findById(reservationId)).thenReturn(null);
        assertThrows(ReservationNotFoundException.class, () -> checkOutReservationUseCase.execute(reservationCheckInOutRequest));
        verify(reservationGateway).findById(reservationId);
        verifyNoMoreInteractions(reservationGateway, reservationDtoMapper);
    }

    @Test
    void shouldThrowInvalidInformationExceptionWhenCheckinInvalid() {
        when(reservationGateway.findById(reservationId)).thenReturn(reservation);

        assertThatThrownBy(() -> checkOutReservationUseCase.execute(reservationCheckInOutRequest))
                .isInstanceOf(InvalidInformationException.class)
                .hasMessage("Não é possível fazer o check-out sem ter feito o check-in primeiro");

        verify(reservationGateway).findById(reservationId);
        verifyNoMoreInteractions(reservationGateway, reservationDtoMapper);
    }

    @Test
    void shouldThrowInvalidInformationExceptionWhenAlreadyCheckout() {
        Reservation reservation = new Reservation(
                reservationId,
                guest,
                LocalDate.of(2024, 8, 4),
                LocalDate.of(2024, 8, 6),
                LocalDateTime.of(2024, 8, 4, 15, 0),
                LocalDateTime.of(2024, 8, 6, 10, 0),
                true,
                LocalDateTime.of(2024, 8, 3, 10, 0),
                ReservationStatus.CHECKED_OUT
        );

        when(reservationGateway.findById(reservationId)).thenReturn(reservation);
        reservationCheckInOutRequest = new ReservationCheckInOutRequest(reservationId, LocalDateTime.of(2024, 8, 3, 16, 0));

        assertThatThrownBy(() -> checkOutReservationUseCase.execute(reservationCheckInOutRequest))
                .isInstanceOf(InvalidInformationException.class)
                .hasMessage("O check-out já foi realizado");

        verify(reservationGateway).findById(reservationId);
        verifyNoMoreInteractions(reservationGateway, reservationDtoMapper);
    }

}
