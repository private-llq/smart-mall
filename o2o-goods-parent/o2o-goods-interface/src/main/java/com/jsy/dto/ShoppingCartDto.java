package com.jsy.dto;
import com.jsy.domain.ShoppingCart;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCartDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 商品总数量
     */
    private Integer sumGoods;

    /**
     * 商品原价
     */
    private BigDecimal sumPrice;

    /**
     * 支付价格
     */
    private BigDecimal payPrice;


    /**
     * 商品共优惠多少
     */
    private BigDecimal discountsPrice;


    /**
     * 购物车
     */
    private List<ShoppingCartListDto> cartList=new ArrayList<>();

    /**
     * 店铺名称
     */
    private String shopName;

}
