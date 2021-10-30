package com.jsy.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsy.domain.ShopRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jsy.vo.WithdrawVO;
import com.jsy.vo.ShopRecordVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yu
 * @since 2020-12-17
 */
public interface IShopRecordService extends IService<ShopRecord> {

    void withdraw(WithdrawVO withdrawVO);

    void save(ShopRecordVo shopRecordVo);

    List<ShopRecord> recordList();

    int deleteByOuid(String uuid);

    int deleteByStr(List<String> strings);

    IPage<ShopRecord> pageShopRecord(Page<ShopRecord> page, QueryWrapper queryWrapper);
}
