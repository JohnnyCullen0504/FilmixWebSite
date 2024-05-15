package com.hhu.filmix.dto.ticketDTO.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewTicketRequest {

    @NotNull(message = "电影id不能为空")
    @Schema(description = "电影id")
    private Long movieId;

    @NotNull(message = "开始时间不能为空")
    @Schema(description = "开始时间",pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime showTime;

    @NotNull(message = "散场时间不能为空")
    @Schema(description = "散场时间",pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endTime;

    @NotNull(message = "影院id不能为空")
    @Schema(description = "影院id")
    private Long cinemaId;

    @NotNull(message = "影厅id不能为空")
    @Schema(description = "影厅id")
    private Long roomId;

    @NotNull(message = "价格不能为空")
    @Schema(description = "价格")
    private BigDecimal price;
}
