package com.hhu.filmix.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "新建实体通用返回封装体")
public class PutResponseDTO {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    public static PutResponseDTO phrase(Long id){
        return new PutResponseDTO(id);
    }
}
