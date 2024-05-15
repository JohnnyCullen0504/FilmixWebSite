package com.hhu.filmix.dto.movieDTO.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MovieBriefInfo {
    @Schema(description = "电影id",type = "string")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long movieId;
    @Schema(description = "名称")
    private String name;
    @Schema(description = "海报URL")
    private String posterURL;


}
