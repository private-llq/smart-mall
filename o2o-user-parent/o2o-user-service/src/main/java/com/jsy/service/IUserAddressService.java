package com.jsy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jsy.domain.UserAddress;
import com.jsy.vo.UserAddressVO;

import java.util.List;

/**
 * @author yu
 */
public interface IUserAddressService extends IService<UserAddress> {



    void save(UserAddressVO entity);
    

    List<UserAddress> queryByUserUuid();


    UserAddress getByUuid(String uuid);

    void updateByUuId(UserAddressVO userAddressVO);
}
