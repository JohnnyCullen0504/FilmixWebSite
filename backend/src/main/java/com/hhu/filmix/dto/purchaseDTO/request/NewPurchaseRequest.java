package com.hhu.filmix.dto.purchaseDTO.request;

import com.hhu.filmix.dto.purchaseSeat.PurchaseSeatInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class NewPurchaseRequest {
    @Schema(description = "排期id")
    @NotNull(message = "排期id不能为空")
    private Long ticket_id;

    @NotNull(message = "订单需要包含购买的座位信息")
    @Schema(description = "所需要购买的座位")
    private List<PurchaseSeatInfo> purchaseSeatInfoList;
}
