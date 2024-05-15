package com.hhu.filmix.dto.purchaseSeat;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PurchaseSeatInfo {
    @NotNull(message = "行不能为空")
    @Schema(description = "行")
    private Integer row;
    @NotNull(message = "列不能为空")
    @Schema(description = "列")
    private Integer column;
}
