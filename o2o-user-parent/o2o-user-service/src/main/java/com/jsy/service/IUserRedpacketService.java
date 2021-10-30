package com.jsy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jsy.basic.util.PageList;
import com.jsy.domain.UserRedpacket;
import com.jsy.dto.SelectUserAllRedpacketDto;
import com.jsy.dto.SelectUserNoUserRedPacketDto;
import com.jsy.dto.UserRedpacketDTO;
import com.jsy.query.UserRedpacketQuery;
import com.jsy.vo.ReceiveRedpacketVo;

import java.util.List;


public interface IUserRedpacketService extends IService<UserRedpacket> {

    //新增抢到的红包
    boolean insterRedPacket(UserRedpacket userRedpacket);

    //查询用户在店铺中没有使用的红包
    List<SelectUserNoUserRedPacketDto> selectUserNoUserRedPacket(String shopUuid);

    //根据用户和活动uuid查询已领红包数据
    UserRedpacket selectUserRedpacket(String userActiveUuid);

    //查询用户的红包
    List<SelectUserAllRedpacketDto> selectUserAllRedpacket();

    //根据用户和活动uuid查询已领红包数据集合
    List<UserRedpacket> selectUserRedpacketAll(String activity);


    void receiveRedpacket(ReceiveRedpacketVo receiveRedpacketVo);

    PageList<UserRedpacketDTO> queryByPage(UserRedpacketQuery query);

    UserRedpacket getByUuid(String uuid);

    List<UserRedpacketDTO> queryByShop(String shopUuid);

    void useRedpacket(String uuid);
}
