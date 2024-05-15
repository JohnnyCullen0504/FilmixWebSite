package com.hhu.filmix.dtoMapper;

import com.hhu.filmix.dto.purchaseSeat.PurchaseSeatInfo;
import com.hhu.filmix.entity.PurchaseSeat;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PurchaseSeatDTOMapper {
    PurchaseSeatInfo toPurchaseSeatInfo(PurchaseSeat purchaseSeat);
}
