package com.jsy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsy.domain.OrderDistribution;
import org.springframework.stereotype.Component;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yu
 * @since 2020-11-14
 */
@Component
public interface OrderDistributionMapper extends BaseMapper<OrderDistribution> {


    Long getByOId(Long orderId);

    int delByOrderId(Long id);
}
