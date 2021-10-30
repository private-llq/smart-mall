package com.jsy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jsy.basic.util.PageList;
import com.jsy.domain.ShopOwner;
import com.jsy.dto.LoginDTO;
import com.jsy.query.ShopOwnerQuery;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lijin
 * @since 2020-11-12
 */
public interface IShopOwnerService extends IService<ShopOwner> {

    PageList<ShopOwner> queryByPage(ShopOwnerQuery query);

    LoginDTO queryShopOwnerLogin(ShopOwner shopOwner);

    String updateShopOwner(ShopOwner shopOwner);

    String addShopOwner(ShopOwner shopOwner);

    ShopOwner getByUuid(String uuid);

    ShopOwner getByRelationUid(String uuid);

    boolean sendSmsPassword(String phoneNumber);

    String loginSms(String phoneNumber, Integer code);
}
