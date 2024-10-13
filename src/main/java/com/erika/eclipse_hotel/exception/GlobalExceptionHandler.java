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
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(Exception exception) {
        return buildResponse("Internal Server Error", "An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Validation exceptions
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        log.error("Validation error: {}", exception.getMessage());
        return buildResponse("Validation Error", Objects.requireNonNull(exception.getBindingResult().getFieldError()).getDefaultMessage(), HttpStatus.BAD_REQUEST);
    }

    // Customer exceptions
    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleCustomerNotFoundException(CustomerNotFoundException exception) {
        return buildResponse("Not Found", exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomerAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleCustomerAlreadyExistsException(CustomerAlreadyExistsException exception) {
        return buildResponse("Customer Already Exists", exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // Room exceptions
    @ExceptionHandler(RoomNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleRoomNotFoundException(RoomNotFoundException exception) {
        return buildResponse("Not Found", exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RoomAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleRoomAlreadyExistsException(RoomAlreadyExistsException exception) {
        return buildResponse("Room Already Exists", exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RoomNotAvailableException.class)
    public ResponseEntity<ErrorResponseDTO> handleRoomNotAvailableException(RoomNotAvailableException exception) {
        return buildResponse("Room Already Booked", exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // Reservation exceptions
    @ExceptionHandler(ReservationCreationException.class)
    public ResponseEntity<ErrorResponseDTO> handleReservationCreationException(ReservationCreationException exception) {
        return buildResponse("Reservation Creation Error", exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ReservationDateIntervalException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidDateIntervalException(ReservationDateIntervalException exception) {
        return buildResponse("Invalid Date Interval", exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ReservationStateException.class)
    public ResponseEntity<ErrorResponseDTO> handleReservationStateException(ReservationStateException exception) {
        return buildResponse("Reservation State Error", exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ErrorResponseDTO> buildResponse(String error, String message, HttpStatus status) {
        ErrorResponseDTO response = new ErrorResponseDTO(error, message, status.value());
        return new ResponseEntity<>(response, status);
    }
}

