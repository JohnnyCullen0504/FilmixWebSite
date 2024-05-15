package com.hhu.filmix.enumeration;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum Language implements BaseEnum<String>{
    EN("EN","English","英语"),
    ZH("ZH","Chinese","汉语"),
    CAN("CAN","Cantonese","粤语"),
    FR("FR","French","法语"),
    ES("ES","Spanish","西班牙语"),
    AR("AR","Arabic","阿拉伯语"),
    JA("JA","Japanese","日语"),
    RU("RU","Russian","俄语"),
    UNKNOWN("UNKNOWN","Unknown","未知");
    @JsonValue
    private String code;
    private String name;
    private String ZH_name;
    Language(String code,String name,String ZH_name){
        this.code = code;
        this.name = name;
        this.ZH_name = ZH_name;
    }
    public static Language Language(String code){
        for(Language language: Language.values()){
            if(language.getCode().equals(code)){
                return language;
            }
        }
        return Language.UNKNOWN;
    }

    @Override
    public String getValue() {
        return code;
    }
}
