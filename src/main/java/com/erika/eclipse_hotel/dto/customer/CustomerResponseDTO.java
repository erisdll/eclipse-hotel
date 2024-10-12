package com.erika.eclipse_hotel.dto.customer;

import lombok.Data;

import java.util.UUID;

@Data
public class CustomerResponseDTO {
    private UUID id;
    private String name;
    private String email;
    private String phone;
    private String createAt;
}
