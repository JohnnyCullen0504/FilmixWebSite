package com.hhu.filmix.enumeration;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum RoomType implements BaseEnum<String>{
    IMAX("IMAX","IMAX"),
    Dolby_Cinema("Dolby-Cinema","杜比影院"),
    Dolby_Atmos("Dolby-Atmos",""),
    DTS("DTS","DTS"),
    CINITY("CINITY","CINITY"),
    Normal("Normal","普通");
    @JsonValue
    private String name;
    private String nameZH;

    RoomType(String name,String nameZH){
        this.name = name;
        this.nameZH = nameZH;
    }
    public static RoomType RoomType(String name){
        for(RoomType roomType :  RoomType.values()){
            if (roomType.getName().equals(name)){
                return roomType;
            }
        }
        return RoomType.Normal;
    }

    @Override
    public String getValue() {
        return name;
    }
}
