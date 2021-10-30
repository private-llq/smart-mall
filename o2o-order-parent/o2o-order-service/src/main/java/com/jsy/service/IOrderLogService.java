package com.jsy.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jsy.domain.OrderLog;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yu
 * @since 2020-12-24
 */
public interface IOrderLogService extends IService<OrderLog> {

    int updateByUid(Integer id, String uuid);

    IPage<OrderLog> pageList(Page page, QueryWrapper<OrderLog> queryWrapper);

    void saveLog(OrderLog orderLog);
}
