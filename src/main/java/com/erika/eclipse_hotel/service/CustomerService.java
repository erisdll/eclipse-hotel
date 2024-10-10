package com.erika.eclipse_hotel.service;

import com.erika.eclipse_hotel.dto.CustomerRequestDTO;
import com.erika.eclipse_hotel.dto.CustomerResponseDTO;
import com.erika.eclipse_hotel.entity.Customer;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.erika.eclipse_hotel.repository.CustomerRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public CustomerResponseDTO createCustomer(CustomerRequestDTO customerRequestDTO) {
        Customer customer = toEntity(customerRequestDTO);
        customer = customerRepository.save(customer);

        return toResponseDTO(customer);
    }

    public List<CustomerResponseDTO> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();

        return customers
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public CustomerResponseDTO getCustomerById(UUID id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found. ID: " + id));
        return toResponseDTO(customer);
    }

    public CustomerResponseDTO updatedCustomerById(UUID id, CustomerRequestDTO customerRequestDTO) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found. ID: " + id));

        customer.setName(customerRequestDTO.getName());
        customer.setEmail(customerRequestDTO.getEmail());
        customer.setPhone(customerRequestDTO.getPhone());

        Customer updatedCustomer = customerRepository.save(customer);
        return toResponseDTO(customer);
    }

    public void deleteCustomerById(UUID id) {
        if (!customerRepository.existsById(id)) {
            throw new EntityNotFoundException("Customer not found. ID: " + id);
        }
        customerRepository.deleteById(id);
    }

    // Conversion Methods
    private Customer toEntity(CustomerRequestDTO customerDTO) {
        Customer customer = new Customer();
        customer.setName(customerDTO.getName());
        customer.setEmail(customerDTO.getEmail());
        customer.setPhone(customerDTO.getPhone());

        return customer;
    }

    private CustomerResponseDTO toResponseDTO(Customer customer) {
        CustomerResponseDTO customerDTO = new CustomerResponseDTO();
        customerDTO.setId(customer.getId());
        customerDTO.setName(customer.getName());
        customerDTO.setEmail(customer.getEmail());
        customerDTO.setPhone(customer.getPhone());
        customerDTO.setCreateAt(customer.getCreateAt());

        return customerDTO;
    }
}

