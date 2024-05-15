package com.hhu.filmix.dto.ticketDTO.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketBriefDTO {
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "排期id")
    private Long ticketId;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "电影id")
    private Long movieId;

    @Schema(description = "电影开始放映时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime showTime;

    @Schema(description = "散场时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endTime;

    @Schema(description = "影院编号")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long cinemaId;
    
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "影厅编号")
    private Long roomId;
    @Schema(description = "价格")
    private BigDecimal price;
    @Schema(description = "排期是否取消")
    private Boolean canceled;
}
