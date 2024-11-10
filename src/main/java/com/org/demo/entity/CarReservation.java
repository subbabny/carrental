package com.org.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "car_reservation")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarReservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String carType;
    private LocalDateTime reservationDate;
    private int durationDays;

}
