package com.erika.eclipse_hotel.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class RoomResponseDTO {
    private UUID id;
    private String roomNumber;
    private String type;
    private String price;
}
