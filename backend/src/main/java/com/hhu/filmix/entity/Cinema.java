package com.hhu.filmix.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Cinema {
    private Long id;
    private String name;
    private String address;
    private Boolean deprecated;
}
