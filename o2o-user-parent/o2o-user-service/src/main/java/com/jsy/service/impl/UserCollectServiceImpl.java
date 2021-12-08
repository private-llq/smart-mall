package com.jsy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsy.basic.util.MyPageUtils;
import com.jsy.basic.util.PageInfo;
import com.jsy.basic.util.exception.JSYException;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.client.GoodsClient;
import com.jsy.client.NewShopClient;
import com.jsy.client.SetMenuClient;
import com.jsy.domain.NewShop;
import com.jsy.domain.UserCollect;
import com.jsy.dto.*;
import com.jsy.mapper.UserCollectMapper;
import com.jsy.param.UserCollectParam;
import com.jsy.query.UserCollectQuery;
import com.jsy.service.IUserCollectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhsj.baseweb.support.ContextHolder;
import com.zhsj.baseweb.support.LoginUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yu
 * @since 2021-11-22
 */
@Service
public class UserCollectServiceImpl extends ServiceImpl<UserCollectMapper, UserCollect> implements IUserCollectService {

    @Autowired
    private UserCollectMapper userCollectMapper;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SetMenuClient setMenuClient;

    @Autowired
    private NewShopClient newShopClient;



    /**
     * 收藏商品\服务\套餐\店铺
     * @param userCollectParam
     * @return
     */
    @Override
    public void addUserCollect(UserCollectParam userCollectParam) {
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
        if (Objects.isNull(loginUser)){
            new JSYException(-1,"用户认证失败！");
        }
        Long userId = loginUser.getId();//用户id

        //查询是否收藏过
        if (userCollectParam.getType()==0){
            UserCollect userCollect = userCollectMapper.selectOne(new QueryWrapper<UserCollect>()
                    .eq("user_id", userId)
                    .eq("goods_id", userCollectParam.getGoodsId())
            );
            if (Objects.nonNull(userCollect)){
                throw new JSYException(-1,"该商品或服务已经收藏");
            }
        }
        if (userCollectParam.getType()==1){
            UserCollect userCollect = userCollectMapper.selectOne(new QueryWrapper<UserCollect>()
                    .eq("user_id", userId)
                    .eq("menu_id", userCollectParam.getMenuId())
            );
            if (Objects.nonNull(userCollect)){
                throw new JSYException(-1,"该套餐已经收藏");
            }
        }
        if (userCollectParam.getType()==2){
            UserCollect userCollect = userCollectMapper.selectOne(new QueryWrapper<UserCollect>()
                    .eq("user_id", userId)
                    .eq("shop_id", userCollectParam.getMenuId())
            );
            if (Objects.nonNull(userCollect)){
                throw new JSYException(-1,"该商店已经收藏");
            }
        }
        UserCollect userCollect = new UserCollect();
        userCollect.setUserId(userId);
        BeanUtils.copyProperties(userCollectParam,userCollect);
        userCollectMapper.insert(userCollect);
    }


    /**
     * 分页查询收藏的商品/服务、套餐、店铺
     * @param userCollectQuery 查询对象
     * @return PageList 分页对象
     */
    @Override
    public userCollectDto userCollectPageList(UserCollectQuery userCollectQuery) {
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
        if (Objects.isNull(loginUser)){
            new JSYException(-1,"用户认证失败！");
        }
        Long userId = loginUser.getId();//用户id
        List<UserCollect> list = userCollectMapper.selectList(new QueryWrapper<UserCollect>().eq("user_id", userId));
        List<Long> goodsCollect = new ArrayList<>();
        List<Long> goodsServiceCollect = new ArrayList<>();
        List<Long> setMenuCollect = new ArrayList<>();
        List<Long> shopCollect = new ArrayList<>();
        list.stream().forEach(x->{
            if (x.getType()==0){
                goodsCollect.add(x.getGoodsId());
            }
            if (x.getType()==1){
                setMenuCollect.add(x.getMenuId());
            }
            if (x.getType()==2){
                shopCollect.add(x.getShopId());
            }
            if (x.getType()==3){
                goodsServiceCollect.add(x.getGoodsId());
            }

        });


        userCollectDto userCollectDto = new userCollectDto();

        if (userCollectQuery.getType()==0 ){//商品
            List<GoodsDto> goodsDtos = goodsClient.batchGoods(goodsCollect).getData();
            if (Objects.nonNull(goodsDtos)){
                PageInfo<GoodsDto> goodsDtoPageInfo = MyPageUtils.pageMap(userCollectQuery.getPage(), userCollectQuery.getRows(), goodsDtos);
                userCollectDto.setGoodsDto(goodsDtoPageInfo);
            }
        }
        if (userCollectQuery.getType()==3 ){//服务
            List<GoodsServiceDto> goodsServiceDtos = goodsClient.batchGoodsService(goodsServiceCollect).getData();
            if (Objects.nonNull(goodsServiceDtos)){
                PageInfo<GoodsServiceDto> goodsServiceDtoPageInfo = MyPageUtils.pageMap(userCollectQuery.getPage(), userCollectQuery.getRows(), goodsServiceDtos);
                userCollectDto.setGoodsServiceDto(goodsServiceDtoPageInfo);
            }
        }

        if (userCollectQuery.getType()==1){//套餐
            List<SetMenuDto> setMenuDtos = setMenuClient.batchIds(setMenuCollect).getData();
            if (Objects.nonNull(setMenuDtos)){
                PageInfo<SetMenuDto> setMenuDtoPageInfo = MyPageUtils.pageMap(userCollectQuery.getPage(), userCollectQuery.getRows(), setMenuDtos);
                userCollectDto.setSetMenuDto(setMenuDtoPageInfo);
            }
        }
        if (userCollectQuery.getType()==2){//店铺
            List<NewShopDto> newShopDtos = newShopClient.batchIds(shopCollect).getData();
            if (Objects.nonNull(newShopDtos)){
                PageInfo<NewShopDto> newShopDtoPageInfo = MyPageUtils.pageMap(userCollectQuery.getPage(), userCollectQuery.getRows(), newShopDtos);
                userCollectDto.setNewShopDto(newShopDtoPageInfo);
            }
        }
        return userCollectDto;
    }

    /**
     * 收藏按钮状态 亮(已收藏)：true   灰色 ：false
     */
    @Override
    public Boolean userCollectState(Integer type, Long id) {
        if (Objects.isNull(type)){
            throw new JSYException(-1,"收藏类型不能为空！");
        }
        if (Objects.isNull(id)){
            throw new JSYException(-1,"id不能为空！");
        }
        /**
         * 收藏类型 0 商品、服务 1 套餐 2 商店 3 服务
         */
        if (type==0){

        }
        UserCollect userCollect = userCollectMapper.selectOne(new QueryWrapper<UserCollect>().eq("id", id).eq("type", type));

        return null;
    }
}
