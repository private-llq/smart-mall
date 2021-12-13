package com.jsy.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsy.basic.util.PageInfo;
import com.jsy.domain.Goods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsy.dto.GoodsServiceDto;
import com.jsy.query.NearTheServiceQuery;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 *  商品表 Mapper 接口
 * </p>
 *
 * @author lijin
 * @since 2021-11-09
 */
@Component
public interface GoodsMapper extends BaseMapper<Goods> {

    Goods latelyGoods(@Param("shopId") Long shopId);

    IPage<GoodsServiceDto> NearTheService(@Param("page") Page<GoodsServiceDto> page,
                                          @Param("query")NearTheServiceQuery nearTheServiceQuery);

    /**
     * 统计一个商品的销量
     */
    Long sumGoodsSales(@Param("goodsId") Long goodsId);
    /**
     * 统计一个服务的销量
     */
    Long sumServiceSales(@Param("serviceId") Long serviceId);

    List<GoodsServiceDto> NearTheService2(@Param("shopQuery") NearTheServiceQuery nearTheServiceQuery);
}
