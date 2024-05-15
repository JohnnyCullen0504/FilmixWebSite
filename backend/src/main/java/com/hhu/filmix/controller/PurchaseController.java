package com.hhu.filmix.controller;

import com.hhu.filmix.annotation.authentication.CheckLogin;
import com.hhu.filmix.api.ApiResult;
import com.hhu.filmix.api.Page;
import com.hhu.filmix.dto.purchaseDTO.request.NewPurchaseRequest;
import com.hhu.filmix.dto.purchaseDTO.response.PurchaseBriefInfo;
import com.hhu.filmix.dto.purchaseDTO.response.PurchaseDetailInfo;
import com.hhu.filmix.service.PurchaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController()
@ApiResponse(useReturnTypeSchema = true)
@Tag(name = "购票Api")
public class PurchaseController {
    private PurchaseService purchaseService;
    @PutMapping("/movie/ticket/purchase")
    @CheckLogin
    @Operation(summary = "新增购票订单")
    public ApiResult<?> purchaseTicket(@Valid@RequestBody NewPurchaseRequest request){
      return purchaseService.generateOrder(request.getTicket_id(),request.getPurchaseSeatInfoList());
    }

    @PutMapping("/movie/ticket/paid")
    @CheckLogin
    @Operation(summary = "支付订单")
    public ApiResult<?> paidTicket(@RequestParam("id")@Parameter(description = "交易记录id")Long purchaseId){
        return purchaseService.paidOrder(purchaseId);
    }
    @PutMapping("/movie/ticket/close")
    @CheckLogin
    @Operation(summary = "取消订单（关闭交易）")
    public ApiResult<?> cancelTicket(@RequestParam("id")@Parameter(description = "交易记录id")Long purchaseId){
        return purchaseService.closeOrder(purchaseId);
    }
    @PostMapping("/movie/ticket/{purchase_id}/refund")
    @CheckLogin
    @Operation(summary = "退票")
    public ApiResult<?> refundTicket(@PathVariable("purchase_id")Long purchaseId){
        return purchaseService.refundTicket(purchaseId);
    }
    @GetMapping("/user/purchase")
    @CheckLogin
    @Operation(summary = "查询用户订单")
    public ApiResult<Page<PurchaseBriefInfo>> purchaseRecords(@RequestParam("pageSize")@Parameter(description = "分页大小") Integer pageSize,
                                                              @RequestParam("currentPage")@Parameter(description = "当前页")Integer currentPage){
        return purchaseService.queryPurchaseRecord(pageSize,currentPage);
    }
    @GetMapping("/user/purchase/{id}")
    @CheckLogin
    @Operation(summary = "查询订单详细信息")
    public ApiResult<PurchaseDetailInfo> purchaseRecord(@PathVariable("id")@Parameter(description = "交易记录id")Long purchaseId){
        return purchaseService.queryPurchaseRecordDetail(purchaseId);
    }


    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }
}
