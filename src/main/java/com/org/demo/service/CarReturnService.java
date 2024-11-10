package com.org.demo.service;

import com.org.demo.entity.CarInventory;
import com.org.demo.exception.CarLimitReachedException;
import com.org.demo.exception.InvalidCarTypeException;
import com.org.demo.repository.CarInventoryRepository;
import com.org.demo.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CarReturnService implements CarReturnServiceI{
    private final CarInventoryRepository carInventoryRepository;

    public CarReturnService(CarInventoryRepository carInventoryRepository) {
        this.carInventoryRepository = carInventoryRepository;
    }

    public void returnCar(String carType) {
        log.info("CarReturnService");

        // Find the car inventory based on the car type
        CarInventory carInventory = carInventoryRepository.findByCarType(carType)
                .orElseThrow(() -> new InvalidCarTypeException(Constants.CAR_TYPE_NOT_FOUNT));

        // Check if the available count exceeds the max limit
        if (carInventory.getAvailableCount() >= carInventory.getMaxCount()) {
            throw new CarLimitReachedException(Constants.CAR_MAX_LIMIT);
        }

        // Increment the available count as car is being returned
        carInventory.setAvailableCount(carInventory.getAvailableCount() + 1);

        // Save the updated inventory back to the database
        carInventoryRepository.save(carInventory);
    }
}
