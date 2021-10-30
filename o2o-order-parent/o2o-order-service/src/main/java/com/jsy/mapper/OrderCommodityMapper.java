package com.jsy.mapper;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.jsy.domain.OrderCommodity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yu
 * @since 2020-11-12
 */
@Component
public interface OrderCommodityMapper extends BaseMapper<OrderCommodity> {

    List<OrderCommodity> getCommoditysByOid(Long orderId);

    void delCommodityByOid(Long orderId);

    @Delete("delete from t_order_commodity where order_uuid =#{orderUuid}")
    void delCommodityByUid(String orderUuid);
    @Select("select Sum(tc.num) from t_order_commodity tc  ${ew.customSqlSegment} ")
    int monthSales(@Param(Constants.WRAPPER) QueryWrapper queryWrapper);
}
