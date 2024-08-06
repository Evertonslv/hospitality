package com.hospitality.api.main;

import com.hospitality.api.domain.usecases.*;
import com.hospitality.api.domain.usecases.mappers.GuestDtoMapper;
import com.hospitality.api.domain.usecases.mappers.ReservationDtoMapper;
import com.hospitality.api.infraestructure.repository.mappers.GuestEntityMapper;
import com.hospitality.api.infraestructure.repository.mappers.ReservationEntityMapper;
import com.hospitality.api.infraestructure.repository.postgres.GuestPostgresRepository;
import com.hospitality.api.infraestructure.repository.postgres.ReservationPostgresRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HospitalityConfig {

    @Bean
    ReservationEntityMapper reservationEntityMapper() {
        return new ReservationEntityMapper();
    }

    @Bean
    GuestEntityMapper guestEntityMapper() {
        return new GuestEntityMapper();
    }

    @Bean
    CreateReservationUseCase createReservationUseCase(GuestPostgresRepository guestGateway, ReservationPostgresRepository reservationGateway, ReservationDtoMapper reservationDtoMapper) {
        return new CreateReservationUseCase(guestGateway, reservationGateway, reservationDtoMapper);
    }

    @Bean
    CheckOutReservationUseCase checkOutReservationUseCase(ReservationPostgresRepository reservationGateway, ReservationDtoMapper reservationDtoMapper) {
        return new CheckOutReservationUseCase(reservationGateway, reservationDtoMapper);
    }

    @Bean
    CheckInReservationUseCase checkInReservationUseCase(ReservationPostgresRepository reservationGateway, ReservationDtoMapper reservationDtoMapper) {
        return new CheckInReservationUseCase(reservationGateway, reservationDtoMapper);
    }

    @Bean
    CreateGuestUseCase createGuestUseCase(GuestPostgresRepository guestGateway, GuestDtoMapper guestDtoMapper) {
        return new CreateGuestUseCase(guestGateway, guestDtoMapper);
    }

    @Bean
    FindGuestsByFilterUseCase findGuestsByFilterUseCase(GuestPostgresRepository guestGateway, GuestDtoMapper guestDtoMapper) {
        return new FindGuestsByFilterUseCase(guestGateway, guestDtoMapper);
    }

    @Bean
    FindGuestsInHotelUseCase findGuestsInHotelUseCase(ReservationPostgresRepository reservationGateway, ReservationDtoMapper reservationDtoMapper) {
        return new FindGuestsInHotelUseCase(reservationGateway, reservationDtoMapper);
    }

    @Bean
    FindGuestsWithPendingCheckInUseCase findGuestsWithPendingCheckInUseCase(ReservationPostgresRepository reservationGateway, ReservationDtoMapper reservationDtoMapper) {
        return new FindGuestsWithPendingCheckInUseCase(reservationGateway, reservationDtoMapper);
    }

    @Bean
    GuestDtoMapper guestDtoMapper() {
        return new GuestDtoMapper();
    }

    @Bean
    ReservationDtoMapper reservationDtoMapper() {
        return new ReservationDtoMapper();
    }
}
