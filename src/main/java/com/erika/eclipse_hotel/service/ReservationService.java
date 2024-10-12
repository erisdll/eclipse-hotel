package com.erika.eclipse_hotel.service;

import com.erika.eclipse_hotel.dto.ReservationRequestDTO;
import com.erika.eclipse_hotel.dto.ReservationResponseDTO;
import com.erika.eclipse_hotel.entity.Customer;
import com.erika.eclipse_hotel.entity.Reservation;
import com.erika.eclipse_hotel.entity.Room;
import com.erika.eclipse_hotel.enums.ReservationStatus;
import com.erika.eclipse_hotel.repository.CustomerRepository;
import com.erika.eclipse_hotel.repository.ReservationRepository;
import com.erika.eclipse_hotel.repository.RoomRepository;
import com.erika.eclipse_hotel.service.mapper.ReservationMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
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
    public ReservationResponseDTO createReservation(@Valid ReservationRequestDTO reservationRequestDTO) {
        LocalDateTime checkIn = LocalDateTime.parse(reservationRequestDTO.getCheckIn());
        LocalDateTime checkOut = LocalDateTime.parse(reservationRequestDTO.getCheckOut());

        //Following block validates if customer and room exist and if room is available during requested date interval.
        Optional<Customer> customer = customerRepository.findById(UUID.fromString(reservationRequestDTO.getCustomerId()));
        if(customer.isEmpty()) {
            throw new IllegalArgumentException("Customer not found");
        }
        Optional<Room> room = roomRepository.findById(UUID.fromString(reservationRequestDTO.getRoomId()));
        if(room.isEmpty()) {
            throw new IllegalArgumentException("Room not found");
        }
        List<Reservation> conflictingReservations = reservationRepository.findConflictingReservations(
                room.get().getId(), checkIn, checkOut);
        if (!conflictingReservations.isEmpty()) {
            throw new IllegalArgumentException("Room is already occupied for the specified period");
        }

        Reservation reservation = new Reservation();
        reservation.setCustomer(customer.get());
        reservation.setRoom(room.get());
        reservation.setCheckIn(checkIn);
        reservation.setCheckOut(checkOut);
        reservation.setStatus(ReservationStatus.SCHEDULED);

        Reservation savedReservation = reservationRepository.save(reservation);

        return reservationMapper.toResponseDTO(savedReservation);
    }

    @Transactional
    public ReservationResponseDTO closeReservation(UUID id) {
        Optional<Reservation> optionalReservation = reservationRepository.findById(id);

        if (optionalReservation.isEmpty()) {
            throw new IllegalArgumentException("Reservation not found");
        }

        Reservation reservation = optionalReservation.get();

        if (reservation.getStatus() != ReservationStatus.IN_USE) {
            throw new IllegalArgumentException("Only reservations currently in use can be closed");
        }

        reservation.setStatus(ReservationStatus.FINISHED);

        return reservationMapper.toResponseDTO(reservation);
    }

    @Transactional(readOnly = true)
    public List<ReservationResponseDTO> findReservationsByInterval(LocalDateTime fromDate, LocalDateTime toDate) {
        List<Reservation> reservations = reservationRepository.findByCheckInBetween(fromDate, toDate);

        return reservations.stream()
                .map(reservationMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}
