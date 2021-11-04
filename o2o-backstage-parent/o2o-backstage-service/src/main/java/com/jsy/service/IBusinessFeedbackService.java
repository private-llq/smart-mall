package com.jsy.service;

import com.jsy.domain.BusinessFeedback;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jsy.dto.BusinessFeedbackDto;
import com.jsy.vo.BusinessFeedbackAppendVo;
import com.jsy.vo.BusinessFeedtypeVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yu
 * @since 2021-05-20
 */
public interface IBusinessFeedbackService extends IService<BusinessFeedback> {

    //新增意见反馈
   public Boolean addUserFeedback(BusinessFeedtypeVo businessFeedtypeVo);

//    //通过反馈订单id查询详情
//    public BusinessFeedbackDto getByUuid(String order_uuid);

    //一天只能提交3次意见反馈
    Boolean selectCount(String info_uuid);

    //未处理只能有3条  超过3天  也不能提交
    Boolean selectState(String info_uuid);

    //据离上次提交必须10分钟
    Boolean selectCreateTime(String info_uuid);
    //    //通过反馈订单id查询详情
    BusinessFeedbackDto getByOrderUuid(String orderUuid);

    //追加投诉建议
// Boolean append(BusinessFeedbackAppendVo businessFeedbackAppendVo);
}
