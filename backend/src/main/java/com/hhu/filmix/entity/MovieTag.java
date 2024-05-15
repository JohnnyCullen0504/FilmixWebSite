package com.hhu.filmix.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieTag {
    private Long id;
    //标签名称
    private String name;
    private String nameZH;
}
