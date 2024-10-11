package com.erika.eclipse_hotel.service;

import com.erika.eclipse_hotel.dto.ReservationRequestDTO;
import com.erika.eclipse_hotel.dto.ReservationResponseDTO;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;

public class ReservationService {
    public ReservationResponseDTO createReservation(@Valid ReservationRequestDTO reservationRequestDTO) {
        return null;
    }

    public ReservationResponseDTO closeReservation(Long id) {
        return null;
    }

    public List<ReservationResponseDTO> getReservationsByInterval(LocalDate fromDate, LocalDate toDate) {
        return null;
    }
}
