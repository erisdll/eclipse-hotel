package com.erika.eclipse_hotel.service.mapper;

import com.erika.eclipse_hotel.dto.customer.CustomerCreateRequestDTO;
import com.erika.eclipse_hotel.dto.customer.CustomerResponseDTO;
import com.erika.eclipse_hotel.entity.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    Customer toEntity(CustomerCreateRequestDTO customerCreateRequestDTO);

    CustomerResponseDTO toResponseDTO(Customer customer);
}
