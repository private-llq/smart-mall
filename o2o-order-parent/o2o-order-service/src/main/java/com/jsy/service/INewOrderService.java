package com.jsy.service;

import com.jsy.domain.NewOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jsy.dto.SelectUserOrderDTO;
import com.jsy.query.CompletionPayParam;
import com.jsy.query.CreationOrderParam;
import com.jsy.query.SelectUserOrderParam;

import java.util.List;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author arli
 * @since 2021-11-15
 */
public interface INewOrderService extends IService<NewOrder> {
    //新增一条订单
    Boolean creationOrder(CreationOrderParam creationOrderParam);
    //用户根据转态查询订单(Integer type 0用户 1是商家)
    List<SelectUserOrderDTO> selectUserOrder(Long id, SelectUserOrderParam param,Integer type);
    //用户删除订单
    boolean deletedUserOrder(Long orderId);
    //商家同意预约订单
    Boolean consentOrder(Long orderId);
    //用户支付后调用的接口
    public Boolean completionPay(CompletionPayParam param);

}
