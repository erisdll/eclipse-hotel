package com.erika.eclipse_hotel.service;

import com.erika.eclipse_hotel.dto.room.RoomCreateRequestDTO;
import com.erika.eclipse_hotel.dto.room.RoomResponseDTO;
import com.erika.eclipse_hotel.dto.room.RoomUpdateRequestDTO;
import com.erika.eclipse_hotel.entity.Reservation;
import com.erika.eclipse_hotel.entity.Room;
import com.erika.eclipse_hotel.enums.ReservationStatus;
import com.erika.eclipse_hotel.exception.room.RoomAlreadyExistsException;
import com.erika.eclipse_hotel.exception.room.RoomNotFoundException;
import com.erika.eclipse_hotel.repository.ReservationRepository;
import com.erika.eclipse_hotel.repository.RoomRepository;
import com.erika.eclipse_hotel.service.mapper.RoomMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private RoomMapper roomMapper;

    @InjectMocks
    private RoomService roomService;

    private Room room;
    private RoomResponseDTO roomResponseDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Initialize mocks

        // Mock Room object
        room = new Room();
        room.setId(UUID.randomUUID());
        room.setRoomNumber("101");
        room.setPrice(new BigDecimal("150.00"));

        // Mock RoomResponseDTO object
        roomResponseDTO = new RoomResponseDTO();
        roomResponseDTO.setId(room.getId());
        roomResponseDTO.setRoomNumber("101");
        roomResponseDTO.setPrice("150.00");
    }

    @Test
    void shouldCreateRoomSuccessfully() {
        // Prepare mock request
        RoomCreateRequestDTO roomRequest = new RoomCreateRequestDTO();
        roomRequest.setRoomNumber("101");
        roomRequest.setPrice("150.00");

        // Mock repository and mapper behavior
        when(roomRepository.existsByRoomNumber(anyString())).thenReturn(false);
        when(roomMapper.toEntity(any(RoomCreateRequestDTO.class))).thenReturn(room);
        when(roomRepository.save(any(Room.class))).thenReturn(room);
        when(roomMapper.toResponseDTO(any(Room.class))).thenReturn(roomResponseDTO);

        // Call the service method
        CompletableFuture<RoomResponseDTO> createdRoom = roomService.createRoom(roomRequest);

        // Assertions
        assertNotNull(createdRoom);
        assertEquals("101", createdRoom.join().getRoomNumber());
        assertEquals("150.00", createdRoom.join().getPrice());

        // Verify interactions
        verify(roomRepository, times(1)).save(any(Room.class));
    }

    @Test
    void shouldGetAllRooms() {
        List<Room> roomList = new ArrayList<>();
        roomList.add(room);

        when(roomRepository.findAll()).thenReturn(roomList);
        when(roomMapper.toResponseDTO(any(Room.class))).thenReturn(roomResponseDTO);

        CompletableFuture<List<RoomResponseDTO>> rooms = roomService.getAllRooms();

        assertNotNull(rooms);
        assertEquals(1, rooms.join().size());
        assertEquals("101", rooms.join().get(0).getRoomNumber());

        verify(roomRepository, times(1)).findAll();
    }

    @Test
    void shouldGetRoomById() {
        when(roomRepository.findById(any(UUID.class))).thenReturn(Optional.of(room));
        when(roomMapper.toResponseDTO(any(Room.class))).thenReturn(roomResponseDTO);

        CompletableFuture<RoomResponseDTO> foundRoom = roomService.getRoomById(room.getId());

        assertNotNull(foundRoom);
        assertEquals(room.getId(), foundRoom.join().getId());

        verify(roomRepository, times(1)).findById(room.getId());
    }

    @Test
    void shouldUpdateRoomSuccessfully() {
        RoomUpdateRequestDTO roomUpdateRequest = new RoomUpdateRequestDTO();
        roomUpdateRequest.setRoomNumber("102");
        roomUpdateRequest.setPrice("200.00");

        when(roomRepository.findById(any(UUID.class))).thenReturn(Optional.of(room));
        when(roomRepository.save(any(Room.class))).thenReturn(room);
        when(roomMapper.toResponseDTO(any(Room.class))).thenReturn(roomResponseDTO);

        CompletableFuture<RoomResponseDTO> updatedRoom = roomService.updateRoomById(room.getId(), roomUpdateRequest);

        assertNotNull(updatedRoom);
        assertEquals("101", updatedRoom.join().getRoomNumber());  // This will match the original number

        verify(roomRepository, times(1)).save(any(Room.class));
    }

    @Test
    void shouldDeleteRoomSuccessfully() {
        when(roomRepository.existsById(any(UUID.class))).thenReturn(true);

        CompletableFuture<String> deleteResult = roomService.deleteRoomById(room.getId());

        assertNotNull(deleteResult);
        assertEquals("Room deleted successfully.", deleteResult.join());

        verify(roomRepository, times(1)).deleteById(room.getId());
    }

    @Test
    void shouldFindBookedRoomsSuccessfully() {
        Reservation reservation = new Reservation();
        reservation.setRoom(room);
        reservation.setStatus(ReservationStatus.IN_USE);
        List<Reservation> reservations = new ArrayList<>();
        reservations.add(reservation);

        when(reservationRepository.findByStatusOrCheckInBefore(any(ReservationStatus.class), any(LocalDateTime.class)))
                .thenReturn(reservations);
        when(roomMapper.toResponseDTO(any(Room.class))).thenReturn(roomResponseDTO);

        CompletableFuture<List<RoomResponseDTO>> bookedRooms = roomService.findBookedRooms();

        assertNotNull(bookedRooms);
        assertEquals(1, bookedRooms.join().size());
        assertEquals("101", bookedRooms.join().get(0).getRoomNumber());

        verify(reservationRepository, times(1)).findByStatusOrCheckInBefore(any(ReservationStatus.class), any(LocalDateTime.class));
    }

    @Test
    void shouldThrowExceptionWhenCreatingDuplicateRoom() {
        RoomCreateRequestDTO roomRequest = new RoomCreateRequestDTO();
        roomRequest.setRoomNumber("101");

        // Mock duplicate room existence
        when(roomRepository.existsByRoomNumber(anyString())).thenReturn(true);

        assertThrows(RoomAlreadyExistsException.class, () -> roomService.createRoom(roomRequest).join());

        verify(roomRepository, never()).save(any(Room.class));
    }

    @Test
    void shouldThrowExceptionWhenRoomNotFoundById() {
        // Mock room non-existence
        when(roomRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(RoomNotFoundException.class, () -> roomService.getRoomById(UUID.randomUUID()).join());

        verify(roomRepository, times(1)).findById(any(UUID.class));
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentRoom() {
        // Mock room non-existence
        when(roomRepository.existsById(any(UUID.class))).thenReturn(false);

        assertThrows(RoomNotFoundException.class, () -> roomService.deleteRoomById(UUID.randomUUID()).join());

        verify(roomRepository, never()).deleteById(any(UUID.class));
    }
}
