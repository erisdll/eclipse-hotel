package com.erika.eclipse_hotel.repository;

import com.erika.eclipse_hotel.entity.Reservation;
import com.erika.eclipse_hotel.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation, UUID> {
    List<Reservation> findByStatus(ReservationStatus status);
    List<Reservation> findByCheckInBetween(LocalDateTime intervalBeginning, LocalDateTime intervalEnding);

    @Query("SELECT r FROM Reservation r WHERE r.room.id = :roomId " +
            "AND r.checkIn < :checkOut " +
            "AND r.checkOut > :checkIn")
    List<Reservation> findConflictingReservations(
            @Param("roomId") UUID roomId,
            @Param("checkIn") LocalDateTime checkIn,
            @Param("checkOut") LocalDateTime checkOut);
}
