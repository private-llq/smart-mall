package com.jsy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsy.domain.UserAddress;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

/**
 * @author yu
 */
@Component
public interface UserAdressMapper extends BaseMapper<UserAddress> {

    /**
     * 将之前默认地址的修改为普通地址
     */
    void updateOrtherDefult(String userUuid);


    @Select("select * from t_user_address where uuid = #{uuid}")
    UserAddress getByUuid(@Param("uuid") String uuid);
}
