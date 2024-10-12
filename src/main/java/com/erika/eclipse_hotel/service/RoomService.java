package com.erika.eclipse_hotel.service;

import com.erika.eclipse_hotel.dto.room.RoomRequestDTO;
import com.erika.eclipse_hotel.dto.room.RoomResponseDTO;
import com.erika.eclipse_hotel.entity.Reservation;
import com.erika.eclipse_hotel.entity.Room;
import com.erika.eclipse_hotel.enums.ReservationStatus;
import com.erika.eclipse_hotel.repository.ReservationRepository;
import com.erika.eclipse_hotel.repository.RoomRepository;
import com.erika.eclipse_hotel.service.mapper.RoomMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
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

    public RoomResponseDTO createRoom(RoomRequestDTO request) {
        log.info("Attempting to create new room: {}", request);
        Room room = roomMapper.toEntity(request);
        room = roomRepository.save(room);

        log.info("Room created successfully. ID: {}", room.getId());
        return roomMapper.toResponseDTO(room);
    }

    public List<RoomResponseDTO> getAllRooms() {
        log.info("Attempting to fetch all rooms.");
        List<Room> rooms = roomRepository.findAll();
        log.info("Found {} rooms.", rooms.size());

        return rooms.stream()
                .map(roomMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public RoomResponseDTO getRoomById(UUID id) {
        log.info("Attempting to fetch room. ID: {}", id);
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Room not found. ID: " + id));
        log.info("Room found. ID: {}", id);

        return roomMapper.toResponseDTO(room);
    }

    public RoomResponseDTO updateRoomById(UUID id, RoomRequestDTO request) {
        log.info("Attempting to update room. ID: {}", id);
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Room not found. ID: " + id));

        log.debug("Updating room details: {}", request);
        room.setRoomNumber(request.getRoomNumber());
        room.setType(request.getType());
        room.setPrice(new BigDecimal(request.getPrice()));

        Room updatedRoom = roomRepository.save(room);
        return roomMapper.toResponseDTO(updatedRoom);
    }

    public void deleteRoomById(UUID id) {
        log.info("Attempting to delete room. ID: {}", id);
        if (!roomRepository.existsById(id)) {
            log.error("Room not found. ID: {}", id);
            throw new EntityNotFoundException("Room not found. ID: " + id);
        }
        roomRepository.deleteById(id);
        log.info("Room deleted successfully. ID: {}", id);
    }

    @Transactional
    public List<RoomResponseDTO> findBookedRooms() {
        LocalDateTime now = LocalDateTime.now();
        log.info("Attempting to fetch currently booked rooms.");

        List<Reservation> currentReservations = reservationRepository.findByStatus(ReservationStatus.IN_USE);
        log.info("Found {} currently active reservations.", currentReservations.size());

        return currentReservations.stream()
                .map(Reservation::getRoom)
                .map(roomMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}
