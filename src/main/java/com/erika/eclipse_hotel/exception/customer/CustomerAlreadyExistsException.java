package com.erika.eclipse_hotel.exception.customer;

import org.springframework.dao.DataIntegrityViolationException;

public class CustomerAlreadyExistsException extends DataIntegrityViolationException {
    public CustomerAlreadyExistsException(String message) {
        super(message);
    }
}
