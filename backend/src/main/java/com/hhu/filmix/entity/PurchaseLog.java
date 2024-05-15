package com.hhu.filmix.entity;

import com.hhu.filmix.enumeration.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 记录订单变化日志
 * 由于课程作业设计需要，底层逻辑在数据库中由触发器完成
 * 实际生产时，记录日志由后端完成
 * **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseLog {
    private Long LogId;
    private Long purchaseId;
    //日志记录时间
    private LocalDateTime changeAt;
    private OrderStatus from;
    private OrderStatus to;
    //日志备注
    private String notes;
}
