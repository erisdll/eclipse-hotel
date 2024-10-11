package com.erika.eclipse_hotel.service.mapper;

import com.erika.eclipse_hotel.dto.CustomerRequestDTO;
import com.erika.eclipse_hotel.dto.CustomerResponseDTO;
import com.erika.eclipse_hotel.entity.Customer;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {
    Customer toEntity(CustomerRequestDTO customerRequestDTO);
    CustomerResponseDTO toResponseDTO(Customer customer);
}
