package com.jsy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jsy.basic.util.MyPageUtils;
import com.jsy.basic.util.PageInfo;
import com.jsy.basic.util.exception.JSYException;
import com.jsy.basic.util.utils.BeansCopyUtils;
import com.jsy.client.BrowseClient;
import com.jsy.client.HotClient;
import com.jsy.client.ServiceCharacteristicsClient;
import com.jsy.domain.*;
import com.jsy.dto.SetMenuDto;
import com.jsy.dto.SetMenuGoodsDto;
import com.jsy.dto.SetMenuListDto;
import com.jsy.mapper.GoodsMapper;
import com.jsy.mapper.SetMenuGoodsMapper;
import com.jsy.mapper.SetMenuMapper;
import com.jsy.parameter.SetMenuParam;
import com.jsy.query.SetMenuQuery;
import com.jsy.service.ISetMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhsj.baseweb.support.ContextHolder;
import com.zhsj.baseweb.support.LoginUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
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
    @Resource
    private BrowseClient browseClient;
    @Autowired
    private HotClient hotClient;

    @Override
    public void addSetMenu(SetMenuParam setMenu) {

        List<SetMenuGoods> setMenuGoodsList = setMenu.getSetMenuGoodsList();
        SetMenu menu = new SetMenu();
        BeanUtils.copyProperties(setMenu,menu);
        //开启折扣价格   是否开启折扣：0未开启 1开启")
        if (setMenu.getSellingPrice()!=null){
            menu.setDiscountState(1);
        }else {
            menu.setDiscountState(0);
//            menu.setMenuExplain(null);
        }
//        String[] ids = setMenu.getServiceCharacteristicsIds().split(",");//服务特点ids
//
//        ArrayList<ServiceCharacteristics> list = new ArrayList<>();
//        for (String id : ids) {
//            try {
//                list.add(characteristicsClient.get(Long.valueOf(id)).getData());
//            } catch (NumberFormatException e) {
//                e.printStackTrace();
//                throw  new JSYException(-1,"服务特点错误");
//            }
//        }
//
//        list.stream().forEach(x->{
//            if (StringUtils.containsAny(x.getName(),"上门服务","上门","到家","到家服务")){
//                menu.setIsVisitingService(1);//支持上门服务
//            }
//        });

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
    public SetMenuDto getSetMenulist(Long id) {
        SetMenu setMenu = setMenuMapper.selectOne(new QueryWrapper<SetMenu>().eq("id", id));

        SetMenuDto setMenuDto = new SetMenuDto();
        if (setMenu==null){
            return setMenuDto;
        }
        //套餐访问量+1
        setMenu.setPvNum(setMenu.getPvNum()+1);
        setMenuMapper.updateById(setMenu);
        List<SetMenuGoods> setMenuGoodsList = menuGoodsMapper.selectList(new QueryWrapper<SetMenuGoods>()
                    .eq("set_menu_id", setMenu.getId())
            );

        List<SetMenuGoodsDto> menuGoodsDtoList = new ArrayList<>();


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



        List<SetMenuListDto> setMenuListDtos = new ArrayList<>();
        Set<Map.Entry<String, List<SetMenuGoodsDto>>> entries = map.entrySet();
        for (Map.Entry entry:entries){
            SetMenuListDto setMenuListDto = new SetMenuListDto();
            setMenuListDto.setTitle((String) entry.getKey());

            setMenuListDto.setDtoList((List<SetMenuGoodsDto>) entry.getValue());
            setMenuListDtos.add(setMenuListDto);
        }

        BeanUtils.copyProperties(setMenu,setMenuDto);
        setMenuDto.setMap(setMenuListDtos);
//        setMenuDto.setServiceCharacteristicsIds(serviceCharacteristicsDtoList);



        //用户浏览记录
        Browse browse = new Browse();
        browse.setRealPrice(setMenu.getRealPrice());
        browse.setSellingPrice(setMenu.getSellingPrice());
//        browse.setIsVisitingService(setMenu.getIsVisitingService());
        browse.setName(setMenu.getName());
        browse.setShopId(setMenu.getShopId());
        browse.setTextDescription(setMenu.getMenuExplain());
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
        System.out.println(loginUser);
        browse.setUserId(loginUser.getId());
        browse.setDiscountState(setMenuDto.getDiscountState());
        //商品id
        browse.setGoodsId(setMenu.getId());
        //套餐为2
        browse.setType(2);
        //商品图片
        browse.setImages(setMenu.getImages());
        browseClient.save(browse);


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
    public PageInfo<SetMenuDto> getList(SetMenuQuery setMenuQuery) {
        //根据商家id 和所
        List<SetMenu> menuList = setMenuMapper.selectList(new QueryWrapper<SetMenu>().eq("shop_id", setMenuQuery.getShopId()).eq("state",setMenuQuery.getState()));
        List<SetMenuDto> dtoList = new ArrayList<>();
        for (SetMenu setMenu : menuList) {
            SetMenuDto setMenuDto = new SetMenuDto();
            BeanUtils.copyProperties(setMenu,setMenuDto);
            dtoList.add(setMenuDto);
        }
        PageInfo<SetMenuDto> setMenuDtoPageInfo = MyPageUtils.pageMap(setMenuQuery.getPage(), setMenuQuery.getRows(), dtoList);
        return setMenuDtoPageInfo;
    }

    @Override
    public List<SetMenuListDto> getMenuId(Long setMenuId) {
        List<SetMenuGoods> menuGoods = menuGoodsMapper.selectList(new QueryWrapper<SetMenuGoods>().eq("set_menu_id", setMenuId));
        for (SetMenuGoods setMenuGoods : menuGoods) {
            Goods goods = goodsMapper.selectOne(new QueryWrapper<Goods>().eq("id", setMenuGoods.getGoodsIds()));
            if (goods!=null){
                setMenuGoods.setName(goods.getTitle());
                setMenuGoods.setPrice(goods.getPrice());
            }
        }
        List<SetMenuGoodsDto> menuGoodsDtoList = BeansCopyUtils.copyListProperties(menuGoods, SetMenuGoodsDto::new);
        Map<String,List<SetMenuGoodsDto>> map = menuGoodsDtoList.stream().collect(Collectors.groupingBy(SetMenuGoodsDto::getTitle));
        List<SetMenuListDto> setMenuListDtos = new ArrayList<>();
        Set<Map.Entry<String, List<SetMenuGoodsDto>>> entries = map.entrySet();
        for (Map.Entry entry:entries){
            SetMenuListDto setMenuListDto = new SetMenuListDto();
            setMenuListDto.setTitle((String) entry.getKey());
            setMenuListDto.setDtoList((List<SetMenuGoodsDto>) entry.getValue());
            setMenuListDtos.add(setMenuListDto);
        }

        return setMenuListDtos;


    }

    @Override
    public PageInfo<SetMenuDto> listAll(SetMenuQuery setMenuQuery) {
        //根据商家id
        List<SetMenu> menuList = setMenuMapper.selectList(new QueryWrapper<SetMenu>().eq("shop_id", setMenuQuery.getShopId()));
        List<SetMenuDto> dtoList = new ArrayList<>();
        for (SetMenu setMenu : menuList) {
            SetMenuDto setMenuDto = new SetMenuDto();
            BeanUtils.copyProperties(setMenu,setMenuDto);
            dtoList.add(setMenuDto);
        }
        PageInfo<SetMenuDto> setMenuDtoPageInfo = MyPageUtils.pageMap(setMenuQuery.getPage(), setMenuQuery.getRows(), dtoList);
        return setMenuDtoPageInfo;
    }

    @Override
    public void setState(Long id,Integer state) {
        SetMenu setMenu = setMenuMapper.selectOne(new QueryWrapper<SetMenu>().eq("id", id));
        //下架套餐时  更新热门商品数据
        if (state==0){
            hotClient.getHotGoods(id);
        }
        setMenu.setState(state);
        setMenuMapper.updateById(setMenu);
    }

    @Override
    public void updateSetMenu(SetMenuParam setMenu) {
        System.out.println("修改");
        List<SetMenuGoods> setMenuGoodsList = setMenu.getSetMenuGoodsList();
        SetMenu menu = new SetMenu();
        BeanUtils.copyProperties(setMenu,menu);
        //开启折扣价格
        if (setMenu.getSellingPrice()!=null){
            menu.setDiscountState(1);
        }else {
            menu.setDiscountState(0);
            menu.setSellingPrice(null);
        }
        setMenuMapper.updateById(menu);
        Long menuId = setMenu.getId();
        for (SetMenuGoods setMenuGoods : setMenuGoodsList) {
            System.out.println(setMenuGoods);
            menuGoodsMapper.updateById(setMenuGoods);
        }
    }

    @Override
    public List<SetMenuDto> batchIds(List<Long> ids) {
        List<SetMenu> setMenus = setMenuMapper.selectBatchIds(ids);
        List<SetMenuDto> dtoList= new ArrayList<>();
        for (SetMenu setMenu : setMenus) {
            SetMenuDto setMenuDto = new SetMenuDto();
            BeanUtils.copyProperties(setMenu,setMenuDto);
            dtoList.add(setMenuDto);
        }
//        BeansCopyUtils.copyListProperties(setMenus,SetMenuDto::new);
        return dtoList;
    }

}

