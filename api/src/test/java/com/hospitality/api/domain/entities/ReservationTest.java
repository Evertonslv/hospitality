package com.hospitality.api.domain.entities;

import com.hospitality.api.domain.exceptions.InvalidInformationException;
import com.hospitality.api.domain.exceptions.MissingInformationException;
import com.hospitality.api.domain.usecases.dtos.ChargeDetail;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationTest {

    static LocalDate checkInDate;
    static LocalDate checkOutDate;
    static LocalDateTime reservationDate;

    static ReservationStatus status;
    static Guest guest;

    @BeforeAll
    static void setup() {
        checkInDate = LocalDate.of(2024, 8, 5);
        checkOutDate = LocalDate.of(2024, 8, 7);
        reservationDate = LocalDateTime.of(2024, 8, 7, 12, 0);
        status = ReservationStatus.PENDING;
        guest = new Guest("Oliver Queen", "123456789", "95555-1234");
    }

    @Test
    void shouldCreateReservationWithValidData() {
        Reservation reservation = new Reservation(guest, checkInDate, checkOutDate, true, reservationDate, status);

        assertThat(reservation.getGuest()).isEqualTo(guest);
        assertThat(reservation.getCheckInDate()).isEqualTo(checkInDate);
        assertThat(reservation.getCheckOutDate()).isEqualTo(checkOutDate);
        assertThat(reservation.getReservationDate()).isEqualTo(reservationDate);
        assertThat(reservation.isHasCar()).isTrue();
    }

    @Test
    void shouldThrowExceptionWhenGuestIsNull() {
        assertThatThrownBy(() -> new Reservation(null, checkInDate, checkOutDate, true, reservationDate, status))
                .isInstanceOf(MissingInformationException.class)
                .hasMessage("O hóspede é obrigatório");
    }

    @Test
    void shouldThrowExceptionWhenCheckInDateIsNull() {
        assertThatThrownBy(() -> new Reservation(guest, null, checkOutDate, true, reservationDate, status))
                .isInstanceOf(MissingInformationException.class)
                .hasMessage("As datas de check-in e check-out são obrigatórias");
    }

    @Test
    void shouldThrowExceptionWhenCheckOutDateIsNull() {
        assertThatThrownBy(() -> new Reservation(guest, checkInDate, null, true, reservationDate, status))
                .isInstanceOf(MissingInformationException.class)
                .hasMessage("As datas de check-in e check-out são obrigatórias");
    }

    @Test
    void shouldThrowExceptionWhenCheckOutDateIsBeforeCheckInDate() {
        assertThatThrownBy(() -> new Reservation(guest, checkOutDate, checkInDate, true, reservationDate, status))
                .isInstanceOf(InvalidInformationException.class)
                .hasMessage("A data de check-out deve ser posterior à data de check-in");
    }

    @Test
    void shouldAllowCheckInAfter14Hours() {
        Reservation reservation = new Reservation(guest, checkInDate, checkOutDate, true, reservationDate, status);

        LocalDateTime checkInTime = LocalDateTime.of(2024, 8, 5, 14, 0);

        reservation.checkIn(checkInTime);

        assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.CHECKED_IN);
    }

    @Test
    void shouldNotAllowCheckInBefore14Hours() {
        Reservation reservation = new Reservation(guest, checkInDate, checkOutDate, true, reservationDate, status);

        LocalDateTime checkInTime = LocalDateTime.of(2024, 8, 5, 13, 59);

        assertThatThrownBy(() -> reservation.checkIn(checkInTime))
                .isInstanceOf(InvalidInformationException.class)
                .hasMessage("O check-in não pode ser realizado antes das 14h na data de check-in");
    }

    @Test
    void shouldAllowCheckOutBefore12Hours() {
        Reservation reservation = new Reservation(guest, checkInDate, checkOutDate, true, reservationDate, status);

        LocalDateTime checkInTime = LocalDateTime.of(2024, 8, 5, 14, 0);
        reservation.checkIn(checkInTime);

        LocalDateTime checkOutTime = LocalDateTime.of(2024, 8, 7, 11, 59);
        reservation.checkOut(checkOutTime);

        assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.CHECKED_OUT);
    }

    @Test
    void shouldNotAllowCheckOutBeforeCheckIn() {
        Reservation reservation = new Reservation(guest, checkInDate, checkOutDate, true, reservationDate, status);

        LocalDateTime checkOutTime = LocalDateTime.of(2024, 8, 7, 11, 59);

        assertThatThrownBy(() -> reservation.checkOut(checkOutTime))
                .isInstanceOf(InvalidInformationException.class)
                .hasMessage("Não é possível fazer o check-out sem ter feito o check-in primeiro");
    }

    @Test
    void shouldThrowCheckOutNotFoundExceptionWhenCheckoutDoesNotExist() {
        Reservation reservation = new Reservation(guest, checkInDate, checkOutDate, false, reservationDate, status);

        assertThatThrownBy(reservation::calculateTotalAmount)
                .isInstanceOf(InvalidInformationException.class)
                .hasMessage("Não é possível calcular o valor total sem realizar o check-out primeiro");
    }

    @Test
    void shouldCalculateTotalAmountWithoutCarDuringWeekdays() {
        Reservation reservation = new Reservation(guest, checkInDate, checkOutDate, false, reservationDate, status);
        reservation.checkIn(LocalDateTime.of(2024, 8, 5, 15, 0));
        reservation.checkOut(LocalDateTime.of(2024, 8, 7, 10, 0));

        List<ChargeDetail> details = reservation.calculateTotalAmount();
        double totalAmount = reservation.getTotalAmount(details);

        assertThat(totalAmount).isEqualTo(120.00 * 2);
    }

    @Test
    void shouldCalculateTotalAmountWithCarDuringWeekdays() {
        Reservation reservation = new Reservation(guest, checkInDate, checkOutDate, true, reservationDate, status);
        reservation.checkIn(LocalDateTime.of(2024, 8, 5, 15, 0));
        reservation.checkOut(LocalDateTime.of(2024, 8, 7, 10, 0));

        List<ChargeDetail> details = reservation.calculateTotalAmount();
        double totalAmount = reservation.getTotalAmount(details);

        assertThat(totalAmount).isEqualTo((120.00 + 15.00) * 2);
    }

    @Test
    void shouldCalculateTotalAmountWithCarIncludingWeekend() {
        LocalDate checkInDate = LocalDate.of(2024, 8, 9);
        LocalDate checkOutDate = LocalDate.of(2024, 8, 11);
        Reservation reservation = new Reservation(guest, checkInDate, checkOutDate, true, reservationDate, status);
        reservation.checkIn(LocalDateTime.of(2024, 8, 9, 15, 0));
        reservation.checkOut(LocalDateTime.of(2024, 8, 11, 10, 0));

        List<ChargeDetail> details = reservation.calculateTotalAmount();
        double totalAmount = reservation.getTotalAmount(details);

        assertThat(totalAmount).isEqualTo((120.00 + 15.00) + (180.00 + 20.00));
    }

    @Test
    void shouldThrowExceptionWhenCheckingInTwice() {
        Reservation reservation = new Reservation(guest, checkInDate, checkOutDate, true, reservationDate, status);

        LocalDateTime checkInTime = LocalDateTime.of(2024, 8, 5, 14, 0);
        reservation.checkIn(checkInTime);

        assertThatThrownBy(() -> reservation.checkIn(checkInTime))
                .isInstanceOf(InvalidInformationException.class)
                .hasMessage("O check-in já foi realizado");
    }

    @Test
    void shouldThrowExceptionWhenCheckingOutTwice() {
        Reservation reservation = new Reservation(guest, checkInDate, checkOutDate, true, reservationDate, status);

        LocalDateTime checkInTime = LocalDateTime.of(2024, 8, 5, 14, 0);
        reservation.checkIn(checkInTime);

        LocalDateTime checkOutTime = LocalDateTime.of(2024, 8, 7, 11, 59);
        reservation.checkOut(checkOutTime);

        assertThatThrownBy(() -> reservation.checkOut(checkOutTime))
                .isInstanceOf(InvalidInformationException.class)
                .hasMessage("O check-out já foi realizado");
    }

    @Test
    void shouldCalculateTotalAmountWithLateCheckout() {
        Reservation reservation = new Reservation(guest, checkInDate, checkOutDate, true, reservationDate, status);

        LocalDateTime checkInTime = LocalDateTime.of(2024, 8, 5, 14, 0);
        reservation.checkIn(checkInTime);

        LocalDateTime checkOutTime = LocalDateTime.of(2024, 8, 7, 12, 30); // Late checkout
        reservation.checkOut(checkOutTime);

        List<ChargeDetail> details = reservation.calculateTotalAmount();
        double totalAmount = reservation.getTotalAmount(details);

        assertThat(totalAmount).isEqualTo((120.00 + 15) * 2 + (120.00 * 0.5));
    }
}
