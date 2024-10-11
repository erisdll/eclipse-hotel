package com.erika.eclipse_hotel.controller;

import com.erika.eclipse_hotel.dto.ReservationRequestDTO;
import com.erika.eclipse_hotel.dto.ReservationResponseDTO;
import com.erika.eclipse_hotel.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

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
    public ResponseEntity<ReservationResponseDTO> closeRegistration(@PathVariable Long id) {
        ReservationResponseDTO closedReservation = reservationService.closeReservation(id);
        return ResponseEntity.ok(closedReservation);
    };

    @GetMapping("/by-interval")
    public ResponseEntity<List<ReservationResponseDTO>> getReservationsByInterval(
            @RequestParam("from") String from, @RequestParam("to") String to) {
        LocalDate fromDate = LocalDate.parse(from);
        LocalDate toDate = LocalDate.parse(to);

        List<ReservationResponseDTO> reservationsByInterval = reservationService.getReservationsByInterval(fromDate, toDate);
    }
}
