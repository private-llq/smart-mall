package com.jsy.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShopEvaluationVo {



    private String uuid;
    /**
     * 店铺id
     */
    private String shopUuid;
    /**
     *
     */
    private String userUuid;
    /**
     * 评价信息
     */
    private String evaluateMessage;

    /**
     * 评价星级，1星，2星，3星，4星，5星
     */
    private Integer evaluateLevel;

    private String orderUuid;

    /**
     * 图片
     */
    private String image;

    /**
     * 姓名
     */
    private String name;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
