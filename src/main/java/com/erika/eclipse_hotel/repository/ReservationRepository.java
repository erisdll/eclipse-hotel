package com.erika.eclipse_hotel.repository;

import com.erika.eclipse_hotel.entity.Reservation;
import com.erika.eclipse_hotel.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation, UUID> {
    List<Reservation> findByStatus(ReservationStatus status);
    List<Reservation> findByCheckInBetween(LocalDateTime intervalBeginning, LocalDateTime intervalEnding);
}
