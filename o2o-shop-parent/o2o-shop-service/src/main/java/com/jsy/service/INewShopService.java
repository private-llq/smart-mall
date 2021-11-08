package com.jsy.service;

import com.jsy.domain.NewShop;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jsy.parameter.NewShopParam;

/**
 * <p>
 * 新——店铺表 服务类
 * </p>
 *
 * @author yu
 * @since 2021-11-08
 */
public interface INewShopService extends IService<NewShop> {
    //创建店铺
    void addNewShop(NewShopParam shopPacketParam);
}
