package com.erika.eclipse_hotel.controller;

import com.erika.eclipse_hotel.dto.customer.CustomerCreateRequestDTO;
import com.erika.eclipse_hotel.dto.customer.CustomerResponseDTO;
import com.erika.eclipse_hotel.dto.customer.CustomerUpdateRequestDTO;
import com.erika.eclipse_hotel.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping
    public CompletableFuture<ResponseEntity<CustomerResponseDTO>> createCustomer(@RequestBody @Valid CustomerCreateRequestDTO customerCreateRequestDTO) {
        return customerService.createCustomer(customerCreateRequestDTO)
                .thenApply(customerResponseDTO -> {
                    UUID customerId = customerResponseDTO.getId();
                    return ResponseEntity
                            .created(URI.create("/api/customers/" + customerId))
                            .body(customerResponseDTO);
                });
    }

    @GetMapping
    public CompletableFuture<ResponseEntity<List<CustomerResponseDTO>>> getAllCustomers() {
        return customerService.getAllCustomers().thenApply(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<CustomerResponseDTO>> getCustomerById(@PathVariable UUID id) {
        return customerService.getCustomerById(id).thenApply(ResponseEntity::ok);
    }

    @PatchMapping("/{id}")
    public CompletableFuture<ResponseEntity<CustomerResponseDTO>> updateCustomerById(
            @PathVariable UUID id,
            @RequestBody @Valid CustomerUpdateRequestDTO customerUpdateRequestDTO) {
        return customerService.updateCustomerById(id, customerUpdateRequestDTO).thenApply(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<String>> deleteCustomerById(@PathVariable UUID id) {
        return customerService.deleteCustomerById(id).thenApply(ResponseEntity::ok);
    }
}
