package com.jsy.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jsy.basic.util.MyPageUtils;
import com.jsy.basic.util.PageInfo;
import com.jsy.basic.util.exception.JSYException;
import com.jsy.basic.util.utils.BeansCopyUtils;
import com.jsy.client.BrowseClient;
import com.jsy.client.HotClient;
import com.jsy.client.ServiceCharacteristicsClient;
import com.jsy.domain.Browse;
import com.jsy.domain.Goods;
import com.jsy.domain.SetMenu;
import com.jsy.domain.SetMenuGoods;
import com.jsy.dto.SetMenuDto;
import com.jsy.dto.SetMenuGoodsDto;
import com.jsy.dto.SetMenuListDto;
import com.jsy.mapper.GoodsMapper;
import com.jsy.mapper.SetMenuGoodsMapper;
import com.jsy.mapper.SetMenuMapper;
import com.jsy.parameter.SetMenuParam;
import com.jsy.query.SetMenuQuery;
import com.jsy.service.ISetMenuService;
import com.zhsj.baseweb.support.ContextHolder;
import com.zhsj.baseweb.support.LoginUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
    @Resource
    private StringRedisTemplate stringRedisTemplate;

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
    public SetMenuDto getSetMenulist(Long id, Integer type) {
        SetMenu setMenu;
        if (type==1){
          setMenu = setMenuMapper.selectOne(new QueryWrapper<SetMenu>().eq("id", id).eq("is_disable",0).eq("state",1));
        }else {
            setMenu = setMenuMapper.selectOne(new QueryWrapper<SetMenu>().eq("id", id));
        }
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
        SetMenuDto setMenuDto = new SetMenuDto();
        if (ObjectUtil.isNull(setMenu)){
         throw  new JSYException(-10,"套餐不存在");
        }
//        Browse browse1 = browseClient.selectOne(id, loginUser.getId()).getData();
//        if (ObjectUtil.isNull(browse1)){
//            //套餐访问量+1
//            setMenu.setPvNum(setMenu.getPvNum()+1);
//        }
        Long pvNum = statisticsPvNum(loginUser.getId(), id);
            setMenu.setPvNum(pvNum);
        setMenuMapper.updateById(setMenu);
        List<SetMenuGoods> setMenuGoodsList = menuGoodsMapper.selectList(new QueryWrapper<SetMenuGoods>()
                    .eq("set_menu_id", setMenu.getId())
            );

        List<SetMenuGoodsDto> menuGoodsDtoList = new ArrayList<>();
        for (SetMenuGoods setMenuGoods : setMenuGoodsList) {
                Goods goods = goodsMapper.selectOne(new QueryWrapper<Goods>().eq("id", setMenuGoods.getGoodsIds()));
                if (goods==null){
                   break;
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
        List<SetMenu> menuList =  new ArrayList<>();
        if (setMenuQuery.getState()!=null&&setMenuQuery.getIsDisable()!=null){
            menuList = setMenuMapper.selectList(new QueryWrapper<SetMenu>().eq("shop_id", setMenuQuery.getShopId())
                    .eq("state",setMenuQuery.getState())
                    .eq("is_disable",setMenuQuery.getIsDisable())
            );
        }else if(setMenuQuery.getState()!=null){
            menuList = setMenuMapper.selectList(new QueryWrapper<SetMenu>()
                    .eq("shop_id", setMenuQuery.getShopId()).eq("state",setMenuQuery.getState()));
        }else {
            menuList = setMenuMapper.selectList(new QueryWrapper<SetMenu>()
                    .eq("shop_id", setMenuQuery.getShopId()).eq("is_disable",setMenuQuery.getIsDisable()));
        }
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
        List<SetMenu> menuList = setMenuMapper.selectList(new QueryWrapper<SetMenu>().eq("shop_id", setMenuQuery.getShopId())
                                                .eq("state",setMenuQuery.getState())
                                                .eq("is_disable",setMenuQuery.getIsDisable()));
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
//修改店铺所有商家的商品上下架或禁用
    @Override
    public Boolean setState(SetMenuQuery setMenuQuery) {
        if (setMenuQuery.getState()!=null){
            setMenuMapper.setAllState(setMenuQuery);
            hotClient.getHotGoods(setMenuQuery.getSetMenuId());
        }
        if (setMenuQuery.getIsDisable()!=null){
            setMenuMapper.setAllDisable(setMenuQuery);
            hotClient.getHotGoods(setMenuQuery.getSetMenuId());
        }
        return true;
    }
//修改单个商品上下架或禁用
    @Override
    public Boolean setStateById(SetMenuQuery setMenuQuery) {
        SetMenu menu = setMenuMapper.selectById(setMenuQuery.getSetMenuId());
        if (setMenuQuery.getState()!=null){
            menu.setState(setMenuQuery.getState());
            setMenuMapper.updateById(menu);
            if (setMenuQuery.getState()==0){
                //0下架 1上架
                hotClient.getHotGoods(setMenuQuery.getSetMenuId());
            }
            return true;
        }
        if (setMenuQuery.getIsDisable()!=null){
            menu.setIsDisable(setMenuQuery.getIsDisable());
            setMenuMapper.updateById(menu);
            if (setMenuQuery.getIsDisable()==1){
                //套餐是否禁用 0不禁用 1禁用
                hotClient.getHotGoods(setMenuQuery.getSetMenuId());
            }
            return true;
        }
        return false;
    }

    @Override
    public Integer seleceAllSenMenuNumber(Long shopId) {
        Integer count = setMenuMapper.selectCount(new QueryWrapper<SetMenu>().eq("shop_id", shopId));
        return count;
    }

    public Long statisticsPvNum(Long userId,Long id) {
            //存入key
            stringRedisTemplate.opsForHyperLogLog().add("pv_num" + id, userId + "-" + id);
            //统计访问量
            Long num = stringRedisTemplate.opsForHyperLogLog().size("pv_num" + id);
            return num;

    }
    
}

