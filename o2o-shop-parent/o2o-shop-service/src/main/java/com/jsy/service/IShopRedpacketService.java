package com.jsy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jsy.basic.util.PageList;
import com.jsy.domain.ShopRedpacket;
import com.jsy.dto.SelectShopRedpacketByUserDto;
import com.jsy.dto.SelectShopRedpacketDto;
import com.jsy.dto.UserGetRedPacketDto;
import com.jsy.parameter.ShopRedPacketParam;
import com.jsy.query.ShopRedpacketQuery;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lijin
 * @since 2020-11-12
 */
public interface IShopRedpacketService extends IService<ShopRedpacket> {

    //创建红包
    boolean creationRedPack(ShopRedPacketParam shopRedPacketParam);
    //查询店铺红包信息
    SelectShopRedpacketDto selectShopRedpacket(String shopUuid);
    //{用户}查看店铺红包信息
    SelectShopRedpacketByUserDto SelectShopRedpacketByUser(String shopUuid);
    //{用户}领取红包
    UserGetRedPacketDto UserGetRedPacket(String redPacketUuid);


/***************************************************************************************************************/
    ShopRedpacket grant(String uuid);

    void addAndUpdate(ShopRedpacket shopRedpacket);

    void changStatus(String id, Integer status);

    PageList<ShopRedpacket> queryByPage(ShopRedpacketQuery query);

    ShopRedpacket getByUuid(String uuid);

    Map<String, ShopRedpacket> getMapByUuid(List<String> uuids);
}
