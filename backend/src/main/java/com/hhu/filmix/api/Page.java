package com.hhu.filmix.api;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "通用分页返回结果")
@Getter
public class Page<T> {
    @Schema(description = "单页容量")
    private int pageSize;
    @Schema(title = "当前页码",description = "页码从第1页开始")
    private int currentPage;
    @Schema(description = "总页数")
    private int totalPage;
    @Schema(description = "记录")
    private List<T> records;

}
