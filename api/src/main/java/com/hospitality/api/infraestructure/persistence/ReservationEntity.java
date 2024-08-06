package com.hospitality.api.infraestructure.persistence;

import com.hospitality.api.domain.entities.ReservationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "reservations")
public class ReservationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "guest_id", nullable = false)
    private GuestEntity guest;

    @Column(nullable = false)
    private LocalDateTime reservationDate;

    @Column()
    private LocalDate checkInDate;

    @Column()
    private LocalDate checkOutDate;

    @Column()
    private LocalDateTime checkInTime;

    @Column()
    private LocalDateTime checkOutTime;

    @Column(nullable = false)
    private boolean hasCar;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status;

}
