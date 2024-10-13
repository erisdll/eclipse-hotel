package com.erika.eclipse_hotel.service.mapper;

import com.erika.eclipse_hotel.dto.room.RoomCreateRequestDTO;
import com.erika.eclipse_hotel.dto.room.RoomResponseDTO;
import com.erika.eclipse_hotel.entity.Room;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoomMapper {
    Room toEntity(RoomCreateRequestDTO roomCreateRequestDTO);

    RoomResponseDTO toResponseDTO(Room room);
}
