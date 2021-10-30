package com.jsy.dto;
import com.jsy.domain.Cart;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * <p>
 * </p>
 * @author lijin
 * @since 2020-11-23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "Cart",description = "购物车实体类")
public class CartDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 商品总价优惠之后最终价格
     */
    @ApiModelProperty(value = "商品总价")
    private BigDecimal sumPriceShow;


    /**
     * 商品总数量
     */
    @ApiModelProperty(value = "商品总数量")
    private Integer sumGoods;


    /**
     * //商品实付总价
     */
    @ApiModelProperty(value = "商品实付总价")
    private BigDecimal sumPrice;


    /**
     * 满减了多少
     */
    @ApiModelProperty(value = "商品满减优惠了多少")
    private BigDecimal subtractPrice;



    @ApiModelProperty(value = "购物车")
    private List<Cart> cartList=new ArrayList<>();


    /**
     *  0：普通商品 1：秒杀商品
     */
    @ApiModelProperty(value = "0：普通商品 1：秒杀商品")
    private Integer killGoods=0;


    /**
     * 活动Map
     */
    @ApiModelProperty(value = "新客立减、满减、红包（券类） 金额")
    private Map<String,BigDecimal> hashMap=new HashMap<>();

    /**
     * 其他费用(包含包装费)
     */
    @ApiModelProperty(value = "其他费用")
    private List<SelectGoodsOtherCostByGoodsUuidDto> otherMoney=new ArrayList<>();

    /**
     * 配送费
     */
    @ApiModelProperty(value = "其他费用")
    private BigDecimal distributionMoney;


}
