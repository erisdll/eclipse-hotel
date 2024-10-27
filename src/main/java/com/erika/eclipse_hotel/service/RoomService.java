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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RoomService {

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Async
    public CompletableFuture<RoomResponseDTO> createRoom(RoomCreateRequestDTO request) {
        log.info("Trying to create new room: {}", request);

        if (roomRepository.existsByRoomNumber(request.getRoomNumber())) {
            log.error("Room with number {} already exists.", request.getRoomNumber());
            throw new RoomAlreadyExistsException("Room with number " + request.getRoomNumber() + " already exists.");
        }

        Room room = roomMapper.toEntity(request);
        room = roomRepository.save(room);

        log.info("Room created successfully. ID: {}", room.getId());
        return CompletableFuture.completedFuture(roomMapper.toResponseDTO(room));
    }

    @Async
    public CompletableFuture<List<RoomResponseDTO>> getAllRooms() {
        log.info("Trying to fetch all rooms.");
        List<Room> rooms = roomRepository.findAll();
        log.info("Found {} rooms.", rooms.size());

        return CompletableFuture.completedFuture(rooms.stream()
                .map(roomMapper::toResponseDTO)
                .collect(Collectors.toList()));
    }

    @Async
    public CompletableFuture<RoomResponseDTO> getRoomById(UUID id) {
        log.info("Trying to fetch room. ID: {}", id);
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RoomNotFoundException("Room not found."));
        log.info("Room found. ID: {}", id);

        return CompletableFuture.completedFuture(roomMapper.toResponseDTO(room));
    }

    @Async
    public CompletableFuture<RoomResponseDTO> updateRoomById(UUID id, RoomUpdateRequestDTO request) {
        log.info("Trying to update room. ID: {}", id);
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RoomNotFoundException("Room not found."));

        // Update only non-null fields from the DTO
        if (request.getRoomNumber() != null) {
            room.setRoomNumber(request.getRoomNumber());
        }
        if (request.getType() != null) {
            room.setType(request.getType());
        }
        if (request.getPrice() != null) {
            room.setPrice(new BigDecimal(request.getPrice()));
        }

        Room updatedRoom = roomRepository.save(room);
        log.info("Room updated successfully. ID: {}", id);
        return CompletableFuture.completedFuture(roomMapper.toResponseDTO(updatedRoom));
    }

    @Async
    public CompletableFuture<String> deleteRoomById(UUID id) {
        log.info("Trying to delete room. ID: {}", id);
        if (!roomRepository.existsById(id)) {
            log.error("Room not found. ID: {}", id);
            throw new RoomNotFoundException("Room not found.");
        }
        roomRepository.deleteById(id);
        log.info("Room deleted successfully. ID: {}", id);

        return CompletableFuture.completedFuture("Room deleted successfully.");
    }

    @Async
    @Transactional(readOnly = true)
    public CompletableFuture<List<RoomResponseDTO>> findBookedRooms() {
        LocalDateTime now = LocalDateTime.now();
        log.info("Trying to fetch currently booked rooms.");

        List<Reservation> currentReservations = reservationRepository.findByStatusOrCheckInBefore(ReservationStatus.IN_USE, now);
        log.info("Found {} rooms currently booked.", currentReservations.size());

        return CompletableFuture.completedFuture(currentReservations.stream()
                .map(Reservation::getRoom)
                .map(roomMapper::toResponseDTO)
                .collect(Collectors.toList()));
    }
}
