package com.erika.eclipse_hotel.controller;

import com.erika.eclipse_hotel.dto.room.RoomCreateRequestDTO;
import com.erika.eclipse_hotel.dto.room.RoomResponseDTO;
import com.erika.eclipse_hotel.dto.room.RoomUpdateRequestDTO;
import com.erika.eclipse_hotel.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @PostMapping
    public CompletableFuture<ResponseEntity<RoomResponseDTO>> createRoom(@RequestBody @Valid RoomCreateRequestDTO roomCreateRequestDTO) {
        return roomService.createRoom(roomCreateRequestDTO)
                .thenApply(roomResponseDTO -> {
                    UUID roomId = roomResponseDTO.getId();
                    return ResponseEntity
                            .created(URI.create("/api/rooms/" + roomId))
                            .body(roomResponseDTO);
                });
    }

    @GetMapping
    public CompletableFuture<ResponseEntity<List<RoomResponseDTO>>> getAllRooms() {
        return roomService.getAllRooms().thenApply(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<RoomResponseDTO>> getRoomById(@PathVariable UUID id) {
        return roomService.getRoomById(id).thenApply(ResponseEntity::ok);
    }

    @PatchMapping("/{id}")
    public CompletableFuture<ResponseEntity<RoomResponseDTO>> updateRoomById(
            @PathVariable UUID id,
            @RequestBody
            @Valid
            RoomUpdateRequestDTO roomUpdateRequestDTO) {
        return roomService.updateRoomById(id, roomUpdateRequestDTO).thenApply(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<String>> deleteRoomById(@PathVariable UUID id) {
        return roomService.deleteRoomById(id).thenApply(ResponseEntity::ok);
    }

    @GetMapping("/currently-booked")
    public CompletableFuture<ResponseEntity<List<RoomResponseDTO>>> getBookedRooms() {
        return roomService.findBookedRooms().thenApply(ResponseEntity::ok);
    }
}
