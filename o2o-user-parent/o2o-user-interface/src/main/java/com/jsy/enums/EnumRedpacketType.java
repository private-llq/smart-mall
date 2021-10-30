package com.jsy.enums;

/**
 * Created by lkl19 on 2019/11/20.
 */
public enum EnumRedpacketType {
    ENUM_COMMON(1,"通用红包"),
    ENUM_SHOP(2,"店铺红包");

    private  Integer value;
    private  String name;

    EnumRedpacketType(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    public static String getName(Integer value) {
        EnumRedpacketType[] enumStockTypes = values();
        for (EnumRedpacketType stockType : enumStockTypes) {
            if (stockType.getValue().equals(value)) {
                return stockType.getName();
            }
        }
        return null;
    }


    public Integer getValue() {
        return this.value;
    }

    public String getName() {
        return this.name;
    }
}
