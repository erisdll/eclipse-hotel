package com.erika.eclipse_hotel.repository;

import com.erika.eclipse_hotel.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
