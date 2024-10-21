package com.erika.eclipse_hotel.service;

import com.erika.eclipse_hotel.dto.customer.CustomerCreateRequestDTO;
import com.erika.eclipse_hotel.dto.customer.CustomerResponseDTO;
import com.erika.eclipse_hotel.dto.customer.CustomerUpdateRequestDTO;
import com.erika.eclipse_hotel.entity.Customer;
import com.erika.eclipse_hotel.exception.customer.CustomerAlreadyExistsException;
import com.erika.eclipse_hotel.exception.customer.CustomerNotFoundException;
import com.erika.eclipse_hotel.repository.CustomerRepository;
import com.erika.eclipse_hotel.service.mapper.CustomerMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerService customerService;

    private Customer customer;
    private CustomerResponseDTO customerResponseDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Initialize mocks

        // Mock Customer object
        customer = new Customer();
        customer.setId(UUID.randomUUID());
        customer.setName("John Doe");
        customer.setEmail("john.doe@example.com");
        customer.setPhone("123456789");

        // Mock CustomerResponseDTO object
        customerResponseDTO = new CustomerResponseDTO();
        customerResponseDTO.setId(customer.getId());
        customerResponseDTO.setName("John Doe");
        customerResponseDTO.setEmail("john.doe@example.com");
        customerResponseDTO.setPhone("123456789");
    }

    @Test
    void shouldCreateCustomerSuccessfully() {
        // Prepare mock request
        CustomerCreateRequestDTO customerRequest = new CustomerCreateRequestDTO();
        customerRequest.setName("John Doe");
        customerRequest.setEmail("john.doe@example.com");
        customerRequest.setPhone("123456789");

        // Mock repository and mapper behavior
        when(customerRepository.existsByName(anyString())).thenReturn(false);
        when(customerRepository.existsByEmail(anyString())).thenReturn(false);
        when(customerRepository.existsByPhone(anyString())).thenReturn(false);
        when(customerMapper.toEntity(any(CustomerCreateRequestDTO.class))).thenReturn(customer);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        when(customerMapper.toResponseDTO(any(Customer.class))).thenReturn(customerResponseDTO);

        // Call the service method
        CompletableFuture<CustomerResponseDTO> createdCustomer = customerService.createCustomer(customerRequest);

        // Assertions
        assertNotNull(createdCustomer);
        assertEquals("John Doe", createdCustomer.join().getName());
        assertEquals("john.doe@example.com", createdCustomer.join().getEmail());
        assertEquals("123456789", createdCustomer.join().getPhone());

        // Verify interactions
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void shouldGetAllCustomers() {
        List<Customer> customerList = new ArrayList<>();
        customerList.add(customer);

        when(customerRepository.findAll()).thenReturn(customerList);
        when(customerMapper.toResponseDTO(any(Customer.class))).thenReturn(customerResponseDTO);

        CompletableFuture<List<CustomerResponseDTO>> customers = customerService.getAllCustomers();

        assertNotNull(customers);
        assertEquals(1, customers.join().size());
        assertEquals("John Doe", customers.join().get(0).getName());

        verify(customerRepository, times(1)).findAll();
    }

    @Test
    void shouldGetCustomerById() {
        when(customerRepository.findById(any(UUID.class))).thenReturn(Optional.of(customer));
        when(customerMapper.toResponseDTO(any(Customer.class))).thenReturn(customerResponseDTO);

        CompletableFuture<CustomerResponseDTO> foundCustomer = customerService.getCustomerById(customer.getId());

        assertNotNull(foundCustomer);
        assertEquals(customer.getId(), foundCustomer.join().getId());

        verify(customerRepository, times(1)).findById(customer.getId());
    }

    @Test
    void shouldUpdateCustomerSuccessfully() {
        CustomerUpdateRequestDTO customerUpdateRequest = new CustomerUpdateRequestDTO();
        customerUpdateRequest.setName("Jane Doe");
        customerUpdateRequest.setEmail("jane.doe@example.com");

        when(customerRepository.findById(any(UUID.class))).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        when(customerMapper.toResponseDTO(any(Customer.class))).thenReturn(customerResponseDTO);

        CompletableFuture<CustomerResponseDTO> updatedCustomer = customerService.updateCustomerById(customer.getId(), customerUpdateRequest);

        assertNotNull(updatedCustomer);
        assertEquals("John Doe", updatedCustomer.join().getName());  // This will match the original name

        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void shouldDeleteCustomerSuccessfully() {
        when(customerRepository.existsById(any(UUID.class))).thenReturn(true);

        CompletableFuture<String> deleteResult = customerService.deleteCustomerById(customer.getId());

        assertNotNull(deleteResult);
        assertEquals("Customer deleted successfully.", deleteResult.join());

        verify(customerRepository, times(1)).deleteById(customer.getId());
    }

    @Test
    void shouldThrowExceptionWhenCreatingDuplicateCustomer() {
        CustomerCreateRequestDTO customerRequest = new CustomerCreateRequestDTO();
        customerRequest.setName("John Doe");
        customerRequest.setEmail("john.doe@example.com");
        customerRequest.setPhone("123456789");

        // Mock duplicate customer existence
        when(customerRepository.existsByName(anyString())).thenReturn(true);

        assertThrows(CustomerAlreadyExistsException.class, () -> customerService.createCustomer(customerRequest).join());

        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void shouldThrowExceptionWhenCustomerNotFoundById() {
        when(customerRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> customerService.getCustomerById(UUID.randomUUID()).join());

        verify(customerRepository, times(1)).findById(any(UUID.class));
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentCustomer() {
        when(customerRepository.existsById(any(UUID.class))).thenReturn(false);

        assertThrows(CustomerNotFoundException.class, () -> customerService.deleteCustomerById(UUID.randomUUID()).join());

        verify(customerRepository, never()).deleteById(any(UUID.class));
    }
}
