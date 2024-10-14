package com.erika.eclipse_hotel.service;

import com.erika.eclipse_hotel.dto.customer.CustomerCreateRequestDTO;
import com.erika.eclipse_hotel.dto.customer.CustomerResponseDTO;
import com.erika.eclipse_hotel.dto.customer.CustomerUpdateRequestDTO;
import com.erika.eclipse_hotel.entity.Customer;
import com.erika.eclipse_hotel.exception.customer.CustomerAlreadyExistsException;
import com.erika.eclipse_hotel.exception.customer.CustomerNotFoundException;
import com.erika.eclipse_hotel.repository.CustomerRepository;
import com.erika.eclipse_hotel.service.mapper.CustomerMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CustomerService {

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private CustomerRepository customerRepository;

    @Async
    public CompletableFuture<CustomerResponseDTO> createCustomer(CustomerCreateRequestDTO request) {
        log.info("Trying to create new user. request: {}", request);

        // Check if customer already exists and ensure uniqueness of name, email and phone
        if (customerRepository.existsByName(request.getName())) {
            throw new CustomerAlreadyExistsException("Name already exists.");
        } else if (customerRepository.existsByEmail(request.getEmail())) {
            throw new CustomerAlreadyExistsException("Email already exists.");
        } else if (customerRepository.existsByPhone(request.getPhone())) {
            throw new CustomerAlreadyExistsException("Phone already exists.");
        }

        Customer customer = customerMapper.toEntity(request);

        customer = customerRepository.save(customer);
        log.info("Customer created successfully. ID: {}", customer.getId());

        return CompletableFuture.completedFuture(customerMapper.toResponseDTO(customer));
    }

    @Async
    public CompletableFuture<List<CustomerResponseDTO>>getAllCustomers() {
        log.info("Trying to fetch all customers.");
        List<Customer> customers = customerRepository.findAll();
        log.info("Found {} customers.", customers.size());

        List<CustomerResponseDTO> customerResponseDTOS = customers.stream()
                .map(customerMapper::toResponseDTO)
                .collect(Collectors.toList());

        return CompletableFuture.completedFuture(customerResponseDTOS);
    }

    @Async
    public CompletableFuture<CustomerResponseDTO> getCustomerById(UUID id) {
        log.info("Trying to fetch customer. ID {}", id);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found."));
        log.info("Customer found. ID: {}", id);
        return CompletableFuture.completedFuture(customerMapper.toResponseDTO(customer));
    }

    @Async
    public CompletableFuture<CustomerResponseDTO> updateCustomerById(UUID id, CustomerUpdateRequestDTO request) {
        log.info("Trying to update customer. ID: {}", id);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found."));

        log.debug("Updating customer details: {}", request);
        // Update only non-null fields from the DTO
        if (request.getName() != null) {
            customer.setName(request.getName());
        }
        if (request.getEmail() != null) {
            customer.setEmail(request.getEmail());
        }
        if (request.getPhone() != null) {
            customer.setPhone(request.getPhone());
        }

        Customer updatedCustomer = customerRepository.save(customer);
        log.info("Customer updated successfully. ID: {}", id);

        return CompletableFuture.completedFuture(customerMapper.toResponseDTO(updatedCustomer));
    }

    @Async
    public CompletableFuture<String> deleteCustomerById(UUID id) {
        log.info("Trying to delete customer. ID: {}", id);
        if (!customerRepository.existsById(id)) {
            throw new CustomerNotFoundException("Customer not found.");
        }
        customerRepository.deleteById(id);
        log.info("Customer deleted successfully. ID: {}", id);
        return CompletableFuture.completedFuture("Customer deleted successfully.");
    }
}

