package com.erika.eclipse_hotel.exception;

import com.erika.eclipse_hotel.dto.exception.ErrorResponseDTO;
import com.erika.eclipse_hotel.exception.customer.CustomerAlreadyExistsException;
import com.erika.eclipse_hotel.exception.customer.CustomerNotFoundException;
import com.erika.eclipse_hotel.exception.reservation.ReservationCreationException;
import com.erika.eclipse_hotel.exception.reservation.ReservationDateIntervalException;
import com.erika.eclipse_hotel.exception.reservation.ReservationStateException;
import com.erika.eclipse_hotel.exception.room.RoomAlreadyExistsException;
import com.erika.eclipse_hotel.exception.room.RoomNotAvailableException;
import com.erika.eclipse_hotel.exception.room.RoomNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(Exception ex) {
        return buildResponse("Internal Server Error", "An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Customer exceptions
    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleCustomerNotFoundException(EntityNotFoundException ex) {
        return buildResponse("Not Found", ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomerAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleCustomerAlreadyExistsException(CustomerAlreadyExistsException ex) {
        return buildResponse("Customer Already Exists", ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // Room exceptions
    @ExceptionHandler(RoomNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleRoomNotFoundException(RoomNotFoundException ex) {
        return buildResponse("Not Found", ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RoomAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleRoomAlreadyExistsException(RoomAlreadyExistsException ex) {
        return buildResponse("Room Already Exists", ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponseDTO> handleRoomNotAvailableException(RoomNotAvailableException ex) {
        return buildResponse("Room Already Booked", ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // Reservation exceptions
    @ExceptionHandler(ReservationCreationException.class)
    public ResponseEntity<ErrorResponseDTO> handleReservationCreationException(ReservationCreationException ex) {
        return buildResponse("Reservation Creation Error", ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ReservationDateIntervalException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidDateIntervalException(ReservationDateIntervalException ex) {
        return buildResponse("Invalid Date Interval", ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ReservationStateException.class)
    public ResponseEntity<ErrorResponseDTO> handleReservationStateException(ReservationStateException ex) {
        return buildResponse("Reservation State Error", ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ErrorResponseDTO> buildResponse(String error, String message, HttpStatus status) {
        ErrorResponseDTO response = new ErrorResponseDTO(error, message, status.value());
        return new ResponseEntity<>(response, status);
    }
}

