package com.org.demo.controller;

import com.org.demo.entity.CarInventory;
import com.org.demo.model.CarReservationRequest;
import com.org.demo.model.CarReturnRequest;
import com.org.demo.repository.CarInventoryRepository;
import com.org.demo.service.CarReservationService;
import com.org.demo.service.CarReservationServiceI;
import com.org.demo.service.CarReturnService;
import com.org.demo.service.CarReturnServiceI;
import com.org.demo.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/car-rental")
@Slf4j
public class CarRentalController {

    private final CarReservationServiceI reservationService;
    private final CarReturnServiceI returnService;

    private final CarInventoryRepository carInventoryRepository;
    @Autowired
    public CarRentalController(CarReservationService reservationService, CarReturnService returnService, CarInventoryRepository carInventoryRepository) {
        this.reservationService = reservationService;
        this.returnService = returnService;
        this.carInventoryRepository = carInventoryRepository;
    }
    @GetMapping("/inventory")
    public String getInventory(@RequestParam String carType) {
        log.info("inventory");
        Optional<CarInventory> optionalCarInventory = carInventoryRepository.findByCarType(carType);
        return optionalCarInventory.map(carInventory -> "Available " + carType + " cars: " + carInventory.getAvailableCount())
                .orElse(Constants.CAR_TYPE_NOT_FOUNT);
    }

    @PostMapping("/reserve")
    public String reserveCar(@RequestBody CarReservationRequest reservationRequest) {
        log.info("reserveCar");
        boolean reserved = reservationService.reserveCar(reservationRequest.getCarType(), reservationRequest.getStartDate(), reservationRequest.getNumberOfDays());
        return reserved ? Constants.RESERVATION_SUCCESS + reservationRequest.getCarType() : "Reservation failed. No cars available.";
    }

    @PostMapping("/return")
    public String returnCar(@RequestBody CarReturnRequest returnRequest) {
        log.info("returnCar");
        returnService.returnCar(returnRequest.getCarType());
        return Constants.CAR_RETURN_SUCCESS + returnRequest.getCarType();
    }
}
