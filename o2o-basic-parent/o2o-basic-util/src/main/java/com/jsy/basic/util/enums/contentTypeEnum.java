package com.jsy.basic.util.enums;

public enum contentTypeEnum {
    PNG("png","application/x-png"),
    JPG("jpg","application/x-jpg"),
    JPEG("jpeg","image/jpeg");

    private  String tags;
    private  String type;

    contentTypeEnum(String tags, String type) {
        this.tags = tags;
        this.type = type;
    }

    public static String getType(String tags) {
        contentTypeEnum[] contentTypeEnums = values();
        for (contentTypeEnum contentTypeEnum : contentTypeEnums) {
            if (contentTypeEnum.getTags().equals(tags)) {
                return contentTypeEnum.getType();
            }
        }
        return null;
    }
    public String getTags() {
        return tags;
    }

    public String getType() {
        return type;
    }
}
