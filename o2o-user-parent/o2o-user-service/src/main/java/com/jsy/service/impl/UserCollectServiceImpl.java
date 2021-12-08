package com.jsy.service.impl;

import cn.hutool.core.lang.Tuple;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsy.basic.util.MyPageUtils;
import com.jsy.basic.util.PageInfo;
import com.jsy.basic.util.exception.JSYException;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.clent.CommentClent;
import com.jsy.client.GoodsClient;
import com.jsy.client.NewShopClient;
import com.jsy.client.SetMenuClient;
import com.jsy.domain.Goods;
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

import java.util.*;

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

    @Autowired
    private CommentClent commentClent;




    /**
     * 收藏商品\服务  \套餐\店铺
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
        Integer type = userCollectParam.getType();
        Long goodsId = userCollectParam.getGoodsId();
        Long shopId = userCollectParam.getShopId();
        Long menuId = userCollectParam.getMenuId();


        UserCollect userCollect = new UserCollect();

        if (userCollectParam.getType()==0){//商品、服务

            UserCollect rult = userCollectMapper.selectOne(new QueryWrapper<UserCollect>()
                    .eq("user_id", userId)
                    .eq("goods_id", userCollectParam.getGoodsId())
            );
            if (Objects.nonNull(rult)){
                throw new JSYException(-1,"该商品或服务已经收藏");
            }

            Goods data = goodsClient.getByGoods(userCollectParam.getGoodsId()).getData();
            if (Objects.nonNull(data)){
                userCollect.setUserId(userId);
                userCollect.setGoodsId(goodsId);
                userCollect.setImage(data.getImages().split(",")[0]);
                userCollect.setTitle(data.getTitle());
                userCollect.setPrice(data.getPrice());
                userCollect.setDiscountState(data.getDiscountState());
                userCollect.setDiscountPrice(data.getDiscountPrice());
               /* SelectShopCommentScoreDto rut = commentClent.selectShopCommentScore(data.getShopId()).getData();
                userCollect.setShopScore(Objects.isNull(rut)?5:rut.getScore());*/
            }

        }
        if (userCollectParam.getType()==1){//套餐
            UserCollect rult = userCollectMapper.selectOne(new QueryWrapper<UserCollect>()
                    .eq("user_id", userId)
                    .eq("menu_id", userCollectParam.getMenuId())
            );
            if (Objects.nonNull(rult)){
                throw new JSYException(-1,"该套餐已经收藏");
            }


        }
        if (userCollectParam.getType()==2){//店铺
            UserCollect rult = userCollectMapper.selectOne(new QueryWrapper<UserCollect>()
                    .eq("user_id", userId)
                    .eq("shop_id", userCollectParam.getMenuId())
            );
            if (Objects.nonNull(userCollect)){
                throw new JSYException(-1,"该商店已经收藏");
            }
        }

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
    public PageInfo<Object> userCollectPageList(UserCollectQuery userCollectQuery) {
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
        if (goodsCollect.size()!=0){
            List<GoodsDto> goodsDtos = goodsClient.batchGoods(goodsCollect).getData();
            userCollectDto.setGoodsDto(goodsDtos);
        }

        if (goodsServiceCollect.size()!=0){
            List<GoodsServiceDto> goodsServiceDtos = goodsClient.batchGoodsService(goodsServiceCollect).getData();
            userCollectDto.setGoodsServiceDto(goodsServiceDtos);
        }
        if (setMenuCollect.size()!=0){
            List<SetMenuDto> setMenuDtos = setMenuClient.batchIds(setMenuCollect).getData();
            userCollectDto.setSetMenuDto(setMenuDtos);
        }
        if (shopCollect.size()!=0){
            List<NewShopDto> newShopDtos = newShopClient.batchIds(shopCollect).getData();
            userCollectDto.setNewShopDto(newShopDtos);
        }
        Tuple tuple = new Tuple(userCollectDto.getGoodsDto(),userCollectDto.getGoodsServiceDto(),userCollectDto.getNewShopDto(),userCollectDto.getSetMenuDto());
        Object[] members = tuple.getMembers();
        PageInfo<Object> pageInfo = MyPageUtils.pageMap(userCollectQuery.getPage(), userCollectQuery.getRows(), Arrays.asList(members));
        return pageInfo;
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
         * 收藏类型：0 商品、服务 1：套餐 2：商店
         */
        if (type==0){
            UserCollect goodsService = userCollectMapper.selectOne(new QueryWrapper<UserCollect>().eq("goods_id", id));
            if (Objects.nonNull(goodsService)){//已收藏
                return true;
            }else {
                return false;
            }
        }
        if (type==1){
            UserCollect menu = userCollectMapper.selectOne(new QueryWrapper<UserCollect>().eq("menu_id", id));
            if (Objects.nonNull(menu)){
                return true;
            }else {
                return false;
            }
        }
        if (type==2){
            UserCollect menu = userCollectMapper.selectOne(new QueryWrapper<UserCollect>().eq("shop_id", id));
            if (Objects.nonNull(menu)){
                return true;
            }else {
                return false;
            }
        }
        return null;
    }
}
