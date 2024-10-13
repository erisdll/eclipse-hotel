package com.erika.eclipse_hotel.controller;

import com.erika.eclipse_hotel.dto.customer.CustomerCreateRequestDTO;
import com.erika.eclipse_hotel.dto.customer.CustomerResponseDTO;
import com.erika.eclipse_hotel.dto.customer.CustomerUpdateRequestDTO;
import com.erika.eclipse_hotel.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerResponseDTO> createCustomer(@RequestBody @Valid CustomerCreateRequestDTO customerCreateRequestDTO) {
        CustomerResponseDTO customer = customerService.createCustomer(customerCreateRequestDTO);
        return ResponseEntity.ok(customer);
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponseDTO>> getAllCustomers() {
        List<CustomerResponseDTO> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> getCustomerById(@PathVariable UUID id) {
        CustomerResponseDTO customer = customerService.getCustomerById(id);
        return ResponseEntity.ok(customer);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> updateCustomerById(
            @PathVariable UUID id,
            @RequestBody @Valid CustomerUpdateRequestDTO customerUpdateRequestDTO) {
        CustomerResponseDTO responseDTO = customerService.updateCustomerById(id, customerUpdateRequestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomerById(@PathVariable UUID id) {
        customerService.deleteCustomerById(id);
        return ResponseEntity.noContent().build();
    }
}
