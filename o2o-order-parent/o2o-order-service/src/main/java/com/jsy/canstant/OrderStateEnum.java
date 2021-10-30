package com.jsy.canstant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 国民探花余公子
 * @ClassName OrderStateEnum
 * @Date 2021-05-27  11:31
 * @Description TODO
 * @Version 1.0
 **/
public interface OrderStateEnum {


    Map<String,List<Map<String, Object>>> sourceMap = new HashMap<>();

    enum OrderStateFromEnum {
        OrderState_FROM_ALL("全部订单", 1),
        OrderState_FROM_WAIT_PAYED("待付款", 2),
        //ORDER_STATE_FROM_PAYED("已付款",3),
        ORDER_STATE_FROM_USED("待使用", 4),
        ORDER_STATE_FROM_EVALUATION("待评价", 5),
        ORDER_STATE_FROM_EVALUATED("已评价",6),
        ORDER_STATE_FROM_BACKING("退款中",7),
        ORDER_STATE_FROM_FINISH_BACK("退款成功",8),
        ORDER_STATE_FROM_REFUSE_BACK("拒绝退款",9),
        ORDER_STATE_FROM_OTHER("退款/售后",10),
        ORDER_STATE_FROM_RETURN("其他", 11);
        private String name;
        private Integer index;

        OrderStateFromEnum(String name, Integer index) {
            this.name = name;
            this.index = index;
        }
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getIndex() {
            return index;
        }

        public void setIndex(Integer index) {
            this.index = index;
        }

        @Override
        public String toString() {
            return this.index+"_"+this.name;
        }

        public static final List<Map<String, Object>> orderFromList = new ArrayList<>();
        public static final Map<Integer, String> orderFromMap = new HashMap<>();
        static {
            for(OrderStateFromEnum orderStateFromEnum : OrderStateFromEnum.values()){
                HashMap<String, Object> map = new HashMap<>();
                map.put("index", orderStateFromEnum.getIndex());
                map.put("name", orderStateFromEnum.getName());
                orderFromList.add(map);
                orderFromMap.put(orderStateFromEnum.getIndex(), orderStateFromEnum.getName());
            }
            sourceMap.put("orderFrom",orderFromList);
        }

        public static String getCode(Integer code){
            OrderStateFromEnum[] values = OrderStateFromEnum.values();
            for(OrderStateFromEnum c : values){
                if( c.index.equals(code) ){
                    return c.name;
                }
            }
            return null;
        }

    }
}