package com.hhu.filmix.dto.MovieTagDTO.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieTagDTO {
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "标签编号",type = "string")
    private Long tagId;
    @Schema(description = "标签名（英文）")
    private String name;
    @Schema(description = "标签名（中文）")
    public  String nameZH;
}
