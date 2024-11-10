package com.org.demo.service;

import java.time.LocalDateTime;

public interface CarReservationServiceI {

    public boolean reserveCar(String carType, LocalDateTime localDateTime, int numberOfDays);
}
