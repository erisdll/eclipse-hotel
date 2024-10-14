package com.erika.eclipse_hotel.controller;

import com.erika.eclipse_hotel.dto.reservation.ReservationRequestDTO;
import com.erika.eclipse_hotel.dto.reservation.ReservationResponseDTO;
import com.erika.eclipse_hotel.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @PostMapping("/create")
    public CompletableFuture<ReservationResponseDTO> createReservation(@RequestBody @Valid ReservationRequestDTO reservationRequestDTO) {
        return reservationService.createReservation(reservationRequestDTO);
    }

    @PatchMapping("/{id}/close")
    public CompletableFuture<ResponseEntity<ReservationResponseDTO>> closeRegistration(@PathVariable UUID id) {
        return reservationService.closeReservation(id).thenApply(ResponseEntity::ok);
    }

    @GetMapping("/by-interval")
    public CompletableFuture<ResponseEntity<List<ReservationResponseDTO>>> getReservationsByInterval(@RequestParam("from") String fromDate, @RequestParam("to") String toDate) {
        return reservationService.findReservationsByInterval(fromDate, toDate).thenApply(ResponseEntity::ok);
    }
}
