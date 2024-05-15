package com.hhu.filmix.entity;

import com.hhu.filmix.enumeration.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Purchase {
    private Long id;
    private Long userId;
    private Long ticketId;
    private OrderStatus status;
    private BigDecimal price;
    private LocalDateTime tradingTime;
}
