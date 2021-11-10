package com.jsy.service.impl;

import com.jsy.domain.Goods;
import com.jsy.domain.SetMenu;
import com.jsy.domain.SetMenuGoods;
import com.jsy.mapper.GoodsMapper;
import com.jsy.mapper.SetMenuGoodsMapper;
import com.jsy.mapper.SetMenuMapper;
import com.jsy.service.ISetMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lijin
 * @since 2021-11-10
 */
@Service
public class SetMenuServiceImpl extends ServiceImpl<SetMenuMapper, SetMenu> implements ISetMenuService {
    @Resource
    private SetMenuMapper setMenuMapper;
    @Resource
    private SetMenuGoodsMapper menuGoodsMapper;
    @Resource
    private GoodsMapper goodsMapper;

    @Override
    public void addSetMenu(SetMenu setMenu) {
        List<SetMenuGoods> setMenuGoodsList = setMenu.getSetMenuGoodsList();
        setMenuMapper.insert(setMenu);
        Long id = setMenu.getId();
        System.out.println(id);
        for (SetMenuGoods setMenuGoods : setMenuGoodsList) {
            SetMenuGoods menuGoods = new SetMenuGoods();
            BeanUtils.copyProperties(setMenuGoods,menuGoods);
            menuGoods.setShopId(setMenu.getShopId());
            menuGoods.setSetMenuId(id);
            menuGoodsMapper.insert(menuGoods);
        }

    }
}
