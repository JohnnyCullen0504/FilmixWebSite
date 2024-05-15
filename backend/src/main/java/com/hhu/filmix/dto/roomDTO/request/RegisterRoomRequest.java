package com.hhu.filmix.dto.roomDTO.request;

import com.hhu.filmix.enumeration.RoomType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRoomRequest {
    @Schema(description = "影厅名称")
    @NotNull(message = "名称不能为空")
    @NotBlank(message = "名称不能为空" )
    private String name;


    @Schema(description = "所属影院id")
    @NotNull(message = "影院id不能为空")
    private Long cinemaId;

    @NotNull(message = "行数不能为空")
    @Schema(description = "座位总行数")
    private Integer rows;

    @NotNull(message = "列数不能为空")
    @Schema(description = "座位总列数")
    private Integer columns;

    @NotNull(message = "影厅类型不能为空")
    @Schema(description = "影厅类型")
    private RoomType roomType;

    @NotNull(message = "3D属性不能为空")
    @Schema(description = "是否支持3D")
    private Boolean support3D;
}
