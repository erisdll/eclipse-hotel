package com.erika.eclipse_hotel.dto.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CustomerCreateRequestDTO {
    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Please insert a valid email.")
    private String email;

    @NotBlank(message = "Phone is mandatory")
    @Pattern(regexp = "\\d{10,12}", message = "Please insert a valid phone with 10 to 12 digits.")
    private String phone;
}
