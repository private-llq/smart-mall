package com.jsy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jsy.domain.CommonRedpacket;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lijin
 * @since 2020-11-20
 */
public interface ICommonRedpacketService extends IService<CommonRedpacket> {

    void saveAndUpdate(CommonRedpacket commonRedpacket);

    void deleteById(String id);

    CommonRedpacket grant(String uuid);

    CommonRedpacket getByUuid(String uuid);

    Map<String, CommonRedpacket> getMapByUuid(List<String> uuids);
}
