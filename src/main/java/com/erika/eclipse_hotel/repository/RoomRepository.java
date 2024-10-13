package com.erika.eclipse_hotel.repository;

import com.erika.eclipse_hotel.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoomRepository extends JpaRepository<Room, UUID> {
    boolean existsByRoomNumber(String roomNumber);
}
