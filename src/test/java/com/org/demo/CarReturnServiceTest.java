package com.org.demo;

import com.org.demo.entity.CarInventory;
import com.org.demo.exception.CarLimitReachedException;
import com.org.demo.exception.InvalidCarTypeException;
import com.org.demo.repository.CarInventoryRepository;
import com.org.demo.service.CarReturnService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class CarReturnServiceTest {

    @Mock
    private CarInventoryRepository carInventoryRepository;

    @InjectMocks
    private CarReturnService carReturnService;

    private CarInventory carInventory;

    @BeforeEach
    public void setup() {
        try (var ignored = MockitoAnnotations.openMocks(this)) {

            carInventory = new CarInventory();
            carInventory.setCarType("Sedan");
            carInventory.setAvailableCount(5);
            carInventory.setMaxCount(10);
        }catch (Exception ex){
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Test
    public void testReturnCar_Success() {
        // Arrange
        when(carInventoryRepository.findByCarType("Sedan")).thenReturn(Optional.of(carInventory));

        // Act
        carReturnService.returnCar("Sedan");

        // Assert
        assertEquals(6, carInventory.getAvailableCount());
        verify(carInventoryRepository, times(1)).save(carInventory);
    }

    @Test
    public void testReturnCar_CarTypeNotFound() {
        // Arrange
        when(carInventoryRepository.findByCarType("Sedan")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(InvalidCarTypeException.class, () -> carReturnService.returnCar("Sedan"));
    }

    @Test
    public void testReturnCar_CarLimitReached() {
        // Arrange
        carInventory.setAvailableCount(10);  // Set available count to max limit
        when(carInventoryRepository.findByCarType("Sedan")).thenReturn(Optional.of(carInventory));

        // Act & Assert
        assertThrows(CarLimitReachedException.class, () -> carReturnService.returnCar("Sedan"));
    }

    @Test
    public void testReturnCar_SaveCalledOnce() {
        // Arrange
        when(carInventoryRepository.findByCarType("SUV")).thenReturn(Optional.of(carInventory));

        // Act
        carReturnService.returnCar("SUV");

        // Assert
        verify(carInventoryRepository, times(1)).save(carInventory);
    }
}

