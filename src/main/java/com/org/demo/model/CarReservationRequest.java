package com.org.demo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarReservationRequest {

    private String carType;

    @Schema(description = "The start date of the reservation, formatted as MM-dd-yyyy HH:mm.")
    @JsonFormat(pattern = "MM-dd-yyyy HH:mm")
    private LocalDateTime startDate;
    private int numberOfDays;

}

