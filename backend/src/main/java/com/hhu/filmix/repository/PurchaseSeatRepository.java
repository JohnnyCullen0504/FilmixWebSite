package com.hhu.filmix.repository;

import com.hhu.filmix.entity.PurchaseSeat;

import java.util.List;
import java.util.Optional;

public interface PurchaseSeatRepository {
    void insertPurchaseSeat(List<PurchaseSeat> purchaseSeats);
    List<PurchaseSeat> findValidSeatByTicketId(Long ticketId);

    Optional<PurchaseSeat> findByTicketAndSeatAndIsValid(Long ticketId, Integer row, Integer column);
    List<PurchaseSeat> findByPurchaseId(Long purchaseId);

    void invalidate(Long purchaseId);
}
