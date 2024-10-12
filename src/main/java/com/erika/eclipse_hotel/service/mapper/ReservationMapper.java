package com.erika.eclipse_hotel.service.mapper;

import com.erika.eclipse_hotel.dto.ReservationRequestDTO;
import com.erika.eclipse_hotel.dto.ReservationResponseDTO;
import com.erika.eclipse_hotel.entity.Reservation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReservationMapper {
    Reservation toEntity(ReservationRequestDTO reservationRequestDTO);
    ReservationResponseDTO toResponseDTO(Reservation reservation);
}
