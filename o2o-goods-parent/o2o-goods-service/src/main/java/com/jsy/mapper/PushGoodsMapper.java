package com.jsy.mapper;

import com.jsy.domain.PushGoods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsy.query.PushGoodsQuery;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lijin
 * @since 2021-12-06
 */
@Component
public interface PushGoodsMapper extends BaseMapper<PushGoods> {
    /**
     * 统计一个产品销量
     */
   Long sumGoodsSales(@Param("goodsId") Long goodsId);

    List<PushGoods> pageListPushGoods(@Param("query") PushGoodsQuery pushGoodsQuery);
}
