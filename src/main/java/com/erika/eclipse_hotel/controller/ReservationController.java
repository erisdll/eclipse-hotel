package com.erika.eclipse_hotel.controller;

import com.erika.eclipse_hotel.dto.ReservationRequestDTO;
import com.erika.eclipse_hotel.dto.ReservationResponseDTO;
import com.erika.eclipse_hotel.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @PostMapping("/")
    public ResponseEntity<ReservationResponseDTO> createReservation(@RequestBody @Valid ReservationRequestDTO reservationRequestDTO) {
        ReservationResponseDTO responseDTO = reservationService.createReservation(reservationRequestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @PatchMapping("/{id}/close")
    public ResponseEntity<ReservationResponseDTO> closeRegistration(@PathVariable UUID id) {
        ReservationResponseDTO closedReservation = reservationService.closeReservation(id);
        return ResponseEntity.ok(closedReservation);
    };

    @GetMapping("/by-interval")
    public ResponseEntity<List<ReservationResponseDTO>> getReservationsByInterval(
            @RequestParam("from") String from, @RequestParam("to") String to) {
        LocalDateTime fromDate = LocalDateTime.parse(from);
        LocalDateTime toDate = LocalDateTime.parse(to);

        List<ReservationResponseDTO> reservationsByInterval = reservationService.findReservationsByInterval(fromDate, toDate);
        return ResponseEntity.ok(reservationsByInterval);
    }
}
