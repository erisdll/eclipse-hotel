package com.erika.eclipse_hotel.dto;

import com.erika.eclipse_hotel.entity.Room;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Data
public class RoomRequestDTO {
    @NotBlank(message = "Room number is mandatory")
    private String roomNumber;

    @NotBlank(message = "Type is mandatory")
    private String type;

    @NotBlank(message = "Price is mandatory")
    private String price;
}
