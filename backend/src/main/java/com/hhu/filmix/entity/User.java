package com.hhu.filmix.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hhu.filmix.enumeration.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;
    private String name;
    private String email;
    private String phone;
    @JsonIgnore
    private String password;
    private UserType type;
}
