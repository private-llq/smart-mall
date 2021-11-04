package com.jsy.mapper;

import com.jsy.domain.BusinessFeedback;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yu
 * @since 2021-05-20
 */
public interface BusinessFeedbackMapper extends BaseMapper<BusinessFeedback> {
   // BusinessFeedback selectCreateTime(String info_uuid);

    void updateByOrderId(String order_uuid);

//    void selectByInfoCount(String info_uuid, long now);
}
