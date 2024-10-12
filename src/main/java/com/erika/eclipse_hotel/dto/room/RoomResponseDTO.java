package com.erika.eclipse_hotel.dto.room;

import lombok.Data;

import java.util.UUID;

@Data
public class RoomResponseDTO {
    private UUID id;
    private String roomNumber;
    private String type;
    private String price;
}
