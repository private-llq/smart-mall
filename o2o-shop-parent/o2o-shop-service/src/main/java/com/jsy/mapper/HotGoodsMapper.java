package com.jsy.mapper;

import com.jsy.domain.HotGoods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 热门商品表 Mapper 接口
 * </p>
 *
 * @author yu
 * @since 2021-12-03
 */
public interface HotGoodsMapper extends BaseMapper<HotGoods> {

    void deleteId();
}
