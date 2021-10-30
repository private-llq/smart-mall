package com.jsy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jsy.basic.util.constant.Global;
import com.jsy.basic.util.exception.JSYException;
import com.jsy.basic.util.utils.CurrentUserHolder;
import com.jsy.basic.util.utils.UUIDUtils;
import com.jsy.basic.util.vo.UserDto;
import com.jsy.domain.ShopAssets;
import com.jsy.domain.ShopRecord;
import com.jsy.mapper.ShopRecordMapper;
import com.jsy.service.IShopAssetsService;
import com.jsy.service.IShopRecordService;
import com.jsy.vo.ShopRecordVo;
import com.jsy.vo.WithdrawVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yu
 * @since 2020-12-17
 */
@Service
public class ShopRecordServiceImpl extends ServiceImpl<ShopRecordMapper, ShopRecord> implements IShopRecordService {

    @Autowired
    private IShopAssetsService shopAssetsService;

    //提现
    @Override
    @Transactional
    public void withdraw(WithdrawVO withdrawVO) {
        UserDto dto = CurrentUserHolder.getCurrentUser();
        if (Objects.isNull(dto)) {
            throw new JSYException(-1,"请登录后操作");
        }
        ShopRecord record = new ShopRecord();
        record.setAccountNumber(UUIDUtils.getAccountNumber());
        // TODO: 2020/12/17 时间暂时都写现在 具体转账到哪里 暂定
        record.setCreateTime(LocalDateTime.now());
        record.setArrivalTime(LocalDateTime.now());
        record.setRecord(withdrawVO.getMoney());
        record.setUuid(UUIDUtils.getUUID());
        record.setAssetsUuid(dto.getUuid());
        record.setTurnType(0);

        baseMapper.insert(record);

        shopAssetsService.updateMoney(0,withdrawVO.getMoney(),dto.getUuid());

    }

    @Override
    public void save(ShopRecordVo shopRecordVo) {
        ShopAssets shopAssets = shopAssetsService.getByUUid(shopRecordVo.getUuid());
        if (Objects.isNull(shopAssets)){
            throw new JSYException(-1,"店铺已注销");
        }
        ShopRecord shopRecord = new ShopRecord();
        //TODO: 流水账号001 暂写
        shopRecord.setAccountName("001");
        shopRecord.setOrderUuid(shopRecordVo.getOrderUuid());
        shopRecord.setRecord(shopRecordVo.getRecord());
        shopRecord.setAssetsUuid(shopAssets.getUuid());
        shopRecord.setCreateTime(LocalDateTime.now());
        shopRecord.setAccountNumber(UUIDUtils.getAccountNumber());
        shopRecord.setTurnType(shopRecordVo.getTurnType());
        shopRecord.setUuid(UUIDUtils.getUUID());
        shopRecord.setStateId(Global.INT_1);
        this.save(shopRecord);
    }

    @Override
    public List<ShopRecord> recordList() {

        UserDto dto = CurrentUserHolder.getCurrentUser();
        if (Objects.isNull(dto)) {
            throw new JSYException(-1,"请登录后操作");
        }
        List<ShopRecord> recordList = baseMapper.selectList(new QueryWrapper<ShopRecord>().eq("assets_uuid", dto.getUuid()));
        return recordList;

    }

    @Override
    public int deleteByOuid(String uuid) {
        return baseMapper.deleteByOuid(uuid);
    }

    @Override
    public int deleteByStr(List<String> strings) {
        return baseMapper.deleteByStr(strings);
    }

    @Override
    public IPage<ShopRecord> pageShopRecord(Page<ShopRecord> page, QueryWrapper queryWrapper) {
        return this.baseMapper.pageShopRecord(page,queryWrapper);
    }
}
