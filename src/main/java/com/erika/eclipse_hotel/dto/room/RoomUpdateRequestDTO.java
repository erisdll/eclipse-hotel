package com.erika.eclipse_hotel.dto.room;

import lombok.Data;

@Data
public class RoomUpdateRequestDTO {

    private String roomNumber;
    private String type;
    private String price;
}
