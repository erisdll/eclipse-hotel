package com.erika.eclipse_hotel.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Data
public class RoomResponseDTO {
    private Long id;
    private String roomNumber;
    private String type;
    private BigDecimal price;
}
