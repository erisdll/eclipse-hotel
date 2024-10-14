package com.erika.eclipse_hotel.exception.reservation;

import jakarta.persistence.EntityNotFoundException;

public class ReservationNotFoundException extends EntityNotFoundException {
    public ReservationNotFoundException(String message) {
        super(message);
    }
}
