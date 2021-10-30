package com.jsy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsy.domain.ShopAssets;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 商家资产 Mapper 接口
 * </p>
 *
 * @author yu
 * @since 2020-12-17
 */
@Component
public interface ShopAssetsMapper extends BaseMapper<ShopAssets> {

    @Select("select * from t_shop_assets where owner_uuid = #{uuid}")
    ShopAssets getByUUid(String uuid);

    @Update("update t_shop_record set state_id =3 where order_uuid =#{uuid}")
    int backByOuid(String uuid);

    @Select("select t.id," +
            "t.uuid," +
            "t.owner_uuid," +
            "t.alipay," +
            "t.wechatpaty," +
            "t.bankcard," +
            "t.assets  " +
            "from t_shop_assets t where uuid = #{uuid}")
    ShopAssets getByOid(@Param("uuid") String uuid);
}
