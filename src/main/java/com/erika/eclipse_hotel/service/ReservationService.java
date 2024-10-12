package com.erika.eclipse_hotel.service;

import com.erika.eclipse_hotel.dto.reservation.ReservationRequestDTO;
import com.erika.eclipse_hotel.dto.reservation.ReservationResponseDTO;
import com.erika.eclipse_hotel.entity.Customer;
import com.erika.eclipse_hotel.entity.Reservation;
import com.erika.eclipse_hotel.entity.Room;
import com.erika.eclipse_hotel.enums.ReservationStatus;
import com.erika.eclipse_hotel.repository.CustomerRepository;
import com.erika.eclipse_hotel.repository.ReservationRepository;
import com.erika.eclipse_hotel.repository.RoomRepository;
import com.erika.eclipse_hotel.service.mapper.ReservationMapper;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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

    @Transactional
    public ReservationResponseDTO createReservation(@Valid ReservationRequestDTO request) {
        log.info("Attempting to create reservation for room: {} and customer {}",
                request.getRoomId(),
                request.getCustomerId()
        );

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime checkIn = LocalDateTime.parse(request.getCheckIn(), formatter);
        LocalDateTime checkOut = LocalDateTime.parse(request.getCheckOut(), formatter);

        //Following blocks validate if customer and room exist and if room is available during requested date interval.
        Optional<Customer> customer = customerRepository.findById(UUID.fromString(request.getCustomerId()));
        if (customer.isEmpty()) {
            log.error("Customer not found. ID: {}", request.getCustomerId());
            throw new IllegalArgumentException("Customer not found");
        }

        Optional<Room> room = roomRepository.findById(UUID.fromString(request.getRoomId()));
        if (room.isEmpty()) {
            log.error("Room not found. ID: {}", request.getRoomId());
            throw new IllegalArgumentException("Room not found");
        }

        List<Reservation> conflictingReservations = reservationRepository.findConflictingReservations(room.get().getId(), checkIn, checkOut);
        if (!conflictingReservations.isEmpty()) {
            log.warn("Conflict detected. Room already reserved for this period.");
            throw new IllegalArgumentException("Room is already occupied for the specified period");
        }

        //Builds new reservation object if request passes validations.
        Reservation reservation = new Reservation();
        reservation.setCustomer(customer.get());
        reservation.setRoom(room.get());
        reservation.setCheckIn(checkIn);
        reservation.setCheckOut(checkOut);
        reservation.setStatus(ReservationStatus.SCHEDULED);

        Reservation savedReservation = reservationRepository.save(reservation);
        log.info("Reservation created successfully. ID: {}",savedReservation.getId());

        return reservationMapper.toResponseDTO(savedReservation);
    }

    @Transactional
    public ReservationResponseDTO closeReservation(UUID id) {
        log.info("Attempting to close reservation. ID: {}", id);
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));

        if (reservation.getStatus() != ReservationStatus.IN_USE) {
            log.warn("Cannot close reservation: not currently in use. ID: {}", id);
            throw new IllegalArgumentException("Only reservations currently in use can be closed");
        }

        reservation.setStatus(ReservationStatus.FINISHED);
        reservationRepository.save(reservation);
        log.info("Reservation closed successfully. ID: {}", id);

        return reservationMapper.toResponseDTO(reservation);
    }

    @Transactional(readOnly = true)
    public List<ReservationResponseDTO> findReservationsByInterval(String fromDate, String toDate) {
        log.info("Attempting to find reservations by date interval. From: {}, to {}", fromDate, toDate);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate startDate = LocalDate.parse(fromDate, formatter);
        LocalDate endDate = LocalDate.parse(toDate, formatter);
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        List<Reservation> reservations = reservationRepository.findByCheckInBetween(startDateTime, endDateTime);

        log.info("Reservations successfully found by date interval.");

        return reservations.stream()
                .map(reservation -> {
                    return reservationMapper.toResponseDTO(reservation);
                })
                .collect(Collectors.toList());
    }
}
