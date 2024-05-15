package com.hhu.filmix.enumeration;

import lombok.Getter;

@Getter
public enum OrderStatus implements BaseEnum<String>{
    New("New"),
    Unpaid("Unpaid"),
    Paid("Paid"),
    Closed("Closed"),
    Finished("Finished"),
    Refunded("Refunded");
    private String name;
    OrderStatus(String name){
        this.name = name;
    }
    public OrderStatus OrderStatus(String name){
        for(OrderStatus orderStatus :OrderStatus.values()){
            if(orderStatus.getName().equals(name)){
                return orderStatus;
            }
        }
        return null;
    }

    @Override
    public String getValue() {
        return name;
    }
}
