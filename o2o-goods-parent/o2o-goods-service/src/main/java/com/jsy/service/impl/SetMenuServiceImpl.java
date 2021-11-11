package com.jsy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jsy.basic.util.utils.BeansCopyUtils;
import com.jsy.domain.Goods;
import com.jsy.domain.SetMenu;
import com.jsy.domain.SetMenuGoods;
import com.jsy.dto.SetMenuDto;
import com.jsy.dto.SetMenuGoodsDto;
import com.jsy.mapper.GoodsMapper;
import com.jsy.mapper.SetMenuGoodsMapper;
import com.jsy.mapper.SetMenuMapper;
import com.jsy.service.ISetMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        for (SetMenuGoods setMenuGoods : setMenuGoodsList) {
            SetMenuGoods menuGoods = new SetMenuGoods();
            BeanUtils.copyProperties(setMenuGoods,menuGoods);
            menuGoods.setShopId(setMenu.getShopId());
            menuGoods.setSetMenuId(id);
            menuGoodsMapper.insert(menuGoods);
        }

    }

    @Override
    public List<SetMenuDto> getSetMenulist(Long shopId) {
        List<SetMenu> menuList = setMenuMapper.selectList(new QueryWrapper<SetMenu>().eq("shop_id", shopId));
        List<SetMenuDto> dtoList = new ArrayList<>();
        for (SetMenu setMenu : menuList) {
            List<SetMenuGoods> setMenuGoodsList = menuGoodsMapper.selectList(new QueryWrapper<SetMenuGoods>()
                    .eq("set_menu_id", setMenu.getId())
            );
            List<SetMenuGoodsDto> menuGoodsDtoList = new ArrayList<>();

            for (SetMenuGoods setMenuGoods : setMenuGoodsList) {
                Goods goods = goodsMapper.selectOne(new QueryWrapper<Goods>().eq("id", setMenuGoods.getGoodsIds()));
                setMenuGoods.setName(goods.getTitle());
                setMenuGoods.setPrice(goods.getPrice());

                SetMenuGoodsDto setMenuGoodsDto = new SetMenuGoodsDto();
                BeanUtils.copyProperties(setMenuGoods,setMenuGoodsDto);
                menuGoodsDtoList.add(setMenuGoodsDto);
            }

            Map<String, List<SetMenuGoodsDto>> map = menuGoodsDtoList.stream().collect(Collectors.groupingBy(SetMenuGoodsDto::getTitle));
            SetMenuDto setMenuDto = new SetMenuDto();
            BeanUtils.copyProperties(setMenu,setMenuDto);
            setMenuDto.setMap(map);
            dtoList.add(setMenuDto);

        }


        return dtoList;
    }

    @Override
    public List<SetMenuDto> getList(Long shopId,Integer state) {
        //根据商家id 和所
        List<SetMenu> menuList = setMenuMapper.selectList(new QueryWrapper<SetMenu>().eq("shop_id", shopId).eq("state",state));
        List<SetMenuDto> dtoList = BeansCopyUtils.copyListProperties(menuList, SetMenuDto::new);
        return dtoList;
    }

    @Override
    public Map<String,List<SetMenuGoodsDto>> getMenuId(Long setMenuId) {
        List<SetMenuGoods> menuGoods = menuGoodsMapper.selectList(new QueryWrapper<SetMenuGoods>().eq("set_menu_id", setMenuId));
        for (SetMenuGoods setMenuGoods : menuGoods) {
            Goods goods = goodsMapper.selectOne(new QueryWrapper<Goods>().eq("id", setMenuGoods.getGoodsIds()));
            setMenuGoods.setName(goods.getTitle());
            setMenuGoods.setPrice(goods.getPrice());
        }
        List<SetMenuGoodsDto> menuGoodsDtoList = BeansCopyUtils.copyListProperties(menuGoods, SetMenuGoodsDto::new);
        Map<String,List<SetMenuGoodsDto>> map = menuGoodsDtoList.stream().collect(Collectors.groupingBy(SetMenuGoodsDto::getTitle));
        return map;


    }

    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        numbers.forEach(x-> {
            if (x == 3) {
                System.out.println(x);
            }
        });
    }
}

