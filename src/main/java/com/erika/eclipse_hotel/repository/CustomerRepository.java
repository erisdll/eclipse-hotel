package com.erika.eclipse_hotel.repository;

import com.erika.eclipse_hotel.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {

}
