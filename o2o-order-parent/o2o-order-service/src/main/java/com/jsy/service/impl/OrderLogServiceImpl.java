package com.jsy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jsy.domain.OrderLog;
import com.jsy.mapper.OrderLogMapper;
import com.jsy.service.IOrderLogService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yu
 * @since 2020-12-24
 */
@Service
public class OrderLogServiceImpl extends ServiceImpl<OrderLogMapper, OrderLog> implements IOrderLogService {

    @Override
    public void saveLog(OrderLog orderLog) {
        this.baseMapper.insert(orderLog);
    }

    @Override
    public int updateByUid(Integer id, String uuid) {
        return this.baseMapper.updateByUid(id,uuid);
    }

    @Override
    public IPage<OrderLog> pageList(Page page, QueryWrapper<OrderLog> queryWrapper) {
        return this.baseMapper.pageList(page,queryWrapper);
    }
}
