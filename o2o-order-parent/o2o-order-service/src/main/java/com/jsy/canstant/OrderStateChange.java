package com.jsy.canstant;

/**
 * @author 国民探花余公子
 * @ClassName OrderStateChange
 * @Date 2021-05-27  11:48
 * @Description TODO
 * @Version 1.0
 **/
public class OrderStateChange {
    public OrderStateChange() {
    }

    public static Integer OrderStateChangeInt(String payState,String used,String evaluationId) {
        //待付款
        if ("0".equals(payState)) {
            //System.out.println("待付款");
            return 2;
        }
        //已付款
        if ("1".equals(payState)){
            if ("0".equals(used)){
                //System.out.println("待使用");
                return 4;
            }
            if ("1".equals(used)){
                if ("1".equals(evaluationId)){
                    //System.out.println("已评论");
                    return 6;
                }
                //System.out.println("已使用,未评论");
                return 5;
            }
            //System.out.println("已付款");
            return 3;
        }
        if ("2".equals(payState)){
            //System.out.println("退款中");
            return 7;
        }
        if ("3".equals(payState)){
            //System.out.println("退款成功");
            return 8;
        }
        if ("4".equals(payState)){
            //System.out.println("拒绝退款");
            return 9;
        }
        return 1;
    }
    //全部：1，代付款：2，待使用：3，待评价：4，退款/售后：5；

    /**
     *
     * @param orderState 传入的参数
     * @param state 根据订单状态判断出的前台展示状态
     * @return
     */
    public static boolean selection(Integer orderState,Integer state){
        /**
         * OrderState_FROM_ALL("全部订单", 1),
         *         OrderState_FROM_WAIT_PAYED("待付款", 2),
         *         //ORDER_STATE_FROM_PAYED("已付款",3),
         *         ORDER_STATE_FROM_USED("待使用", 4),
         *         ORDER_STATE_FROM_EVALUATION("待评价", 5),
         *         ORDER_STATE_FROM_EVALUATED("已评价",6),
         *         ORDER_STATE_FROM_BACKING("退款中",7),
         *         ORDER_STATE_FROM_FINISH_BACK("退款成功",8),
         *         ORDER_STATE_FROM_REFUSE_BACK("拒绝退款",9),
         *         ORDER_STATE_FROM_OTHER("退款/售后",10),
         *         ORDER_STATE_FROM_RETURN("其他", 11);
         */
        //全部：1
        if (orderState.equals(1)){
            return true;
        }
        //代付款：2
        if (orderState.equals(2)){
            if (state.equals(2)){
                return true;
            }
            return false;
        }
        //待使用：3
        if (orderState.equals(3)){
            if (state.equals(4)){
                return true;
            }
            return false;
        }
        //待评价：4
        if (orderState.equals(4)){
            if (state.equals(5)){
                return true;
            }
            return false;
        }
        //退款/售后：5
        if (orderState.equals(5)){
            if (state.equals(7)||state.equals(8)||state.equals(9)){
                return true;
            }
            return false;
        }
        return true;
    }

}