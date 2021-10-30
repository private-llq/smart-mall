package com.jsy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsy.domain.GoodsSpec;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lijin
 * @since 2020-11-12
 */
public interface GoodsSpecMapper extends BaseMapper<GoodsSpec> {


    //根据规格的id查询对应的商品的上下架状态
    @Select("select gb.is_marketable\n" +
            "FROM t_goods_spec  as gs  \n" +
            "left JOIN t_goods_basic  as gb on gs.goods_uuid=gb.uuid where  gs.uuid=#{specUUID};")
    public Integer  selectGoodStatu(String specUUID);
}
