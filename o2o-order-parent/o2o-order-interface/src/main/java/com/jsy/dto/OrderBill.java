package com.jsy.dto;

import com.jsy.domain.Order;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shy
 * @ClassName OrderBill
 * @Date 2021-06-18  16:03
 * @Description 用于
 * @Version 1.0
 **/
public class OrderBill {


    @ApiModelProperty("收入")
    private BigDecimal positiveMoney;
    @ApiModelProperty("退款")
    private BigDecimal negativeMoney;
    @ApiModelProperty("订单集合")
    private List<Order> list = new ArrayList<>();

    public BigDecimal getPositiveMoney() {
        return positiveMoney;
    }

    public void setPositiveMoney(BigDecimal positiveMoney) {
        this.positiveMoney = positiveMoney;
    }

    public BigDecimal getNegativeMoney() {
        return negativeMoney;
    }

    public void setNegativeMoney(BigDecimal negativeMoney) {
        this.negativeMoney = negativeMoney;
    }

    public List<Order> getList() {
        return list;
    }

    public void setList(List<Order> list) {
        this.list = list;
    }

    public OrderBill() {
    }


    public OrderBill(BigDecimal positiveMoney, BigDecimal negativeMoney, List<Order> list) {
        this.positiveMoney = positiveMoney;
        this.negativeMoney = negativeMoney;
        this.list = list;
    }
}