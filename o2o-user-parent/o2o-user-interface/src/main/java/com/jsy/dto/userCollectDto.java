package com.jsy.dto;
import com.jsy.basic.util.PageInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class userCollectDto implements Serializable {

    /**
     * 商品返回对象
     */
    private List<GoodsDto> goodsDto=new ArrayList<>();


    /**
     * 服务返回对象
     */
    private List<GoodsServiceDto> goodsServiceDto=new ArrayList<>();

    /**
     * 套餐返回对象
     */
    private List<SetMenuDto> setMenuDto=new ArrayList<>();

    /**
     * 商家返回对象
     */
    private List<NewShopDto> newShopDto=new ArrayList<>();


}
