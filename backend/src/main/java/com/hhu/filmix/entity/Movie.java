package com.hhu.filmix.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hhu.filmix.enumeration.Language;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Movie {
    //影片id
    private Long id;
    private String name;
    //影片时长（min）
    private Integer duration;
    //上映时间
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseTime;
    //海报图
    private String posterURL;
    private Language language;
    //片源地
    private String source;
    private Boolean deleted;
}
