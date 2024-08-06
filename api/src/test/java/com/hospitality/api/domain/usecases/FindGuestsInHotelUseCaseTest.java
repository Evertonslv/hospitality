package com.hospitality.api.domain.usecases;

import com.hospitality.api.domain.entities.Guest;
import com.hospitality.api.domain.entities.Reservation;
import com.hospitality.api.domain.entities.ReservationStatus;
import com.hospitality.api.domain.usecases.dtos.GuestResponse;
import com.hospitality.api.domain.usecases.dtos.ReservationResponse;
import com.hospitality.api.domain.usecases.gateways.ReservationGateway;
import com.hospitality.api.domain.usecases.mappers.GuestDtoMapper;
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

    @InjectMocks
    private FindGuestsInHotelUseCase findGuestsInHotelUseCase;

    private Reservation reservation1;
    private Reservation reservation2;
    private Guest guest1;
    private Guest guest2;

    @BeforeEach
    void setUp() {
        guest1 = new Guest(1L, "Wanda", "123456789", "(47) 99999-9999");
        guest2 = new Guest(2L, "Vision ", "987654321", "(47) 99999-9999");

        reservation1 = new Reservation(
                guest1,
                LocalDate.of(2024, 8, 5),
                LocalDate.of(2024, 8, 7),
                false,
                LocalDateTime.now(),
                ReservationStatus.CHECKED_IN
        );

        reservation2 = new Reservation(
                guest2,
                LocalDate.of(2024, 8, 5),
                LocalDate.of(2024, 8, 7),
                true,
                LocalDateTime.now(),
                ReservationStatus.CHECKED_IN
        );
    }

    @Test
    void shouldReturnDistinctGuestsInHotel() {
        when(reservationGateway.findAllByCheckedOut(ReservationStatus.CHECKED_IN))
                .thenReturn(List.of(reservation1, reservation2));

        when(guestDtoMapper.toResponse(guest1)).thenReturn(new GuestResponse(guest1.getId(), guest1.getName(), guest1.getDocument(), guest1.getPhone()));
        when(guestDtoMapper.toResponse(guest2)).thenReturn(new GuestResponse(guest2.getId(), guest2.getName(), guest2.getDocument(), guest2.getPhone()));

        List<ReservationResponse> reservationResponses = findGuestsInHotelUseCase.execute();

        assertEquals(2, reservationResponses.size());
        assertTrue(reservationResponses.stream().map(ReservationResponse::id).toList().containsAll(List.of(guest1.getId(), guest2.getId())));
        verify(reservationGateway).findAllByCheckedOut(ReservationStatus.CHECKED_IN);
        verify(guestDtoMapper).toResponse(guest1);
        verify(guestDtoMapper).toResponse(guest2);
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
