package com.jsy.mapper;

import com.jsy.domain.NewOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsy.dto.SelectShopOrderDto;
import com.jsy.query.SelectAllOrderByBackstageParam;
import com.jsy.query.SelectAllOrderMapperParam;
import com.jsy.query.SelectShopOrderParam;
import com.jsy.vo.SelectAllOrderByBackstageVo;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * <p>
 * 订单表 Mapper 接口
 * </p>
 *
 * @author arli
 * @since 2021-11-15
 */
public interface NewOrderMapper extends BaseMapper<NewOrder> {
     //大后台查询所有订单接口
    List<SelectAllOrderByBackstageVo> selectAllOrderByBackstage(@Param("orderParam") SelectAllOrderMapperParam orderParam);
    // 商家根据转态查询订单(包含逻辑删除)
    List<SelectShopOrderDto> selectShopOrder2(@Param("ShopOrderParam") SelectShopOrderParam param);
}
