package com.hhu.filmix.dtoMapper;

import com.hhu.filmix.dto.ticketDTO.request.NewTicketRequest;
import com.hhu.filmix.dto.ticketDTO.response.TicketBriefDTO;
import com.hhu.filmix.entity.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TicketDTOMapper {
    @Mapping(source = "id",target = "ticketId")
    TicketBriefDTO toTicketBriefDTO(Ticket ticket);
    Ticket toTicket(NewTicketRequest newTicketRequest);

}
