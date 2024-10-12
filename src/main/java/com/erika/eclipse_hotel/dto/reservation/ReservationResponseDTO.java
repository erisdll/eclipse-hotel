package com.erika.eclipse_hotel.dto.reservation;


import com.erika.eclipse_hotel.enums.ReservationStatus;
import lombok.Data;

import java.util.UUID;

@Data
public class ReservationResponseDTO {
    private UUID id;
    private String customerId;
    private String roomId;
    private String checkIn;
    private String checkOut;
    private ReservationStatus status;
}

