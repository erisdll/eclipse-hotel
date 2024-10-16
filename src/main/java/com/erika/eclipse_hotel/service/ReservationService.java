package com.erika.eclipse_hotel.service;

import com.erika.eclipse_hotel.dto.reservation.ReservationRequestDTO;
import com.erika.eclipse_hotel.dto.reservation.ReservationResponseDTO;
import com.erika.eclipse_hotel.entity.Customer;
import com.erika.eclipse_hotel.entity.Reservation;
import com.erika.eclipse_hotel.entity.Room;
import com.erika.eclipse_hotel.enums.ReservationStatus;
import com.erika.eclipse_hotel.exception.reservation.ReservationDateIntervalException;
import com.erika.eclipse_hotel.exception.reservation.ReservationCreationException;
import com.erika.eclipse_hotel.exception.reservation.ReservationNotFoundException;
import com.erika.eclipse_hotel.exception.reservation.ReservationStateException;
import com.erika.eclipse_hotel.exception.room.RoomNotAvailableException;
import com.erika.eclipse_hotel.repository.CustomerRepository;
import com.erika.eclipse_hotel.repository.ReservationRepository;
import com.erika.eclipse_hotel.repository.RoomRepository;
import com.erika.eclipse_hotel.service.mapper.ReservationMapper;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReservationService {

    @Autowired
    private ReservationMapper reservationMapper;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Async
    @Transactional
    public CompletableFuture<ReservationResponseDTO> createReservation(@Valid ReservationRequestDTO request) {
        log.info("Trying to create reservation for room: {} and customer {}",
                request.getRoomId(),
                request.getCustomerId()
        );

        //Parse date strings to LocalDateTime
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime checkIn = LocalDateTime.parse(request.getCheckIn(), formatter);
        LocalDateTime checkOut = LocalDateTime.parse(request.getCheckOut(), formatter);

        //Verify date interval integrity
        if (checkIn.isAfter((checkOut))) {
            log.error("Check-out date must be after check-in date.");
            throw new ReservationDateIntervalException("Check-out date must be after check-in date.");
        }
        if (checkIn.isBefore(LocalDateTime.now())) {
            log.error("Check-in date must be in the future.");
            throw new ReservationDateIntervalException("Check-in date must be in the future.");
        }

        //Verify customer and room validity
        Optional<Customer> customer = customerRepository.findById(UUID.fromString(request.getCustomerId()));
        Optional<Room> room = roomRepository.findById(UUID.fromString(request.getRoomId()));
        if (customer.isEmpty()) {
            log.error("Customer not found.");
            throw new ReservationCreationException("Customer not found.");
        }
        if (room.isEmpty()) {
            log.error("Room not found");
            throw new ReservationCreationException("Room not found");
        }

        //Verify room availability during date interval
        List<Reservation> conflictingReservations = reservationRepository.findConflictingReservations(room.get().getId(), checkIn, checkOut);
        if (!conflictingReservations.isEmpty()) {
            log.warn("Room not available during specified period.");
            throw new RoomNotAvailableException("Room not available during specified period.");
        }

        // Create and save reservation
        Reservation reservation = new Reservation();
        reservation.setCustomer(customer.get());
        reservation.setRoom(room.get());
        reservation.setCheckIn(checkIn);
        reservation.setCheckOut(checkOut);
        reservation.setStatus(ReservationStatus.SCHEDULED);
        Reservation savedReservation = reservationRepository.save(reservation);

        log.info("Reservation created successfully. ID: {}",savedReservation.getId());
        return CompletableFuture.completedFuture(reservationMapper.toResponseDTO(savedReservation));
    }

    @Async
    @Transactional
    public CompletableFuture<ReservationResponseDTO> closeReservation(UUID id) {
        log.info("Trying to close reservation. ID: {}", id);

        // Verify reservation existence and status
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException("Reservation not found"));
        if (reservation.getStatus() != ReservationStatus.IN_USE) {
            log.warn("Only reservations currently in use can be closed.");
            throw new ReservationStateException("Only reservations currently in use can be closed.");
        }

        // Close reservation and save to DB
        reservation.setStatus(ReservationStatus.FINISHED);
        reservationRepository.save(reservation);
        log.info("Reservation closed successfully. ID: {}", id);

        return CompletableFuture.completedFuture(reservationMapper.toResponseDTO(reservation));
    }

    @Async
    @Transactional(readOnly = true)
    public CompletableFuture<List<ReservationResponseDTO>> findReservationsByInterval(String fromDate, String toDate) {
        log.info("Trying to find reservations by date interval. From: {}, to {}", fromDate, toDate);

        // Parse date strings to LocalDateTime and verify interval integrity
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime startDateTime = LocalDate.parse(fromDate, formatter).atStartOfDay();
        LocalDateTime endDateTime = LocalDate.parse(toDate, formatter).atTime(23, 59, 59);
        if (startDateTime.isAfter(endDateTime)) {
            log.error("Invalid date interval. Start date must be before end date.");
            throw new ReservationDateIntervalException("Invalid date interval. Start date must be before end date.");
        }

        // Find reservations by date interval and map them to DTO
        List<Reservation> reservations = reservationRepository.findByCheckInBetween(startDateTime, endDateTime);
        log.info("Reservations found by date interval.");

        return CompletableFuture.completedFuture(reservations.stream()
                .map(reservation -> {
                    return reservationMapper.toResponseDTO(reservation);
                })
                .collect(Collectors.toList()));
    }
}
