package com.jsy.service.impl;

import com.jsy.basic.util.PageInfo;
import com.jsy.domain.BackstageGoods;
import com.jsy.dto.BackstageGoodsDto;
import com.jsy.mapper.BackstageGoodsMapper;
import com.jsy.param.BackstageGoodsParam;
import com.jsy.query.BackstageGoodsQuery;
import com.jsy.service.IBackstageGoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Tian
 * @since 2021-11-29
 */
@Service
public class BackstageGoodsServiceImpl extends ServiceImpl<BackstageGoodsMapper, BackstageGoods> implements IBackstageGoodsService {

    @Autowired
    private BackstageGoodsMapper backstageGoodsMapper;

    @Override
    public void addBackstageGoods(BackstageGoodsParam backstageGoodsParam) {
        BackstageGoods backstageGoods = new BackstageGoods();
        BeanUtils.copyProperties(backstageGoodsParam,backstageGoods);
        backstageGoods.setPlatform("平台官方");
        backstageGoods.setState(0);//默认禁用
        backstageGoodsMapper.insert(backstageGoods);
    }

    @Override
    public void updateBackstageGoods(BackstageGoodsParam backstageGoodsParam) {
        BackstageGoods backstageGoods = new BackstageGoods();
        BeanUtils.copyProperties(backstageGoodsParam,backstageGoods);
        backstageGoodsMapper.updateById(backstageGoods);
    }

    @Override
    public PageInfo<BackstageGoodsDto> pageBackstageGoods(BackstageGoodsQuery query) {
        return null;
    }
}
