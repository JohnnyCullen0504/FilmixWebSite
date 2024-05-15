package com.hhu.filmix.enumeration;

public enum UserType implements BaseEnum<String>{
    USER("USER","用户"),
    STAFF("STAFF","门店工作人员"),
    ADMIN("ADMIN","系统管理员");
    private String type;
    private String ZHType;
    UserType(String type,String ZHType){
        this.type = type;
        this.ZHType = ZHType;
    }
    public String type(){
        return this.type;
    }
    public String ZHType(){
        return this.ZHType;
    }

    @Override
    public String getValue() {
        return type;
    }
}
