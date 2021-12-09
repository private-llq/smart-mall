package com.jsy.service;

import com.jsy.domain.NewRefund;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jsy.dto.SelectRefundByoderDto;
import com.jsy.query.AgreeRefundParam;
import com.jsy.query.ApplyRefundParam;
import com.jsy.query.ShopWhetherRefundParam;

/**
 * <p>
 * 退款申请表 服务类
 * </p>
 *
 * @author arli
 * @since 2021-11-15
 */
public interface INewRefundService extends IService<NewRefund> {
    //申请退款
    Boolean applyRefund(ApplyRefundParam param);
    //根据订单号查询退款信息
    SelectRefundByoderDto selectRefundByoder(Long orderId);
    //拒绝退款
    Boolean refuseRefund(ShopWhetherRefundParam param);
    //同意退款
    Boolean agreeRefund(AgreeRefundParam param);
}
