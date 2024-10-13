package com.erika.eclipse_hotel.service;

import com.erika.eclipse_hotel.dto.customer.CustomerRequestDTO;
import com.erika.eclipse_hotel.dto.customer.CustomerResponseDTO;
import com.erika.eclipse_hotel.entity.Customer;
import com.erika.eclipse_hotel.repository.CustomerRepository;
import com.erika.eclipse_hotel.service.mapper.CustomerMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CustomerService {

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private CustomerRepository customerRepository;

    public CustomerResponseDTO createCustomer(CustomerRequestDTO request) {
        log.info("Attempting to create new user. request: {}", request);
        Customer customer = customerMapper.toEntity(request);

        customer = customerRepository.save(customer);
        log.info("Created new user successfully. ID: {}", customer.getId());

        return customerMapper.toResponseDTO(customer);
    }

    public List<CustomerResponseDTO> getAllCustomers() {
        log.info("Attempting to fetch all customers.");
        List<Customer> customers = customerRepository.findAll();
        log.info("Found {} customers.", customers.size());

        return customers.stream()
                .map(customerMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public CustomerResponseDTO getCustomerById(UUID id) {
        log.info("Attempting to fetch customer. ID {}", id);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found."));
        log.info("Customer found. ID: {}", id);
        return customerMapper.toResponseDTO(customer);
    }

    public CustomerResponseDTO updatedCustomerById(UUID id, CustomerRequestDTO request) {
        log.info("Attempting to update customer. ID: {}", id);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found."));

        log.debug("Updating customer details: {}", request);
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());

        Customer updatedCustomer = customerRepository.save(customer);
        log.info("Customer updated successfully. ID: {}", id);

        return customerMapper.toResponseDTO(updatedCustomer);
    }

    public void deleteCustomerById(UUID id) {
        log.info("Attempting to delete customer. ID: {}", id);
        if (!customerRepository.existsById(id)) {
            throw new EntityNotFoundException("Customer not found.");
        }
        customerRepository.deleteById(id);
        log.info("Customer deleted successfully. ID: {}", id);
    }
}

