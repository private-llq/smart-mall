package com.jsy.service;

import com.jsy.domain.ShopSigns;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 门店招牌表 服务类
 * </p>
 *
 * @author yu
 * @since 2021-03-27
 */
public interface IShopSignsService extends IService<ShopSigns> {

    ShopSigns getShopSigns(String shopUuid);
}
