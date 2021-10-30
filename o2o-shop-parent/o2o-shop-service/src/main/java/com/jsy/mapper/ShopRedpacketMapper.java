package com.jsy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsy.domain.ShopRedpacket;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lijin
 * @since 2020-11-12
 */
public interface ShopRedpacketMapper extends BaseMapper<ShopRedpacket> {


    //查询活动的使用金额
    @Select("select ur.money from t_shop_redpacket  as sr  JOIN t_user_redpacket  as ur  on sr.uuid=ur.activitie_uuid  where ur.deleted=2 AND sr.uuid=#{activityUuid};")
    public List<Integer> selectYes(String activityUuid);

    //查询活动的使用金额
    @Select("select ur.money from t_shop_redpacket  as sr  JOIN t_user_redpacket  as ur  on sr.uuid=ur.activitie_uuid  where ur.deleted=1 AND sr.uuid=#{activityUuid};")
    public List<Integer> selectNo(String activityUuid);
}
