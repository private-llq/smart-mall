package com.jsy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jsy.domain.ShopCapital;
import com.jsy.mapper.ShopCapitalMapper;
import com.jsy.query.AddCapitalParam;
import com.jsy.service.IShopCapitalService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * <p>
 * 商家余额表 服务实现类
 * </p>
 *
 * @author arli
 * @since 2021-12-24
 */
@Service
public class ShopCapitalServiceImpl extends ServiceImpl<ShopCapitalMapper, ShopCapital> implements IShopCapitalService {
    @Resource
    private ShopCapitalMapper shopCapitalMapper;

    //增加余额
    @Override
    public Boolean addCapital(AddCapitalParam param) {
        QueryWrapper<ShopCapital> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("shop_id", param.getShopId());
        ShopCapital shopCapital = shopCapitalMapper.selectOne(queryWrapper);
        shopCapital.setCapital(shopCapital.getCapital().add(param.getMoney()));
        int i = shopCapitalMapper.updateById(shopCapital);
        if (i > 0) {
            return true;
        }
        return false;
    }

    //减少余额
    @Override
    public Boolean subtractCapital(AddCapitalParam param) {
        QueryWrapper<ShopCapital> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("shop_id", param.getShopId());
        ShopCapital shopCapital = shopCapitalMapper.selectOne(queryWrapper);
        shopCapital.setCapital(shopCapital.getCapital().subtract(param.getMoney()));
        int i = shopCapitalMapper.updateById(shopCapital);
        if (i > 0) {
            return true;
        }
        return false;
    }

    //初始化店铺余额
    @Override
    public Boolean initializeShopCapital(Long shopId) {
        ShopCapital entity = new ShopCapital();
        entity.setShopId(shopId);
        entity.setCapital(BigDecimal.ZERO);
        int insert = shopCapitalMapper.insert(entity);
        if (insert>0) {
            return true;
        }
        return false;
    }
    //查询店铺的余额
    @Override
    public ShopCapital selectShopCapital(Long shopId) {
        QueryWrapper<ShopCapital> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("shop_id",shopId);
        ShopCapital shopCapital = shopCapitalMapper.selectOne(queryWrapper);
        return shopCapital;
    }
}
