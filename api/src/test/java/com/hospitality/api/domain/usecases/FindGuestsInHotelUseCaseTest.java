package com.hospitality.api.domain.usecases;

import com.hospitality.api.domain.entities.Guest;
import com.hospitality.api.domain.entities.Reservation;
import com.hospitality.api.domain.entities.ReservationStatus;
import com.hospitality.api.domain.usecases.dtos.ReservationResponse;
import com.hospitality.api.domain.usecases.gateways.ReservationGateway;
import com.hospitality.api.domain.usecases.mappers.GuestDtoMapper;
import com.hospitality.api.domain.usecases.mappers.ReservationDtoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FindGuestsInHotelUseCaseTest {

    @Mock
    private ReservationGateway reservationGateway;

    @Mock
    private GuestDtoMapper guestDtoMapper;

    @Mock
    private ReservationDtoMapper reservationDtoMapper;

    @InjectMocks
    private FindGuestsInHotelUseCase findGuestsInHotelUseCase;

    private Reservation reservation1;
    private Reservation reservation2;

    private ReservationResponse reservationResponse1;
    private ReservationResponse reservationResponse2;

    @BeforeEach
    void setUp() {
        Guest guest1 = new Guest(1L, "Wanda", "123456789", "(47) 99999-9999");
        Guest guest2 = new Guest(2L, "Vision ", "987654321", "(47) 99999-9999");

        reservation1 = new Reservation(
                1L,
                guest1,
                LocalDate.of(2024, 8, 5),
                LocalDate.of(2024, 8, 7),
                LocalDateTime.of(2024, 8, 5, 15, 0),
                LocalDateTime.of(2024, 8, 7, 10, 0),
                false,
                LocalDateTime.now(),
                ReservationStatus.CHECKED_IN
        );

        reservation2 = new Reservation(
                2L,
                guest2,
                LocalDate.of(2024, 8, 5),
                LocalDate.of(2024, 8, 7),
                LocalDateTime.of(2024, 8, 5, 15, 0),
                LocalDateTime.of(2024, 8, 7, 10, 0),
                true,
                LocalDateTime.now(),
                ReservationStatus.CHECKED_IN
        );

        reservationResponse1 = new ReservationResponse(
                1L,
                guest1,
                LocalDate.of(2024, 8, 5),
                LocalDate.of(2024, 8, 7),
                LocalDateTime.of(2024, 8, 5, 15, 0),
                LocalDateTime.of(2024, 8, 7, 10, 0),
                false,
                LocalDateTime.of(2024, 8, 1, 10, 0),
                ReservationStatus.CHECKED_IN
        );

        reservationResponse2 = new ReservationResponse(
                2L,
                guest2,
                LocalDate.of(2024, 8, 5),
                LocalDate.of(2024, 8, 7),
                LocalDateTime.of(2024, 8, 5, 15, 0),
                LocalDateTime.of(2024, 8, 7, 10, 0),
                true,
                LocalDateTime.of(2024, 8, 1, 10, 0),
                ReservationStatus.CHECKED_IN
        );

    }

    @Test
    void shouldReturnDistinctGuestsInHotel() {
        when(reservationGateway.findAllByCheckedOut(ReservationStatus.CHECKED_IN))
                .thenReturn(List.of(reservation1, reservation2));

        when(reservationDtoMapper.toResponse(reservation1)).thenReturn(reservationResponse1);
        when(reservationDtoMapper.toResponse(reservation2)).thenReturn(reservationResponse2);

        List<ReservationResponse> reservationResponses = findGuestsInHotelUseCase.execute();

        assertEquals(2, reservationResponses.size());
        assertTrue(reservationResponses.stream().map(ReservationResponse::id).toList().containsAll(List.of(reservation1.getId(), reservation2.getId())));
        verify(reservationGateway).findAllByCheckedOut(ReservationStatus.CHECKED_IN);
        verify(reservationDtoMapper).toResponse(reservation1);
        verify(reservationDtoMapper).toResponse(reservation2);
    }

    @Test
    void shouldReturnEmptyListWhenNoGuestsAreCheckedIn() {
        when(reservationGateway.findAllByCheckedOut(ReservationStatus.CHECKED_IN))
                .thenReturn(List.of());

        List<ReservationResponse> reservationResponses = findGuestsInHotelUseCase.execute();

        assertTrue(reservationResponses.isEmpty());
        verify(reservationGateway).findAllByCheckedOut(ReservationStatus.CHECKED_IN);
        verifyNoInteractions(guestDtoMapper);
    }

}
