package com.org.demo;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.org.demo.entity.CarInventory;
import com.org.demo.entity.CarReservation;
import com.org.demo.exception.InvalidCarTypeException;
import com.org.demo.repository.CarInventoryRepository;
import com.org.demo.repository.CarReservationRepository;
import com.org.demo.service.CarReservationService;
import com.org.demo.util.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDateTime;
import java.util.Optional;

public class CarReservationServiceTest {

    @Mock
    private CarInventoryRepository carInventoryRepository;

    @Mock
    private CarReservationRepository carReservationRepository;

    private CarReservationService carReservationService;

    @BeforeEach
    void setUp() {
        try (var ignored = MockitoAnnotations.openMocks(this)) {
            carReservationService = new CarReservationService(carInventoryRepository, carReservationRepository);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Test
    void testReserveCar_Success() {
        String carType = "SUV";
        int numberOfDays = 5;
        CarInventory carInventory = new CarInventory();
        carInventory.setCarType(carType);
        carInventory.setAvailableCount(2);

        when(carInventoryRepository.findByCarType(carType)).thenReturn(Optional.of(carInventory));

        boolean result = carReservationService.reserveCar(carType, LocalDateTime.now(), numberOfDays);

        assertTrue(result);
        verify(carInventoryRepository, times(1)).save(carInventory);
        verify(carReservationRepository, times(1)).save(any(CarReservation.class));
        assertEquals(1, carInventory.getAvailableCount());
    }

    @Test
    void testReserveCar_Failure_InsufficientInventory() {
        String carType = "SUV";
        int numberOfDays = 5;
        CarInventory carInventory = new CarInventory();
        carInventory.setCarType(carType);
        carInventory.setAvailableCount(0);

        when(carInventoryRepository.findByCarType(carType)).thenReturn(Optional.of(carInventory));

        boolean result = carReservationService.reserveCar(carType, LocalDateTime.now(), numberOfDays);

        assertFalse(result);
        verify(carInventoryRepository, never()).save(any(CarInventory.class));
        verify(carReservationRepository, never()).save(any(CarReservation.class));
    }

    @Test
    void testReserveCar_InvalidCarType() {
        // Arrange
        String carType = "Truck"; // Invalid car type
        int numberOfDays = 5;

        // Act and Assert
        InvalidCarTypeException exception = assertThrows(InvalidCarTypeException.class, () -> {
            carReservationService.reserveCar(carType, LocalDateTime.now(), numberOfDays);
        });

        assertEquals(String.format("%s%s%s", Constants.INVALID_CAR_TYPE, carType, Constants.CAR_TYPE), exception.getMessage());
    }

    @Test
    void testReserveCar_InvalidCarType_Empty() {
        // Arrange
        String carType = ""; // Empty car type
        int numberOfDays = 5;

        // Act and Assert
        InvalidCarTypeException exception = assertThrows(InvalidCarTypeException.class, () -> {
            carReservationService.reserveCar(carType, LocalDateTime.now(), numberOfDays);
        });

        assertEquals(String.format("%s%s%s", Constants.INVALID_CAR_TYPE, carType, Constants.CAR_TYPE), exception.getMessage());
    }

    @Test
    void testReserveCar_InvalidCarType_Null() {
        // Arrange
        String carType = null; // Null car type
        int numberOfDays = 5;

        // Act and Assert
        InvalidCarTypeException exception = assertThrows(InvalidCarTypeException.class, () -> {
            carReservationService.reserveCar(carType, LocalDateTime.now(), numberOfDays);
        });

        assertEquals(String.format("%s%s%s", Constants.INVALID_CAR_TYPE, carType, Constants.CAR_TYPE), exception.getMessage());
    }
}

