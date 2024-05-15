package com.hhu.filmix.repository;

import com.hhu.filmix.api.Page;
import com.hhu.filmix.dto.purchaseDTO.response.PurchaseBriefInfo;
import com.hhu.filmix.dto.purchaseDTO.response.PurchaseDetailInfo;
import com.hhu.filmix.entity.Purchase;

import java.util.Optional;

public interface PurchaseRepository {
    Long insertPurchase(Purchase purchase);

    Optional<Purchase> findByUserAndTicket(Long userId, Long ticketId);

    void update(Purchase purchase);
    Page<PurchaseBriefInfo> findUserPurchases(Long userId, Integer pageSize, Integer currentPage);

    Optional<Purchase> findPurchaseById(Long purchaseId);

    Optional<PurchaseDetailInfo> findPurchaseDetail(Long purchaseId);
}
