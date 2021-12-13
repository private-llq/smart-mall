package com.jsy.service;

import com.jsy.basic.util.PageInfo;
import com.jsy.domain.UserAddr;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jsy.dto.UserAddrDto;
import com.jsy.mapper.UserAddrMapper;
import com.jsy.param.UserAddrParam;
import com.jsy.query.UserAddrQuery;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

/**
 * <p>
 * 用户地址管理表	 服务类
 * </p>
 *
 * @author yu
 * @since 2021-11-22
 */
public interface IUserAddrService extends IService<UserAddr> {


    /**
     * 用户添加地址
     * @param userAddrParam
     * @return
     */
    void addUserAddr(UserAddrParam userAddrParam);

    /**
     * 用户修改地址
     * @param userAddrParam
     * @return
     */
    void updateUserAddr(UserAddrParam userAddrParam);


    /**
     * 地址分页列表
     *
     * @param userAddrQuery 查询对象
     * @return  分页对象
     */
    PageInfo<UserAddrDto> UserAddrPageList(UserAddrQuery userAddrQuery);

    /**
     * 用户删除地址
     * @param id
     */
    void deleteUserAddr(Long id);


    /**
     * 查询店铺到用户所在位置的距离
     * @param shopId
     * @return
     */
    String getDistance(Long shopId, BigDecimal userLongitude, BigDecimal userLatitude);

    /**
     * 用户的设置默认地址  默认地址 1 默认 0 否
     */
    void setUserAddr(Long id, Integer state);
}
