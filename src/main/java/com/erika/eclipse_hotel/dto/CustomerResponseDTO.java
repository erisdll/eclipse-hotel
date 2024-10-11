package com.erika.eclipse_hotel.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CustomerResponseDTO {
    private UUID id;
    private String name;
    private String email;
    private String phone;
    private LocalDateTime createAt;
}
