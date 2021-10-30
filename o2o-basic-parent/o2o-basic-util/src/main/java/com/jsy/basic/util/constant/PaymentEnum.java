package com.jsy.basic.util.constant;

/**
 * @author 国民探花余公子
 * @ClassName PaymentEnum
 * @Date 2021-05-21  15:19
 * @Description TODO
 * @Version 1.0
 **/
public interface PaymentEnum {

    /**
     *@Author: Pipi
     *@Description: 银联支付短信模板编码
     *@Param: null:
     *@Return:
     *@Date: 2021/4/12 17:10
     **/
    enum SmsTmpltCode {
        COMMON("COMMON", "1"),
        REGIST("REGIST", "2"),
        ACTIVATE("ACTIVATE", "3"),
        TRADE("TRADE", "4");

        private String name;
        private String index;

        SmsTmpltCode(String name, String index) {
            this.name = name;
            this.index = index;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIndex() {
            return index;
        }

        public void setIndex(String index) {
            this.index = index;
        }

        public static String getName(String index) {
            for (SmsTmpltCode value : SmsTmpltCode.values()) {
                if (index.equals(value.getIndex())) {
                    return value.getName();
                }
            }
            return null;
        }
    }
}