package com.hhu.filmix.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.hhu.filmix.api.ApiCode;
import com.hhu.filmix.api.ApiResult;
import com.hhu.filmix.api.Page;
import com.hhu.filmix.dto.PutResponseDTO;
import com.hhu.filmix.dto.purchaseDTO.response.PurchaseBriefInfo;
import com.hhu.filmix.dto.purchaseDTO.response.PurchaseDetailInfo;
import com.hhu.filmix.dto.purchaseSeat.PurchaseSeatInfo;
import com.hhu.filmix.entity.Purchase;
import com.hhu.filmix.entity.PurchaseSeat;
import com.hhu.filmix.entity.Ticket;
import com.hhu.filmix.enumeration.OrderStatus;
import com.hhu.filmix.repository.PurchaseRepository;
import com.hhu.filmix.repository.PurchaseSeatRepository;
import com.hhu.filmix.repository.TicketRepository;
import com.hhu.filmix.service.PurchaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional//保证原子性
public class PurchaseServiceImpl implements PurchaseService {
    private PurchaseRepository purchaseRepository;
    private TicketRepository ticketRepository;
    private PurchaseSeatRepository purchaseSeatRepository;
    @Override
    public ApiResult<?> generateOrder(Long ticketId, List<PurchaseSeatInfo> purchaseSeatInfoList) {
        Ticket ticket = ticketRepository.findById(ticketId).orElse(null);
        if(ticket == null){
            return ApiResult.fail(ApiCode.NOTFOUND,"该电影排期不存在");
        }
        if(ticket.getShowTime().isBefore(LocalDateTime.now()))
            return ApiResult.fail(ApiCode.UNABLE,"已过期限，无法购买");
        if(purchaseSeatInfoList.isEmpty())
            return ApiResult.fail(ApiCode.MISSING_ARGUMENT,"必须至少选择一个座位购买");

        for(PurchaseSeatInfo psi : purchaseSeatInfoList){
            if(purchaseSeatRepository.findByTicketAndSeatAndIsValid(ticketId,psi.getRow(),psi.getColumn()).isPresent()){
                return ApiResult.fail(ApiCode.RESOURCE_CONFLICT,String.format("座位%s排%s列已经售出",psi.getRow().toString(),psi.getColumn().toString()));
            }
        }

        Purchase newPurchase = new Purchase(
                null,
                StpUtil.getLoginIdAsLong(),
                ticketId,
                OrderStatus.Unpaid,//订单状态设置为未付款状态
                ticket.getPrice().multiply(BigDecimal.valueOf(purchaseSeatInfoList.size())),
                LocalDateTime.now());
        Long purchaseId = purchaseRepository.insertPurchase(newPurchase);
        purchaseSeatRepository.insertPurchaseSeat(
                purchaseSeatInfoList.stream().map(purchaseSeatInfo -> {
                    return new PurchaseSeat(
                            null,
                            purchaseId,
                            ticketId,
                            purchaseSeatInfo.getRow(),
                            purchaseSeatInfo.getColumn(),
                            true);
                }).collect(Collectors.toList())
        );
        return ApiResult.data("购票成功", PutResponseDTO.phrase(purchaseId));
    }

    @Override
    public ApiResult<?> refundTicket(Long purchaseId) {
        Purchase purchase = purchaseRepository.findPurchaseById(purchaseId).orElse(null);
        long userId = StpUtil.getLoginIdAsLong();
        if(purchase == null){
            return ApiResult.fail(ApiCode.NOTFOUND,"无该购票记录");
        }
        if(!purchase.getUserId().equals(userId)){
            return ApiResult.fail(ApiCode.UNABLE,"当前订单不属于该用户");
        }
        if (!purchase.getStatus().equals(OrderStatus.Paid)){
            return ApiResult.fail(ApiCode.UNABLE,"该状态下的订单无法退票");
        }
        purchase.setStatus(OrderStatus.Refunded);
        purchaseRepository.update(purchase);
        purchaseSeatRepository.invalidate(purchase.getId());
        return ApiResult.success("退票成功");
    }

    @Override
    public ApiResult<Page<PurchaseBriefInfo>> queryPurchaseRecord(Integer pageSize, Integer currentPage) {
        Long userId = StpUtil.getLoginIdAsLong();
        Page<PurchaseBriefInfo> purchases = purchaseRepository.findUserPurchases(userId,pageSize,currentPage);
        return ApiResult.data(purchases);
    }

    @Override
    public ApiResult<PurchaseDetailInfo> queryPurchaseRecordDetail(Long purchaseId) {
        Purchase purchase = purchaseRepository.findPurchaseById(purchaseId).orElse(null);
        if(purchase == null){
            return ApiResult.fail(ApiCode.NOTFOUND,"该订单不存在");
        }
        if (!purchase.getUserId().equals(StpUtil.getLoginIdAsLong())){
            return ApiResult.fail(ApiCode.NOT_ALLOWED,"无权查询他人订单");
        }

        PurchaseDetailInfo purchaseDetailInfo = purchaseRepository.findPurchaseDetail(purchaseId).orElse(null);
        if(purchaseDetailInfo == null){
            return ApiResult.fail(ApiCode.NOT_ALLOWED,"无详细交易记录");
        }
        return ApiResult.data(purchaseDetailInfo);
    }

    @Override
    public ApiResult<?> paidOrder(Long purchaseId) {
       Purchase purchase =  purchaseRepository.findPurchaseById(purchaseId).orElse(null);
       if(purchase == null){
           return ApiResult.fail(ApiCode.NOTFOUND,"订单不存在");
       }

       //检查订单是否属于当前用户
       Long userID = StpUtil.getLoginIdAsLong();
       if(!purchase.getUserId().equals(userID)){
           return ApiResult.fail(ApiCode.NOT_ALLOWED,"该订单不属于当前用户");
       }

       if(!purchase.getStatus().equals(OrderStatus.Unpaid)){
           return ApiResult.fail(ApiCode.UNABLE,"当前订单无需支付");
       }

       purchase.setStatus(OrderStatus.Paid);
       purchaseRepository.update(purchase);

       return ApiResult.success("订单状态变更为已支付");
    }

    @Override
    @Transactional
    public ApiResult<?> closeOrder(Long purchaseId) {
        Purchase purchase =  purchaseRepository.findPurchaseById(purchaseId).orElse(null);
        if(purchase == null){
            return ApiResult.fail(ApiCode.NOTFOUND,"订单不存在");
        }

        //检查订单是否属于当前用户
        Long userID = StpUtil.getLoginIdAsLong();
        if(!purchase.getUserId().equals(userID)){
            return ApiResult.fail(ApiCode.NOT_ALLOWED,"该订单不属于当前用户");
        }
        //校验是否能够关闭
        if(!purchase.getStatus().equals(OrderStatus.Unpaid)){
            return ApiResult.fail(ApiCode.UNABLE,"仅未支付订单可关闭");
        }
        purchase.setStatus(OrderStatus.Closed);

        //取消座位
        purchaseSeatRepository.invalidate(purchaseId);
        //保存订单
        purchaseRepository.update(purchase);

        return ApiResult.success("订单已关闭");
    }

    public PurchaseServiceImpl(PurchaseRepository purchaseRepository,
                               TicketRepository ticketRepository,
                               PurchaseSeatRepository purchaseSeatRepository) {
        this.purchaseRepository = purchaseRepository;
        this.ticketRepository  = ticketRepository;
        this.purchaseSeatRepository = purchaseSeatRepository;
    }
}
