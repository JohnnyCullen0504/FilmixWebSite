package com.hhu.filmix.dto.roomDTO.request;

import com.hhu.filmix.enumeration.RoomType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EditRoomRequest {
    @Schema(description = "影厅id")
    @NotNull(message = "影厅id不能为空")
    private Long roomId;

    @Schema(description = "影厅名称")
    private String name;


    @Schema(description = "座位总行数")
    private Integer rows;

    @Schema(description = "座位总列数")
    private Integer columns;

    @Schema(description = "影厅类型")
    private RoomType roomType;

    @Schema(description = "是否支持3D")
    private Boolean support3D;
}
