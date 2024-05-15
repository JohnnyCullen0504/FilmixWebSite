package com.hhu.filmix.service;

import com.hhu.filmix.api.ApiResult;
import com.hhu.filmix.api.Page;
import com.hhu.filmix.dto.purchaseDTO.response.PurchaseBriefInfo;
import com.hhu.filmix.dto.purchaseDTO.response.PurchaseDetailInfo;
import com.hhu.filmix.dto.purchaseSeat.PurchaseSeatInfo;

import java.util.List;

public interface PurchaseService {
    ApiResult<?> generateOrder(Long ticketId, List<PurchaseSeatInfo> purchaseSeatInfoList);

    ApiResult<?> refundTicket(Long purchaseId);

    ApiResult<Page<PurchaseBriefInfo>> queryPurchaseRecord(Integer pageSize, Integer currentPage);

    ApiResult<PurchaseDetailInfo> queryPurchaseRecordDetail(Long purchaseId);

    ApiResult<?> paidOrder(Long purchaseId);

    ApiResult<?> closeOrder(Long purchaseId);
}
