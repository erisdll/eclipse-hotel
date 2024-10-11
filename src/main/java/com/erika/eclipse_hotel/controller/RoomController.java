package com.erika.eclipse_hotel.controller;

import com.erika.eclipse_hotel.dto.RoomRequestDTO;
import com.erika.eclipse_hotel.dto.RoomResponseDTO;
import com.erika.eclipse_hotel.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @PostMapping
    public ResponseEntity<RoomResponseDTO> createRoom(@RequestBody @Valid RoomRequestDTO roomRequestDTO) {
        RoomResponseDTO room = roomService.createRoom(roomRequestDTO);
        return ResponseEntity.ok(room);
    }

    @GetMapping
    public ResponseEntity<List<RoomResponseDTO>> getAllRooms() {
        List<RoomResponseDTO> rooms = roomService.getAllRooms();
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomResponseDTO> getRoomById(@PathVariable Long id) {
        RoomResponseDTO room = roomService.getRoomById(id);
        return ResponseEntity.ok(room);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoomResponseDTO> updateRoomById(
            @PathVariable Long id,
            @RequestBody
            @Valid
            RoomRequestDTO roomRequestDTO) {
        RoomResponseDTO responseDTO = roomService.updateRoomById(id, roomRequestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoomById(@PathVariable Long id) {
        roomService.deleteRoomById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/occupied")
    public ResponseEntity<List<RoomResponseDTO>> getOccupiedRooms() {
        List<RoomResponseDTO> occupiedRooms = roomService.getOccupiedRooms();
        return ResponseEntity.ok(occupiedRooms);
    }
}
