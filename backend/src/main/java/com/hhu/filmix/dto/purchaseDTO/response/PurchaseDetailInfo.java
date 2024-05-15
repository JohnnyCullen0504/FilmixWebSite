package com.hhu.filmix.dto.purchaseDTO.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.hhu.filmix.dto.purchaseSeat.PurchaseSeatInfo;
import com.hhu.filmix.entity.PurchaseLog;
import com.hhu.filmix.enumeration.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseDetailInfo {
    @Schema(description = "订单id",type = "string")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long purchaseId;

    @Schema(description = "排期id",type = "string")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long ticketId;

    private List<PurchaseSeatInfo> purchaseSeats;

    @Schema(description = "电影名")
    private String movieName;

    @Schema(description = "影院名")
    private String cinemaName;

    @Schema(description = "影厅名")
    private String roomName;

    @Schema(description = "订单状态")
    private OrderStatus status;

    @Schema(description = "电影开始时间",pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime showTime;

    @Schema(description = "交易金额")
    private BigDecimal price;

    @Schema(description = "订单日志")
    private List<PurchaseLog> purchaseLogs;

    @Schema(description = "交易时间",pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime tradingTime;
}
