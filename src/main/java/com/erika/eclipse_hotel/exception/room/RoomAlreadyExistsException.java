package com.erika.eclipse_hotel.exception.room;

import org.springframework.dao.DataIntegrityViolationException;

public class RoomAlreadyExistsException extends DataIntegrityViolationException {
    public RoomAlreadyExistsException(String message) {
        super(message);
    }
}
