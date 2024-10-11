package com.erika.eclipse_hotel.dto;


import com.erika.eclipse_hotel.enums.ReservationStatus;
import lombok.Data;

@Data
public class ReservationResponseDTO {
    private Long id;
    private String customerName;
    private String roomNumber;
    private String roomType;
    private String checkIn;
    private String checkOut;
    private ReservationStatus status;
}

