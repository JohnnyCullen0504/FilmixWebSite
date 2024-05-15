package com.hhu.filmix.entity;

import com.hhu.filmix.enumeration.RoomType;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Room {
    private Long id;
    //影厅所属影院id
    private Long cinemaId;
    private String name;
    //行数
    private Integer rows;
    //列数
    private Integer columns;
    private RoomType roomType;
    private Boolean support3D;
    private Boolean deprecated;
}
