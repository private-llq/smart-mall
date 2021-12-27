package com.jsy.service;

import com.jsy.domain.ShopCapital;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jsy.query.AddCapitalParam;

/**
 * <p>
 * 商家余额表 服务类
 * </p>
 *
 * @author arli
 * @since 2021-12-24
 */
public interface IShopCapitalService extends IService<ShopCapital> {
    //增加余额
    Boolean addCapital(AddCapitalParam param);
    //减少余额
    Boolean subtractCapital(AddCapitalParam param);
    //初始化店铺余额
    Boolean initializeShopCapital(Long shopId);
    //查询店铺的余额
    ShopCapital selectShopCapital(Long shopId);
}
