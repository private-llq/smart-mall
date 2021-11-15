package com.jsy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jsy.basic.util.exception.JSYException;
import com.jsy.basic.util.utils.BeansCopyUtils;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.client.ServiceCharacteristicsClient;
import com.jsy.domain.Goods;
import com.jsy.domain.ServiceCharacteristics;
import com.jsy.domain.SetMenu;
import com.jsy.domain.SetMenuGoods;
import com.jsy.dto.ServiceCharacteristicsDto;
import com.jsy.dto.SetMenuDto;
import com.jsy.dto.SetMenuGoodsDto;
import com.jsy.mapper.GoodsMapper;
import com.jsy.mapper.SetMenuGoodsMapper;
import com.jsy.mapper.SetMenuMapper;
import com.jsy.parameter.SetMenuParam;
import com.jsy.service.ISetMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
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
    @Resource
    private ServiceCharacteristicsClient characteristicsClient;

    @Override
    public void addSetMenu(SetMenuParam setMenu) {

        List<SetMenuGoods> setMenuGoodsList = setMenu.getSetMenuGoodsList();
        SetMenu menu = new SetMenu();
        BeanUtils.copyProperties(setMenu,menu);
        String[] ids = setMenu.getServiceCharacteristicsIds().split(",");//服务特点ids

        ArrayList<ServiceCharacteristics> list = new ArrayList<>();
        for (String id : ids) {
            list.add(characteristicsClient.get(Long.valueOf(id)).getData());
        }

        list.stream().forEach(x->{
            if (StringUtils.containsAny(x.getName(),"上门服务","上门","到家","到家服务")){
                menu.setIsVisitingService(1);//支持上门服务
            }
        });

        setMenuMapper.insert(menu);
        Long id = menu.getId();
        for (SetMenuGoods setMenuGoods : setMenuGoodsList) {
            SetMenuGoods menuGoods = new SetMenuGoods();
            BeanUtils.copyProperties(setMenuGoods,menuGoods);
            menuGoods.setShopId(setMenu.getShopId());
            menuGoods.setSetMenuId(id);
            menuGoodsMapper.insert(menuGoods);
        }

    }

    @Override
    public SetMenuDto getSetMenulist(Long shopId,Long id) {
        SetMenu setMenu = setMenuMapper.selectOne(new QueryWrapper<SetMenu>().eq("shop_id", shopId).eq("id", id));
        List<SetMenuGoods> setMenuGoodsList = menuGoodsMapper.selectList(new QueryWrapper<SetMenuGoods>()
                    .eq("set_menu_id", setMenu.getId())
            );
        List<SetMenuGoodsDto> menuGoodsDtoList = new ArrayList<>();
        //查询服务特点
        String characteristicsIds = setMenu.getServiceCharacteristicsIds();
        String[] strings = characteristicsIds.split(",");
        List<ServiceCharacteristicsDto> serviceCharacteristicsDtoList = new ArrayList<>();
        for (String s : strings) {
            ServiceCharacteristics serviceCharacteristics = characteristicsClient.get(Long.valueOf(s)).getData();
            ServiceCharacteristicsDto serviceCharacteristicsDto = new ServiceCharacteristicsDto();
            BeanUtils.copyProperties(serviceCharacteristics,serviceCharacteristicsDto);
            serviceCharacteristicsDtoList.add(serviceCharacteristicsDto);
        }

        for (SetMenuGoods setMenuGoods : setMenuGoodsList) {
                Goods goods = goodsMapper.selectOne(new QueryWrapper<Goods>().eq("id", setMenuGoods.getGoodsIds()));
                if (goods==null){
                    throw new JSYException(-1,"商品为空");
                }
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
        setMenuDto.setServiceCharacteristicsIds(serviceCharacteristicsDtoList);
        return setMenuDto;




//        List<SetMenuDto> dtoList = new ArrayList<>();
//        for (SetMenu setMenu : menuList) {
//            List<SetMenuGoods> setMenuGoodsList = menuGoodsMapper.selectList(new QueryWrapper<SetMenuGoods>()
//                    .eq("set_menu_id", setMenu.getId())
//            );
//            List<SetMenuGoodsDto> menuGoodsDtoList = new ArrayList<>();
//
//            for (SetMenuGoods setMenuGoods : setMenuGoodsList) {
//                Goods goods = goodsMapper.selectOne(new QueryWrapper<Goods>().eq("id", setMenuGoods.getGoodsIds()));
//                setMenuGoods.setName(goods.getTitle());
//                setMenuGoods.setPrice(goods.getPrice());
//
//                SetMenuGoodsDto setMenuGoodsDto = new SetMenuGoodsDto();
//                BeanUtils.copyProperties(setMenuGoods,setMenuGoodsDto);
//                menuGoodsDtoList.add(setMenuGoodsDto);
//            }
//
//            Map<String, List<SetMenuGoodsDto>> map = menuGoodsDtoList.stream().collect(Collectors.groupingBy(SetMenuGoodsDto::getTitle));
//            SetMenuDto setMenuDto = new SetMenuDto();
//            BeanUtils.copyProperties(setMenu,setMenuDto);
//            setMenuDto.setMap(map);
//            dtoList.add(setMenuDto);
//
//        }
    }

    @Override
    public List<SetMenuDto> getList(Long shopId,Integer state) {
        //根据商家id 和所
        List<SetMenu> menuList = setMenuMapper.selectList(new QueryWrapper<SetMenu>().eq("shop_id", shopId).eq("state",state));
        List<SetMenuDto> dtoList = new ArrayList<>();
        for (SetMenu setMenu : menuList) {
            SetMenuDto setMenuDto = new SetMenuDto();
            BeanUtils.copyProperties(setMenu,setMenuDto);
            dtoList.add(setMenuDto);
        }
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

    @Override
    public List<SetMenuDto> listAll(Long shopId) {
        //根据商家id 和所
        List<SetMenu> menuList = setMenuMapper.selectList(new QueryWrapper<SetMenu>().eq("shop_id", shopId));
        List<SetMenuDto> dtoList = new ArrayList<>();
        for (SetMenu setMenu : menuList) {
            SetMenuDto setMenuDto = new SetMenuDto();
            BeanUtils.copyProperties(setMenu,setMenuDto);
            dtoList.add(setMenuDto);
        }
        return dtoList;
    }

    @Override
    public void setState(Long id,Integer state) {
        SetMenu setMenu = setMenuMapper.selectOne(new QueryWrapper<SetMenu>().eq("id", id));
        setMenu.setState(state);
        setMenuMapper.updateById(setMenu);
    }

    @Override
    public void updateSetMenu(SetMenuParam setMenu) {
        List<SetMenuGoods> setMenuGoodsList = setMenu.getSetMenuGoodsList();
        SetMenu menu = new SetMenu();
        BeanUtils.copyProperties(setMenu,menu);
        setMenuMapper.updateById(menu);
        Long menuId = setMenu.getId();
        for (SetMenuGoods setMenuGoods : setMenuGoodsList) {
            System.out.println(setMenuGoods);
            menuGoodsMapper.updateById(setMenuGoods);
        }
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

