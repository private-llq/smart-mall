package com.jsy.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jsy.basic.util.exception.JSYException;
import com.jsy.basic.util.utils.CurrentUserHolder;
import com.jsy.basic.util.utils.UUIDUtils;
import com.jsy.basic.util.vo.UserEntity;
import com.jsy.domain.UserAddress;
import com.jsy.mapper.UserAdressMapper;
import com.jsy.service.IUserAddressService;
import com.jsy.vo.UserAddressVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * @author yu
 */

@Service
public class UserAddressServiceImpl extends ServiceImpl<UserAdressMapper, UserAddress> implements IUserAddressService {

    @Autowired
    private UserAdressMapper userAdressMapper;

    @Override
    @Transactional
    public void save(UserAddressVO userAddressVO) {
        UserEntity userEntity= CurrentUserHolder.getUserEntity();
        if(Objects.isNull(userEntity)){
            throw new JSYException(-1,"用户信息验证失败");
        }
        UserAddress userAddress = new UserAddress();
        BeanUtils.copyProperties(userAddressVO,userAddress);

        userAddress.setUserUuid(userEntity.getUid());
        userAddress.setUuid(UUIDUtils.getUUID());

       if (userAddress.getIsdefult()!=null&&userAddress.getIsdefult()==1){
           userAdressMapper.updateOrtherDefult(userAddress.getUserUuid());
           userAdressMapper.insert(userAddress);
           return;
       }
       userAddress.setIsdefult(0);
       userAdressMapper.insert(userAddress);

    }


    @Transactional
    public void updateByUuId(UserAddressVO userAddressVO) {

        UserAddress userAddress = new UserAddress();
        BeanUtils.copyProperties(userAddressVO,userAddress);
        UserEntity userEntity= CurrentUserHolder.getUserEntity();
        if(Objects.isNull(userEntity)){
            throw new JSYException(-1,"用户信息验证失败");
        }
        UserAddress byUuid = this.getByUuid(userAddress.getUuid());
        if (Objects.isNull(byUuid)){
            throw new JSYException(-1,"用户地址不存在");
        }
        userAddress.setUserUuid(userEntity.getUid());
        if (userAddress.getIsdefult()!=null&&userAddress.getIsdefult()==1){
            userAdressMapper.updateOrtherDefult(userAddress.getUserUuid());
        }
        userAdressMapper.update(userAddress,new QueryWrapper<UserAddress>().eq("uuid",userAddress.getUuid()));
    }


    @Override
    public List<UserAddress> queryByUserUuid() {

        UserEntity userEntity= CurrentUserHolder.getUserEntity();
        if(Objects.isNull(userEntity)){
            throw new JSYException(-1,"用户信息验证失败");
        }
        return baseMapper.selectList(new QueryWrapper<UserAddress>().eq("user_uuid",userEntity.getUid()).orderByDesc("isdefult","id"));
    }

    @Override
    public UserAddress getByUuid(String uuid){
        return userAdressMapper.getByUuid(uuid);
    }
}
