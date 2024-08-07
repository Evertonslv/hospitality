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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
            throw new InvalidInformationException("O check-in já foi realizado");
        }

        if (checkInTime.toLocalDate().isBefore(checkInDate)) {
            throw new InvalidInformationException("O check-in não pode ser realizado antes da data de check-in");
        }

        if (checkInTime.toLocalTime().isBefore(LocalTime.of(14, 0))) {
            throw new InvalidInformationException("O check-in não pode ser realizado antes das 14h na data de check-in");
        }

        this.checkInTime = checkInTime;
        this.status = ReservationStatus.CHECKED_IN;
    }

    public void checkOut(LocalDateTime checkOutTime) {
        if (ReservationStatus.CHECKED_OUT.equals(status)) {
            throw new InvalidInformationException("O check-out já foi realizado");
        }

        if (!ReservationStatus.CHECKED_IN.equals(status) && checkInTime == null) {
            throw new InvalidInformationException("Não é possível fazer o check-out sem ter feito o check-in primeiro");
        }

        this.checkOutTime = checkOutTime;
        this.status = ReservationStatus.CHECKED_OUT;
    }

    public List<ChargeDetail> calculateTotalAmount() {
        validateCheckoutStatus();

        Map<String, ChargeDetail> chargesMap = new HashMap<>();
        LocalDateTime currentDate = checkInTime;

        while (currentDate.isBefore(checkOutTime)) {
            processDailyCharges(chargesMap, currentDate);
            if (hasCar) {
                processParkingCharges(chargesMap, currentDate);
            }
            currentDate = currentDate.plusDays(1);
        }

        if (isCheckedOutLate()) {
            addLateCheckoutCharge(chargesMap);
        }

        return new ArrayList<>(chargesMap.values());
    }

    private void processDailyCharges(Map<String, ChargeDetail> chargesMap, LocalDateTime date) {
        boolean isWeekend = isWeekend(date);
        String description = isWeekend ? "Diária (final de semana)" : "Diária (dia de semana)";
        double rate = isWeekend ? WEEKEND_RATE : WEEKDAY_RATE;
        addCharge(chargesMap, description, rate);
    }

    private void processParkingCharges(Map<String, ChargeDetail> chargesMap, LocalDateTime date) {
        boolean isWeekend = isWeekend(date);
        String description = isWeekend ? "Taxa de estacionamento (final de semana)" : "Taxa de estacionamento (dia de semana)";
        double rate = isWeekend ? WEEKEND_PARKING_RATE : WEEKDAY_PARKING_RATE;
        addCharge(chargesMap, description, rate);
    }

    private void addLateCheckoutCharge(Map<String, ChargeDetail> chargesMap) {
        boolean isWeekend = isWeekend(checkOutTime);
        String description = isWeekend ? "Taxa de checkout atrasado (final de semana)" : "Taxa de checkout atrasado (dia de semana)";
        double dailyRate = isWeekend ? WEEKEND_RATE : WEEKDAY_RATE;
        double lateCheckoutFee = dailyRate * 0.5;
        addCharge(chargesMap, description, lateCheckoutFee);
    }

    private void addCharge(Map<String, ChargeDetail> chargesMap, String description, double rate) {
        chargesMap.merge(description, new ChargeDetail(description, rate, 1),
                (existingDetail, newDetail) -> {
                    existingDetail.addQuantity(newDetail.getQuantity());
                    existingDetail.addamount(newDetail.getAmount());
                    return existingDetail;
                });
    }

    private boolean isWeekend(LocalDateTime date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }

    private boolean isCheckedOutLate() {
        LocalTime checkoutDeadline = LocalTime.of(12, 0);
        return checkOutTime != null && checkOutTime.toLocalTime().isAfter(checkoutDeadline);
    }

    public double getTotalAmount(List<ChargeDetail> chargeDetails) {
        return chargeDetails.stream().mapToDouble(ChargeDetail::getAmount).sum();
    }

    private void validateDates() {
        if (guest == null) {
            throw new MissingInformationException("O hóspede é obrigatório");
        }

        if (checkInDate == null || checkOutDate == null) {
            throw new MissingInformationException("As datas de check-in e check-out são obrigatórias");
        }

        if (checkOutDate.isBefore(checkInDate)) {
            throw new InvalidInformationException("A data de check-out deve ser posterior à data de check-in");
        }
    }

    private void validateCheckoutStatus() {
        if (!ReservationStatus.CHECKED_OUT.equals(status)) {
            throw new InvalidInformationException("Não é possível calcular o valor total sem realizar o check-out primeiro");
        }
    }

}
