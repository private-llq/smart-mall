package com.jsy.dto;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class userCollectDto implements Serializable {

    /**
     * 商品服务返回对象
     */
    private List<GoodsDto> goodsDto;

    /**
     * 套餐返回对象
     */
    private List<SetMenuDto> setMenuDto;

    /**
     * 商家返回对象
     */
    private List<NewShopDto> newShopDto;

}
