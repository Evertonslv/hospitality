package com.hospitality.api.domain.entities;

import com.hospitality.api.domain.exceptions.InvalidInformationException;
import com.hospitality.api.domain.exceptions.MissingInformationException;
import com.hospitality.api.domain.usecases.dtos.ChargeDetail;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Reservation {

    private static final double WEEKDAY_RATE = 120.00;
    private static final double WEEKEND_RATE = 180.00;
    private static final double WEEKDAY_PARKING_RATE = 15.00;
    private static final double WEEKEND_PARKING_RATE = 20.00;

    private final Guest guest;
    private final boolean hasCar;
    private final LocalDate checkInDate;
    private final LocalDate checkOutDate;
    private final LocalDateTime reservationDate;
    private ReservationStatus status;
    private Long id;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;

    public Reservation(Long id, Guest guest, LocalDate checkInDate, LocalDate checkOutDate, LocalDateTime checkInTime, LocalDateTime checkOutTime, boolean hasCar, LocalDateTime reservationDate, ReservationStatus status) {
        this(guest, checkInDate, checkOutDate, hasCar, reservationDate, status);
        this.id = id;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
    }

    public Reservation(Guest guest, LocalDate checkInDate, LocalDate checkOutDate, boolean hasCar, LocalDateTime reservationDate, ReservationStatus status) {
        this.guest = guest;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.hasCar = hasCar;
        this.reservationDate = reservationDate;
        this.status = status;

        validateDates();
    }

    public void checkIn(LocalDateTime checkInTime) {
        if (ReservationStatus.CHECKED_IN.equals(status)) {
            throw new InvalidInformationException("Check-in has already been done");
        }

        if (checkInTime.toLocalDate().isBefore(checkInDate)) {
            throw new InvalidInformationException("Check-in cannot be done before check-in date");
        }

        if (checkInTime.toLocalTime().isBefore(LocalTime.of(14, 0))) {
            throw new InvalidInformationException("Check-in cannot be done before 2 PM on the check-in date");
        }

        this.checkInTime = checkInTime;
        this.status = ReservationStatus.CHECKED_IN;
    }

    public void checkOut(LocalDateTime checkOutTime) {
        if (ReservationStatus.CHECKED_OUT.equals(status)) {
            throw new InvalidInformationException("Check-out has already been done");
        }

        if (!ReservationStatus.CHECKED_IN.equals(status) && checkInTime == null) {
            throw new InvalidInformationException("Cannot check out without checking in first");
        }

        this.checkOutTime = checkOutTime;
        this.status = ReservationStatus.CHECKED_OUT;
    }

    public List<ChargeDetail> calculateTotalAmount() {
        validateCheckoutStatus();

        List<ChargeDetail> chargeDetails = new ArrayList<>();
        LocalDateTime currentDate = checkInTime;

        while (currentDate.isBefore(checkOutTime)) {
            addDailyCharges(chargeDetails, currentDate);
            if (hasCar) {
                addParkingCharges(chargeDetails, currentDate);
            }
            currentDate = currentDate.plusDays(1);
        }

        if (isCheckedOutLate()) {
            addLateCheckoutCharge(chargeDetails, currentDate);
        }

        return chargeDetails;
    }

    private void addDailyCharges(List<ChargeDetail> chargeDetails, LocalDateTime date) {
        boolean isWeekend = isWeekend(date);
        double dailyRate = isWeekend ? WEEKEND_RATE : WEEKDAY_RATE;
        String description = isWeekend ? "Diária final de semana" : "Diária normal";
        chargeDetails.add(new ChargeDetail(description, dailyRate));
    }

    private void addParkingCharges(List<ChargeDetail> chargeDetails, LocalDateTime date) {
        boolean isWeekend = isWeekend(date);
        double parkingRate = isWeekend ? WEEKEND_PARKING_RATE : WEEKDAY_PARKING_RATE;
        String description = isWeekend ? "Taxa de estacionamento final de semana" : "Taxa de estacionamento normal";
        chargeDetails.add(new ChargeDetail(description, parkingRate));
    }

    private void addLateCheckoutCharge(List<ChargeDetail> chargeDetails, LocalDateTime date) {
        boolean isWeekend = isWeekend(date);
        double dailyRate = isWeekend ? WEEKEND_RATE : WEEKDAY_RATE;
        String description = isWeekend ? "Taxa de checkout atrasado final de semana" : "Taxa de checkout atrasado normal";
        double lateCheckoutFee = dailyRate * 0.5;
        chargeDetails.add(new ChargeDetail(description, lateCheckoutFee));
    }

    public double getTotalAmount(List<ChargeDetail> chargeDetails) {
        return chargeDetails.stream().mapToDouble(ChargeDetail::amount).sum();
    }

    private boolean isCheckedOutLate() {
        LocalTime checkoutDeadline = LocalTime.of(12, 0);
        return checkOutTime != null && checkOutTime.toLocalTime().isAfter(checkoutDeadline);
    }

    private boolean isWeekend(LocalDateTime date) {
        return date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY;
    }

    private void validateDates() {
        if (guest == null) {
            throw new MissingInformationException("Guest is required");
        }

        if (checkInDate == null || checkOutDate == null) {
            throw new MissingInformationException("Check-in and Check-out dates are required");
        }

        if (checkOutDate.isBefore(checkInDate)) {
            throw new InvalidInformationException("Check-out date must be after check-in date");
        }
    }

    private void validateCheckoutStatus() {
        if (!ReservationStatus.CHECKED_OUT.equals(status)) {
            throw new InvalidInformationException("Cannot calculate total amount without checkout in first");
        }
    }

}
