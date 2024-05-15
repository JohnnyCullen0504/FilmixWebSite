package com.hhu.filmix.repository;

import com.hhu.filmix.entity.PurchaseLog;

import java.util.List;
public interface PurchaseLogRepository {
    List<PurchaseLog> findLogByPurchaseId(Long purchaseId);
}
