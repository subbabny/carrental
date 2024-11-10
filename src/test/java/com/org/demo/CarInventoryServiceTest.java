package com.org.demo;

import com.org.demo.entity.CarInventory;
import com.org.demo.entity.CarReservation;
import com.org.demo.exception.InvalidCarTypeException;
import com.org.demo.repository.CarInventoryRepository;
import com.org.demo.repository.CarReservationRepository;
import com.org.demo.service.CarReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CarInventoryServiceTest {

    @Mock
    private CarInventoryRepository carInventoryRepository;

    @Mock
    private CarReservationRepository carReservationRepository;

    @InjectMocks
    private CarReservationService carReservationService;

    private CarInventory sedanCarInventory;

    @BeforeEach
    void setUp() {
        sedanCarInventory = new CarInventory();
        sedanCarInventory.setCarType("sedan");
        sedanCarInventory.setAvailableCount(5);
        sedanCarInventory.setMaxCount(10);
    }

    @Test
    void testReserveCarValidCarTypeAndAvailability() {
        String carType = "Sedan";
        when(carInventoryRepository.findByCarType(carType)).thenReturn(Optional.of(sedanCarInventory));

        carReservationService.reserveCar(carType, LocalDateTime.now(), 3);

        assertEquals(4, sedanCarInventory.getAvailableCount()); // Available count should decrease by 1
        verify(carInventoryRepository, times(1)).save(sedanCarInventory); // Ensure save was called on carInventoryRepository
        verify(carReservationRepository, times(1)).save(any()); // Ensure save was called on carReservationRepository
    }

    @Test
    void testInvalidCarType() {
        String expectedMessage = "Invalid car type other Please choose from Sedan, SUV, or Van.";

        InvalidCarTypeException exception = assertThrows(InvalidCarTypeException.class, () -> {
            carReservationService.reserveCar("other", LocalDateTime.now(), 3);
        });

        assertEquals(expectedMessage, exception.getMessage());
    }


    @Test
    void testReserveCarNoAvailableCars() {
        String carType = "Sedan";
        sedanCarInventory.setAvailableCount(0);
        when(carInventoryRepository.findByCarType(carType)).thenReturn(Optional.of(sedanCarInventory));

        boolean result = carReservationService.reserveCar(carType, LocalDateTime.now(), 3);

        // Assert
        assertFalse(result);
        verify(carInventoryRepository, never()).save(any(CarInventory.class));
        verify(carReservationRepository, never()).save(any(CarReservation.class));
    }

}


