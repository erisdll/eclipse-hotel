package com.erika.eclipse_hotel.service;

import com.erika.eclipse_hotel.dto.RoomRequestDTO;
import com.erika.eclipse_hotel.dto.RoomResponseDTO;
import com.erika.eclipse_hotel.entity.Reservation;
import com.erika.eclipse_hotel.entity.Room;
import com.erika.eclipse_hotel.enums.ReservationStatus;
import com.erika.eclipse_hotel.repository.ReservationRepository;
import com.erika.eclipse_hotel.repository.RoomRepository;
import com.erika.eclipse_hotel.service.mapper.RoomMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RoomService {

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    public RoomResponseDTO createRoom(RoomRequestDTO roomRequestDTO) {
        Room room = roomMapper.toEntity(roomRequestDTO);
        room = roomRepository.save(room);

        return roomMapper.toResponseDTO(room);
    }

    public List<RoomResponseDTO> getAllRooms() {
        List<Room> rooms = roomRepository.findAll();

        return rooms.stream()
                .map(roomMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public RoomResponseDTO getRoomById(UUID id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Room not found. ID: " + id));
        return roomMapper.toResponseDTO(room);
    }

    public RoomResponseDTO updateRoomById(UUID id, RoomRequestDTO roomRequestDTO) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Room not found. ID: " + id));

        room.setRoomNumber(roomRequestDTO.getRoomNumber());
        room.setType(roomRequestDTO.getType());
        room.setPrice(new BigDecimal(roomRequestDTO.getPrice()));

        Room updatedRoom = roomRepository.save(room);
        return  roomMapper.toResponseDTO(updatedRoom);
    }

    public void deleteRoomById(UUID id) {
        if (!roomRepository.existsById(id)) {
            throw new EntityNotFoundException("Customer not found. ID: " + id);
        }
        roomRepository.deleteById(id);
    }

    public List<RoomResponseDTO> getOccupiedRooms() {
        return null;
    }
}
