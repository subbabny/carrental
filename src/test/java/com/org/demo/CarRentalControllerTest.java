package com.org.demo;

import com.org.demo.controller.CarRentalController;
import com.org.demo.entity.CarInventory;
import com.org.demo.model.CarReservationRequest;
import com.org.demo.model.CarReturnRequest;
import com.org.demo.repository.CarInventoryRepository;
import com.org.demo.service.CarReservationService;
import com.org.demo.service.CarReturnService;
import com.org.demo.util.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class CarRentalControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CarReservationService reservationService;

    @MockBean
    private CarReturnService returnService;

    @MockBean
    private CarInventoryRepository carInventoryRepository;

    @InjectMocks
    private CarRentalController carRentalController;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testGetInventory_Success() throws Exception {
        String carType = "Sedan";
        CarInventory carInventory = new CarInventory();
        carInventory.setCarType(carType);
        carInventory.setAvailableCount(5);
        when(carInventoryRepository.findByCarType(carType)).thenReturn(Optional.of(carInventory));

        mockMvc.perform(get("/car-rental/inventory")
                        .param("carType", carType))
                .andExpect(status().isOk())
                .andExpect(content().string("Available Sedan cars: 5"));
    }

    @Test
    void testGetInventory_CarNotFound() throws Exception {
        String carType = "SUV";
        when(carInventoryRepository.findByCarType(carType)).thenReturn(Optional.empty());

        mockMvc.perform(get("/car-rental/inventory")
                        .param("carType", carType))
                .andExpect(status().isOk())
                .andExpect(content().string(Constants.CAR_TYPE_NOT_FOUNT));
    }

    @Test
    void testReserveCar_Success() throws Exception {
        String startDateStr = "12-01-2024 10:00"; // Correct LocalDateTime format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        LocalDateTime startDate = LocalDateTime.parse(startDateStr, formatter);
        CarReservationRequest reservationRequest = new CarReservationRequest();
        reservationRequest.setCarType("Sedan");
        reservationRequest.setStartDate(startDate);
        reservationRequest.setNumberOfDays(5);

        when(reservationService.reserveCar(anyString(), any(), anyInt())).thenReturn(true);

        mockMvc.perform(post("/car-rental/reserve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"carType\":\"Sedan\",\"startDate\":\"12-01-2024 10:00\",\"numberOfDays\":5}"))
                .andExpect(status().isOk())
                .andExpect(content().string(Constants.RESERVATION_SUCCESS + "Sedan"));
    }


    @Test
    void testReserveCar_Failure() throws Exception {
        String startDateStr = "12-01-2024 10:00"; // Correct LocalDateTime format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        LocalDateTime startDate = LocalDateTime.parse(startDateStr, formatter);
        CarReservationRequest reservationRequest = new CarReservationRequest();
        reservationRequest.setCarType("Sedan");
        reservationRequest.setStartDate(startDate);
        reservationRequest.setNumberOfDays(5);

        when(reservationService.reserveCar(anyString(), any(), anyInt())).thenReturn(false); // Simulating failure scenario

        mockMvc.perform(post("/car-rental/reserve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"carType\":\"Sedan\",\"startDate\":\"12-01-2024 10:00\",\"numberOfDays\":5}")) // Corrected startDate format
                .andExpect(status().isOk())
                .andExpect(content().string("Reservation failed. No cars available."));
    }


    @Test
    void testReturnCar_Success() throws Exception {
        CarReturnRequest returnRequest = new CarReturnRequest();
        returnRequest.setCarType("SUV");

        doNothing().when(returnService).returnCar(anyString());

        mockMvc.perform(post("/car-rental/return")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"carType\":\"SUV\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string(Constants.CAR_RETURN_SUCCESS + "SUV"));
    }

    @Test
    void testReturnCar_Exception() throws Exception {
        CarReturnRequest returnRequest = new CarReturnRequest();
        returnRequest.setCarType("SUV");

        doThrow(new RuntimeException("Error")).when(returnService).returnCar(anyString());

        mockMvc.perform(post("/car-rental/return")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"carType\":\"SUV\"}"))
                .andExpect(status().isInternalServerError()) // Expect 500 status
                .andExpect(content().string("An error occurred while processing the return")); // Expect the custom error message
    }

}

