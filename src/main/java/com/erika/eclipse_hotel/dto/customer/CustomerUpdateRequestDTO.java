package com.erika.eclipse_hotel.dto.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class CustomerUpdateRequestDTO {

    @Email(message = "Please insert a valid email.")
    private String email;

    @Pattern(regexp = "\\d{10,12}", message = "Please insert a valid phone with 10 to 12 digits.")
    private String phone;

    @Length(min = 3, max = 50, message = "Name must be between 3 and 50 characters.")
    private String name;
}
