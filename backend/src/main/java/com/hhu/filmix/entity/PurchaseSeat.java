package com.hhu.filmix.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 座位购买记录
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseSeat {
    //针对一个订单的特定记录编号
    private Long id;
    //订单id
    private Long purchaseId;
    //排期id
    private Long ticketId;
    private Integer row;
    private Integer column;
    //当前座位的购买记录是否有效
    private Boolean valid;
}
