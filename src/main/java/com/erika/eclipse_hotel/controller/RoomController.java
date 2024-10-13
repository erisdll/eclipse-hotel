package com.erika.eclipse_hotel.controller;

import com.erika.eclipse_hotel.dto.room.RoomCreateRequestDTO;
import com.erika.eclipse_hotel.dto.room.RoomResponseDTO;
import com.erika.eclipse_hotel.dto.room.RoomUpdateRequestDTO;
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
    public ResponseEntity<RoomResponseDTO> createRoom(@RequestBody @Valid RoomCreateRequestDTO roomCreateRequestDTO) {
        RoomResponseDTO room = roomService.createRoom(roomCreateRequestDTO);
        return ResponseEntity.ok(room);
    }

    @GetMapping
    public ResponseEntity<List<RoomResponseDTO>> getAllRooms() {
        List<RoomResponseDTO> rooms = roomService.getAllRooms();
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomResponseDTO> getRoomById(@PathVariable UUID id) {
        RoomResponseDTO room = roomService.getRoomById(id);
        return ResponseEntity.ok(room);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<RoomResponseDTO> updateRoomById(
            @PathVariable UUID id,
            @RequestBody
            @Valid
            RoomUpdateRequestDTO roomUpdateRequestDTO) {
        RoomResponseDTO responseDTO = roomService.updateRoomById(id, roomUpdateRequestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoomById(@PathVariable UUID id) {
        roomService.deleteRoomById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/currently-booked")
    public ResponseEntity<List<RoomResponseDTO>> getBookedRooms() {
        List<RoomResponseDTO> occupiedRooms = roomService.findBookedRooms();
        return ResponseEntity.ok(occupiedRooms);
    }
}
