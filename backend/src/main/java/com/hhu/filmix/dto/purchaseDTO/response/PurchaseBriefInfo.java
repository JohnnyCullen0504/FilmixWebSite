package com.hhu.filmix.dto.purchaseDTO.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.hhu.filmix.enumeration.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseBriefInfo {
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "订单id",type = "string")
    private Long purchaseId;
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "排期id",type = "string")
    private Long ticketId;
    @Schema(description = "电影名")
    private String movieName;
    @Schema(description = "影院名")
    private String cinemaName;
    @Schema(description = "订单状态")
    private OrderStatus status;
    @Schema(description = "电影开始时间",pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime showTime;
    @Schema(description = "交易金额",pattern = "yyyy-MM-dd HH:mm")
    private BigDecimal price;
}
