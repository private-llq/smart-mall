package com.jsy.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author lijin
 * @since 2020-11-12
 */
@EqualsAndHashCode(callSuper = false)
@TableName("t_shop_evaluation")
public class ShopEvaluation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 店铺id
     */
    private Long shopId;

    /**
     * 订单信息
     */
    private String orderMessage;

    /**
     * 评价信息
     */
    private String evaluateMessage;

    /**
     * 评价星级，1星，2星，3星，4星，5星
     */
    private String evaluateLevel;

    public ShopEvaluation() {
    }


    public Long getId() {
        return this.id;
    }

    public Long getShopId() {
        return this.shopId;
    }

    public String getOrderMessage() {
        return this.orderMessage;
    }

    public String getEvaluateMessage() {
        return this.evaluateMessage;
    }

    public String getEvaluateLevel() {
        return this.evaluateLevel;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public void setOrderMessage(String orderMessage) {
        this.orderMessage = orderMessage;
    }

    public void setEvaluateMessage(String evaluateMessage) {
        this.evaluateMessage = evaluateMessage;
    }

    public void setEvaluateLevel(String evaluateLevel) {
        this.evaluateLevel = evaluateLevel;
    }

    public String toString() {
        return "ShopEvaluation(id=" + this.getId() + ", shopId=" + this.getShopId() + ", orderMessage=" + this.getOrderMessage() + ", evaluateMessage=" + this.getEvaluateMessage() + ", evaluateLevel=" + this.getEvaluateLevel() + ")";
    }
}
