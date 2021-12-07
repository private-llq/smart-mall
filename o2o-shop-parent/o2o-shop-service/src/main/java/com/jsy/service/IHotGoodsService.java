package com.jsy.service;

import com.jsy.basic.util.PageInfo;
import com.jsy.domain.HotGoods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jsy.dto.NewShopHotDto;
import com.jsy.query.NewShopQuery;

/**
 * <p>
 * 热门商品表 服务类
 * </p>
 *
 * @author yu
 * @since 2021-12-03
 */
public interface IHotGoodsService extends IService<HotGoods> {
 /**
  * @author Tian
  * @since 2021/12/3-15:24
  * @description 热门分类商品
  **/
    PageInfo<HotGoods> getHot(NewShopQuery newShopQuery);
}
