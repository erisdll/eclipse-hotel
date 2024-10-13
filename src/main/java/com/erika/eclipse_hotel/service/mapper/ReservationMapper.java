package com.erika.eclipse_hotel.service.mapper;

import com.erika.eclipse_hotel.dto.reservation.ReservationRequestDTO;
import com.erika.eclipse_hotel.dto.reservation.ReservationResponseDTO;
import com.erika.eclipse_hotel.entity.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReservationMapper {
    Reservation toEntity(ReservationRequestDTO reservationRequestDTO);

    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "roomId", source = "room.id")
    @Mapping(target = "customerName", source = "customer.name")
    @Mapping(target = "roomNumber", source = "room.roomNumber")
    @Mapping(target = "roomType", source = "room.type")
    ReservationResponseDTO toResponseDTO(Reservation reservation);
}
