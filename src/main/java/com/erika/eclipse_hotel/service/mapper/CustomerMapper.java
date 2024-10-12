package com.erika.eclipse_hotel.service.mapper;

import com.erika.eclipse_hotel.dto.customer.CustomerRequestDTO;
import com.erika.eclipse_hotel.dto.customer.CustomerResponseDTO;
import com.erika.eclipse_hotel.entity.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    Customer toEntity(CustomerRequestDTO customerRequestDTO);

    CustomerResponseDTO toResponseDTO(Customer customer);
}
