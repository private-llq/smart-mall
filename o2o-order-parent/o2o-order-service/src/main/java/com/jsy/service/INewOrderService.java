package com.jsy.service;

import com.jsy.domain.NewOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jsy.dto.SelectUserOrderDTO;
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
    //用户根据转态查询订单
    List<SelectUserOrderDTO> selectUserOrder(Long id, SelectUserOrderParam param);
}
