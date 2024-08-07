package com.hospitality.api.domain.usecases;

import com.hospitality.api.domain.entities.Guest;
import com.hospitality.api.domain.entities.Reservation;
import com.hospitality.api.domain.entities.ReservationStatus;
import com.hospitality.api.domain.exceptions.GuestNotFoundException;
import com.hospitality.api.domain.exceptions.InvalidInformationException;
import com.hospitality.api.domain.usecases.dtos.CreateReservationRequest;
import com.hospitality.api.domain.usecases.dtos.ReservationResponse;
import com.hospitality.api.domain.usecases.gateways.GuestGateway;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateReservationUseCaseTest {
    @Mock
    private GuestGateway guestGateway;

    @Mock
    private ReservationGateway reservationGateway;

    @Mock
    private ReservationDtoMapper reservationDtoMapper;

    @InjectMocks
    private CreateReservationUseCase createReservationUseCase;

    private CreateReservationRequest request;

    private ReservationResponse reservationResponse;

    private Guest guest;

    @BeforeEach
    void setUp() {
        guest = new Guest("Clark Kent", "123456789", "(47) 99999-9999");

        request = new CreateReservationRequest(
                1L,
                LocalDate.now(),
                LocalDate.now().plusDays(2),
                true
        );

        reservationResponse = new ReservationResponse(
                1L,
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
    void shouldCreateReservationSuccessfully() {
        when(guestGateway.findById(request.guestId())).thenReturn(guest);
        when(reservationGateway.save(any(Reservation.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(reservationDtoMapper.toResponse(any(Reservation.class))).thenReturn(reservationResponse);

        ReservationResponse response = createReservationUseCase.execute(request);

        assertNotNull(response);

        verify(guestGateway).findById(request.guestId());
        verify(reservationGateway).save(any(Reservation.class));
        verify(reservationDtoMapper).toResponse(any(Reservation.class));
    }

    @Test
    void shouldThrowGuestNotFoundExceptionWhenGuestDoesNotExist() {
        when(guestGateway.findById(request.guestId())).thenReturn(null);

        assertThrows(GuestNotFoundException.class, () -> createReservationUseCase.execute(request));

        verify(guestGateway).findById(request.guestId());
        verifyNoMoreInteractions(reservationGateway, reservationDtoMapper);
    }

    @Test
    void shouldThrowInvalidInformationExceptionWhenCheckOutDateIsBeforeCheckInDate() {
        CreateReservationRequest request = new CreateReservationRequest(
                1L,
                LocalDate.now().plusDays(1),
                LocalDate.now().minusDays(2),
                true
        );

        assertThatThrownBy(() -> createReservationUseCase.execute(request))
                .isInstanceOf(InvalidInformationException.class)
                .hasMessage("A data de check-out deve ser posterior à data de check-in");

        verifyNoMoreInteractions(guestGateway, reservationGateway, reservationDtoMapper);
    }

    @Test
    void shouldThrowInvalidInformationExceptionWhenCheckInDateIsInThePast() {
        CreateReservationRequest request = new CreateReservationRequest(
                1L,
                LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(2),
                true
        );

        assertThatThrownBy(() -> createReservationUseCase.execute(request))
                .isInstanceOf(InvalidInformationException.class)
                .hasMessage("A data de check-in não pode ser no passado");

        verifyNoMoreInteractions(guestGateway, reservationGateway, reservationDtoMapper);
    }
}
