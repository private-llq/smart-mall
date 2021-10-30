package com.jsy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsy.domain.ShopOwner;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lijin
 * @since 2020-11-12
 */
@Repository
public interface ShopOwnerMapper extends BaseMapper<ShopOwner> {


    @Select("select * from t_shop_owner where relation_uuid =#{uuid}")
    ShopOwner getByRelationUid(String uuid);
}
