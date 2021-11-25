package com.jsy.dto;
import com.jsy.basic.util.PageInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class userCollectDto implements Serializable {

    /**
     * 商品返回对象
     */
    private PageInfo<GoodsDto> goodsDto;


    /**
     * 服务返回对象
     */
    private PageInfo<GoodsServiceDto> goodsServiceDto;

    /**
     * 套餐返回对象
     */
    private PageInfo<SetMenuDto> setMenuDto;

    /**
     * 商家返回对象
     */
    private PageInfo<NewShopDto> newShopDto;


}
