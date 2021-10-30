package com.jsy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jsy.client.FileClient;
import com.jsy.domain.ShopSigns;
import com.jsy.mapper.ShopSignsMapper;
import com.jsy.service.IShopSignsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;

/**
 * <p>
 * 门店招牌表 服务实现类
 * </p>
 *
 * @author yu
 * @since 2021-03-27
 */
@Service
public class ShopSignsServiceImpl extends ServiceImpl<ShopSignsMapper, ShopSigns> implements IShopSignsService {
    @Resource
    private ShopSignsMapper shopSignsMapper;

    @Override
    public ShopSigns getShopSigns(String shopUuid) {
        //商家招牌信息
        ShopSigns ShopSigns = shopSignsMapper.selectOne(new QueryWrapper<ShopSigns>().eq("shop_uuid", shopUuid));
        return  ShopSigns;
    }
}
