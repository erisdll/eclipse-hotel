package com.erika.eclipse_hotel.exception.room;

import jakarta.persistence.EntityNotFoundException;

public class RoomNotFoundException extends EntityNotFoundException {
    public RoomNotFoundException(String message) {
        super(message);
    }
}
