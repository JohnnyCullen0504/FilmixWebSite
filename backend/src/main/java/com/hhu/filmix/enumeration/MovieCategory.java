package com.hhu.filmix.enumeration;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
@Getter
public enum MovieCategory implements BaseEnum<String> {
    COMEDY("Comedy","喜剧"),
    ADVENTURE("Adventure","冒险"),
    MYSTERY("Mystery","悬疑"),
    THRILLER("Thriller","惊悚"),
    HORROR("Horror","恐怖"),
    WAR("War","战争"),
    ROMANCE("Romance","爱情"),
    ACTION("Action","动作"),
    SCI_FI("Sci-Fi","科幻"),
    CRIME("Crime","犯罪"),
    BIOPIC("Biopic","传记"),
    ANIMATED("Animated","动画"),
    UNKNOWN("Unknown","未知");
    private String name;
    private String ZH_name;
    @JsonValue
    public String getName(){
        return name;
    }
    MovieCategory(String name,String ZH_name){
        this.name = name;
        this.ZH_name = ZH_name;
    }

    public static MovieCategory MovieCategory(String name){
        for(MovieCategory movieCategory: MovieCategory.values()){
            if (movieCategory.getName().equals(name)){
                return movieCategory;
            }
        }
        return MovieCategory.UNKNOWN;
    }

    @Override
    public String getValue() {
        return name;
    }
}
