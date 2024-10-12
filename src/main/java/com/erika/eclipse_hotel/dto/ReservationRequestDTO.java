package com.erika.eclipse_hotel.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationRequestDTO {
    @NotBlank(message = "Customer ID cannot be empty.")
    private String customerId;

    @NotBlank(message = "Customer ID cannot be empty.")
    private String roomId;

    @NotBlank(message = "Check-in Date cannot be empty.")
    @Pattern(regexp = "\\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2}:\\d{2}", message = "Check-in date format must be dd/MM/yyyy HH:mm:ss")
    private String checkIn;

    @NotBlank(message = "Check-in Date cannot be empty.")
    @Pattern(regexp = "\\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2}:\\d{2}", message = "Check-in date format must be dd/MM/yyyy HH:mm:ss")
    private String checkOut;
}
