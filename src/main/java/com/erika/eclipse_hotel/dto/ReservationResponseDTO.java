package com.erika.eclipse_hotel.dto;


import com.erika.eclipse_hotel.enums.ReservationStatus;
import lombok.Data;

import java.util.UUID;

@Data
public class ReservationResponseDTO {
    private UUID id;
    private String customerName;
    private String roomNumber;
    private String roomType;
    private String checkIn;
    private String checkOut;
    private ReservationStatus status;
}

