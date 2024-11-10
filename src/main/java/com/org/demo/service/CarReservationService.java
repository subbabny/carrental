package com.org.demo.service;

import com.org.demo.entity.CarInventory;
import com.org.demo.entity.CarReservation;
import com.org.demo.exception.InvalidCarTypeException;
import com.org.demo.repository.CarInventoryRepository;
import com.org.demo.repository.CarReservationRepository;
import com.org.demo.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class CarReservationService implements CarReservationServiceI {
    private final CarInventoryRepository carInventoryRepository;
    private final CarReservationRepository carReservationRepository;

    @Autowired
    public CarReservationService(CarInventoryRepository carInventoryRepository, CarReservationRepository carReservationRepository) {
        this.carInventoryRepository = carInventoryRepository;
        this.carReservationRepository = carReservationRepository;
    }

    public boolean reserveCar(String carType, LocalDateTime localDateTime, int numberOfDays) {
        List<String> validCarTypes = Arrays.asList(Constants.SEDAN, Constants.SUV, Constants.VAN);

        if (!validCarTypes.contains(carType)) {
            throw new InvalidCarTypeException(String.format("%s%s%s", Constants.INVALID_CAR_TYPE, carType, Constants.CAR_TYPE));
        }


        Optional<CarInventory> optionalCarInventory = carInventoryRepository.findByCarType(carType);


        return optionalCarInventory.filter(carInventory -> carInventory.getAvailableCount() > 0)
                .map(carInventory -> {
                    carInventory.setAvailableCount(carInventory.getAvailableCount() - 1);
                    carInventoryRepository.save(carInventory);

                    CarReservation reservation = new CarReservation();
                    reservation.setCarType(carType);
                    reservation.setReservationDate(localDateTime);
                    reservation.setDurationDays(numberOfDays);
                    carReservationRepository.save(reservation);

                    return true;
                }).orElse(false);
    }

}
