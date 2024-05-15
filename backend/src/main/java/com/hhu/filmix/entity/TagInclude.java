package com.hhu.filmix.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagInclude {
    private Long movieId;
    private Long tagId;
}
