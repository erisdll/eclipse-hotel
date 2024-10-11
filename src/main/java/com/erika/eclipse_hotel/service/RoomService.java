package com.erika.eclipse_hotel.service;

import com.erika.eclipse_hotel.dto.RoomRequestDTO;
import com.erika.eclipse_hotel.dto.RoomResponseDTO;
import com.erika.eclipse_hotel.entity.Customer;
import com.erika.eclipse_hotel.entity.Room;
import com.erika.eclipse_hotel.repository.RoomRepository;
import com.erika.eclipse_hotel.service.mapper.RoomMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;
    private RoomMapper roomMapper;

    public RoomResponseDTO createRoom(RoomRequestDTO roomRequestDTO) {
        Room room = roomMapper.toEntity(roomRequestDTO);
        room = roomRepository.save(room);

        return roomMapper.toResponseDTO(room);
    }

    public List<RoomResponseDTO> getAllRooms() {
        List<Room> rooms = roomRepository.findAll();

        return rooms;
    }

    public RoomResponseDTO getRoomById(UUID id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Room not found. ID: " + id));
        return roomMapper.toResponseDTO(room);
    }

    public RoomResponseDTO updateRoomById(RoomRequestDTO roomRequestDTO, UUID id) {

    }

    public void deleteRoomById(UUID id) {
        if (!roomRepository.existsById(id)) {
            throw new EntityNotFoundException("Customer not found. ID: " + id);
        }
        roomRepository.deleteById(id);
    }
    }

}
