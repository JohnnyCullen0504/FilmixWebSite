package com.hhu.filmix.dto.roomDTO;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomDTO {
    @Schema(description = "影厅id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long roomId;
    @Schema(description = "所属影院id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long cinemaId;
    @Schema(description = "名称")
    private String name;
    @Schema(description = "座位总行数")
    private Integer rows;
    @Schema(description = "座位总列数")
    private Integer columns;
    @Schema(description = "影厅类型")
    private String roomType;
    @Schema(description = "是否支持3D")
    private Boolean support3D;
}
