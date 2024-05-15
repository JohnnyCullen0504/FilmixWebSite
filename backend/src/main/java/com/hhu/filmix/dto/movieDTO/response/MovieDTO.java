package com.hhu.filmix.dto.movieDTO.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.hhu.filmix.dto.MovieTagDTO.response.MovieTagDTO;
import com.hhu.filmix.enumeration.Language;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "电影详细信息",description = "承载电影详细信息")
public class MovieDTO{
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "电影id",type = "string")
    Long movieId;

    @Schema(description = "电影名称")
    String name;

    @Schema(description = "影片时长（min）")
    Integer duration;

    @Schema(description = "电影发行日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate releaseTime;

    @Schema(description = "电影标签")
    List<MovieTagDTO> movieTags;

    @Schema(description = "海报图URL")
    String posterURL;

    @Schema(description = "语言")
    Language language;

    @Schema(description = "语言（英文）")
    String language_EN;

    @Schema(description = "语言（中文）")
    String language_ZH;

    @Schema(description = "片原地")
    String source;
}
