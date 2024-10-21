package com.erika.eclipse_hotel.service;

import com.erika.eclipse_hotel.dto.reservation.ReservationRequestDTO;
import com.erika.eclipse_hotel.dto.reservation.ReservationResponseDTO;
import com.erika.eclipse_hotel.entity.Customer;
import com.erika.eclipse_hotel.entity.Reservation;
import com.erika.eclipse_hotel.entity.Room;
import com.erika.eclipse_hotel.enums.ReservationStatus;
import com.erika.eclipse_hotel.exception.reservation.*;
import com.erika.eclipse_hotel.exception.room.RoomNotAvailableException;
import com.erika.eclipse_hotel.repository.CustomerRepository;
import com.erika.eclipse_hotel.repository.ReservationRepository;
import com.erika.eclipse_hotel.repository.RoomRepository;
import com.erika.eclipse_hotel.service.mapper.ReservationMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private ReservationMapper reservationMapper;

    @InjectMocks
    private ReservationService reservationService;

    private Reservation reservation;
    private Customer customer;
    private Room room;
    private ReservationResponseDTO reservationResponseDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock Customer, Room, and Reservation objects
        customer = new Customer();
        customer.setId(UUID.randomUUID());

        room = new Room();
        room.setId(UUID.randomUUID());
        room.setRoomNumber("101");

        reservation = new Reservation();
        reservation.setId(UUID.randomUUID());
        reservation.setCustomer(customer);
        reservation.setRoom(room);
        reservation.setCheckIn(LocalDateTime.now().plusDays(1));
        reservation.setCheckOut(LocalDateTime.now().plusDays(3));
        reservation.setStatus(ReservationStatus.SCHEDULED);

        // Mock reservation response DTO
        reservationResponseDTO = new ReservationResponseDTO();
        reservationResponseDTO.setId(reservation.getId());
        reservationResponseDTO.setCustomerId(customer.getId().toString());
        reservationResponseDTO.setRoomId(room.getId().toString());
    }

    @Test
    void shouldCreateReservationSuccessfully() {
        // Prepare mock request
        ReservationRequestDTO request = new ReservationRequestDTO();
        request.setCustomerId(customer.getId().toString());
        request.setRoomId(room.getId().toString());
        request.setCheckIn("01/01/2099 14:00:00");
        request.setCheckOut("10/01/2099 12:00:00");

        // Mock repository and mapper behavior
        when(customerRepository.findById(any(UUID.class))).thenReturn(Optional.of(customer));
        when(roomRepository.findById(any(UUID.class))).thenReturn(Optional.of(room));
        when(reservationRepository.findConflictingReservations(any(UUID.class), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(new ArrayList<>());
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
        when(reservationMapper.toResponseDTO(any(Reservation.class))).thenReturn(reservationResponseDTO);

        CompletableFuture<ReservationResponseDTO> createdReservation = reservationService.createReservation(request);

        assertNotNull(createdReservation);
        assertEquals(customer.getId().toString(), createdReservation.join().getCustomerId());
        assertEquals(room.getId().toString(), createdReservation.join().getRoomId());

        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    void shouldCloseReservationSuccessfully() {
        // Mock reservation in use
        reservation.setStatus(ReservationStatus.IN_USE);
        when(reservationRepository.findById(any(UUID.class))).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
        when(reservationMapper.toResponseDTO(any(Reservation.class))).thenReturn(reservationResponseDTO);

        CompletableFuture<ReservationResponseDTO> closedReservation = reservationService.closeReservation(reservation.getId());

        assertNotNull(closedReservation);
        assertEquals(reservation.getId(), closedReservation.join().getId());

        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    void shouldFindReservationsByIntervalSuccessfully() {
        List<Reservation> reservations = List.of(reservation);

        when(reservationRepository.findByCheckInBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(reservations);
        when(reservationMapper.toResponseDTO(any(Reservation.class))).thenReturn(reservationResponseDTO);

        CompletableFuture<List<ReservationResponseDTO>> foundReservations = reservationService
                .findReservationsByInterval("15/10/2024", "18/10/2024");

        assertNotNull(foundReservations);
        assertEquals(1, foundReservations.join().size());

        verify(reservationRepository, times(1)).findByCheckInBetween(any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    void shouldThrowExceptionWhenCreatingReservationWithConflictingDates() {
        // Prepare mock request with conflicting dates
        ReservationRequestDTO request = new ReservationRequestDTO();
        request.setCustomerId(customer.getId().toString());
        request.setRoomId(room.getId().toString());
        request.setCheckIn("01/01/2099 14:00:00");
        request.setCheckOut("01/01/2099 12:00:00");

        // Call the service method and expect an exception
        assertThrows(ReservationDateIntervalException.class, () -> reservationService.createReservation(request).join());
    }

    @Test
    void shouldThrowExceptionWhenRoomIsNotAvailable() {
        // Prepare mock request with valid date interval
        ReservationRequestDTO request = new ReservationRequestDTO();
        request.setCustomerId(customer.getId().toString());
        request.setRoomId(room.getId().toString());
        request.setCheckIn("01/01/2099 14:00:00");
        request.setCheckOut("10/01/2099 12:00:00");

        // Mock room availability
        when(customerRepository.findById(any(UUID.class))).thenReturn(Optional.of(customer));
        when(roomRepository.findById(any(UUID.class))).thenReturn(Optional.of(room));
        when(reservationRepository.findConflictingReservations(any(UUID.class), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(reservation));  // Conflicting reservation

        // Call the service method and expect a RoomNotAvailableException
        assertThrows(RoomNotAvailableException.class, () -> reservationService.createReservation(request).join());

        // Verify that the repository was checked for conflicting reservations but no save operation occurred
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void shouldThrowExceptionWhenClosingNonInUseReservation() {
        // Mock reservation not in use
        when(reservationRepository.findById(any(UUID.class))).thenReturn(Optional.of(reservation));

        assertThrows(ReservationStateException.class, () -> reservationService.closeReservation(reservation.getId()).join());

        verify(reservationRepository, never()).save(any(Reservation.class));
    }
}
