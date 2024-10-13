package com.erika.eclipse_hotel.dto.room;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RoomCreateRequestDTO {
    @NotBlank(message = "Room number is mandatory")
    private String roomNumber;

    @NotBlank(message = "Type is mandatory")
    private String type;

    @NotBlank(message = "Price is mandatory")
    private String price;
}
