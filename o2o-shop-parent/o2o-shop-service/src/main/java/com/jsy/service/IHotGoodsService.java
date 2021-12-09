package com.jsy.service;

import com.jsy.basic.util.PageInfo;
import com.jsy.domain.HotGoods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jsy.dto.NewShopHotDto;
import com.jsy.query.NewShopQuery;

import java.util.List;

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
 /**
  * @author Tian
  * @since 2021/12/9-11:52
  * @description 根据店铺id彻底删除热门数据表
  **/
    Boolean delHotGoods(Long goodsId);
    /**
     * @author Tian
     * @since 2021/12/9-11:52
     * @description 根据店铺id彻底删除热门数据表
     **/
    Boolean delHotShop(Long shopId);
 /**
  * @author Tian
  * @since 2021/12/9-13:40
  * @description 热根据商品id查询是否是门数据
  **/
    HotGoods getHotGoods(Long goodsId);
 /**
  * @author Tian
  * @since 2021/12/9-13:40
  * @description 热根据店铺id查询是否是门数据
  **/
    HotGoods getHotShop(Long shopId);
 /**
  * @author Tian
  * @since 2021/12/9-13:48
  * @description 查询所有热门数据
  **/
    List<HotGoods> getHotList();
}
