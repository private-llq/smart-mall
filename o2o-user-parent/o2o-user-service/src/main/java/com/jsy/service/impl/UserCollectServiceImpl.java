package com.jsy.service.impl;

import cn.hutool.core.lang.Tuple;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsy.basic.util.MyPageUtils;
import com.jsy.basic.util.PageInfo;
import com.jsy.basic.util.exception.JSYException;
import com.jsy.basic.util.utils.BeansCopyUtils;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.clent.CommentClent;
import com.jsy.client.*;
import com.jsy.domain.*;
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
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private TreeClient treeClient;

    @Autowired
    private ShoppingCartClient shoppingCartClient;

    /**
     * 收藏商品\服务\套餐\店铺
     * @param userCollectParam 收藏类型：0 商品  1:服务  2：套餐  3：商店
     * @return
     */
    @Override
    @Transactional
    public UserCollect addUserCollect(UserCollectParam userCollectParam) {
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

        if (type==0) {//商品

            UserCollect rult = userCollectMapper.selectOne(new QueryWrapper<UserCollect>()
                    .eq("user_id", userId)
                    .eq("goods_id", userCollectParam.getGoodsId())
            );
            if (Objects.nonNull(rult)) {
                throw new JSYException(-1, "该商品已经收藏");
            }

            Goods data = goodsClient.getByGoods(userCollectParam.getGoodsId()).getData();
            if (Objects.isNull(data)) {
               throw new JSYException(-1,"未找到该商品！");
            }
            userCollect.setUserId(userId);
            userCollect.setGoodsId(goodsId);
            userCollect.setType(0);
            userCollect.setShopId(data.getShopId());
            userCollect.setImage(data.getImages().split(",")[0]);
            userCollect.setTitle(data.getTitle());
            userCollect.setPrice(data.getPrice());
            userCollect.setDiscountState(data.getDiscountState());
            userCollect.setDiscountPrice(data.getDiscountPrice());
        }
        if (type==1) {//服务

            UserCollect rult = userCollectMapper.selectOne(new QueryWrapper<UserCollect>()
                    .eq("user_id", userId)
                    .eq("goods_id", userCollectParam.getGoodsId())
            );
            if (Objects.nonNull(rult)) {
                throw new JSYException(-1, "该服务已经收藏");
            }

            Goods data = goodsClient.getByGoods(userCollectParam.getGoodsId()).getData();
            if (Objects.isNull(data)) {
                throw new JSYException(-1,"未找到该服务！");
            }
            userCollect.setUserId(userId);
            userCollect.setGoodsId(goodsId);
            userCollect.setType(1);
            userCollect.setShopId(data.getShopId());
            userCollect.setImage(data.getImages().split(",")[0]);
            userCollect.setTitle(data.getTitle());
            userCollect.setPrice(data.getPrice());
            userCollect.setDiscountState(data.getDiscountState());
            userCollect.setDiscountPrice(data.getDiscountPrice());
        }
        if (type==2){//套餐
            UserCollect rult = userCollectMapper.selectOne(new QueryWrapper<UserCollect>()
                    .eq("user_id", userId)
                    .eq("menu_id", userCollectParam.getMenuId())
            );
            if (Objects.nonNull(rult)){
                throw new JSYException(-1,"该套餐已经收藏");
            }
            SetMenuDto data = setMenuClient.SetMenuList(menuId).getData();
            if (Objects.isNull(data)){
                throw new JSYException(-1,"未找到该套餐！");
            }
            userCollect.setType(2);
            userCollect.setUserId(userId);
            userCollect.setShopId(data.getShopId());
            userCollect.setMenuId(menuId);
            userCollect.setImage(data.getImages().split(",")[0]);
            userCollect.setTitle(data.getName());
            userCollect.setPrice(data.getRealPrice());
            userCollect.setDiscountState(data.getDiscountState());
            userCollect.setDiscountPrice(data.getSellingPrice());

        }
        if (type==3){//店铺
            UserCollect rult = userCollectMapper.selectOne(new QueryWrapper<UserCollect>()
                    .eq("user_id", userId)
                    .eq("shop_id", userCollectParam.getMenuId())
            );
            if (Objects.nonNull(rult)){
                throw new JSYException(-1,"该商店已经收藏！");
            }

            NewShopDto data = newShopClient.get(shopId).getData();
            if (Objects.isNull(data)){
                throw new JSYException(-1,"未找到该店铺！");
            }
            userCollect.setType(3);
            userCollect.setUserId(userId);
            userCollect.setShopId(shopId);
            userCollect.setImage(data.getShopLogo());
            SelectShopCommentScoreDto rut = commentClent.selectShopCommentScore(shopId).getData();
            userCollect.setShopScore(Objects.isNull(rut)?5:rut.getScore());
            userCollect.setTitle(data.getShopName());
            String shopTreeId = data.getShopTreeId();
            if (Objects.nonNull(shopTreeId)){
                String shopTreeIdName = getShopTreeIdName(shopTreeId.split(","));
                userCollect.setShopTypeName(shopTreeIdName);
            }
        }
        int insert = userCollectMapper.insert(userCollect);
        if (insert!=0){
            Long id = userCollect.getId();
            UserCollect rultUserCollect = userCollectMapper.selectById(id);
            return rultUserCollect;
        }
        return null;
    }


    /**
     * 分页查询收藏的商品/服务、套餐、店铺
     * @param userCollectQuery 查询对象
     * @return PageList 分页对象
     */
    @Override
    public PageInfo<UserCollect>  userCollectPageList(UserCollectQuery userCollectQuery) {
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
        if (Objects.isNull(loginUser)){
            new JSYException(-1,"用户认证失败！");
        }
        Long userId = loginUser.getId();//用户id
        Page<UserCollect> page = new Page<>(userCollectQuery.getPage(), userCollectQuery.getRows());
        Page<UserCollect> selectPage = userCollectMapper.selectPage(page, new QueryWrapper<UserCollect>().eq("user_id", userId));
        PageInfo<UserCollect> pageInfo = new PageInfo<>();
        pageInfo.setSize(selectPage.getSize());
        pageInfo.setTotal(selectPage.getTotal());
        pageInfo.setCurrent(selectPage.getCurrent());
        pageInfo.setRecords(selectPage.getRecords());
        return pageInfo;
    }

    /**
     * 收藏按钮状态 亮(已收藏)：true   灰色 ：false
     */
    @Override
    public Boolean userCollectState(Integer type, Long id) {
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
        if (Objects.isNull(loginUser)){
            new JSYException(-1,"用户认证失败！");
        }
        Long userId = loginUser.getId();//用户id
        if (Objects.isNull(type)){
            throw new JSYException(-1,"收藏类型不能为空！");
        }
        if (Objects.isNull(id)){
            throw new JSYException(-1,"id不能为空！");
        }
        /**
         * 收藏类型：0 商品  1:服务  2：套餐  3：商店
         */
        if (type==0){
            UserCollect goodsService = userCollectMapper.selectOne(new QueryWrapper<UserCollect>().eq("goods_id", id).eq("user_id",userId));
            if (Objects.nonNull(goodsService)){//已收藏
                return true;
            }else {
                return false;
            }
        }
        if (type==1){
            UserCollect goodsService = userCollectMapper.selectOne(new QueryWrapper<UserCollect>().eq("goods_id", id).eq("user_id",userId));
            if (Objects.nonNull(goodsService)){//已收藏
                return true;
            }else {
                return false;
            }

        }
        if (type==2){
            UserCollect menu = userCollectMapper.selectOne(new QueryWrapper<UserCollect>().eq("menu_id", id).eq("user_id",userId));
            if (Objects.nonNull(menu)){
                return true;
            }else {
                return false;
            }

        }
        if (type==3){
            UserCollect shop = userCollectMapper.selectOne(new QueryWrapper<UserCollect>().eq("shop_id", id).eq("user_id",userId));
            if (Objects.nonNull(shop)){
                return true;
            }else {
                return false;
            }
        }
        return null;
    }

    /**
     * 取消收藏
     * @param type
     * @param id
     */
    @Override
    @Transactional
    public void cancelUserCollect(Integer type, Long id) {
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
        if (Objects.isNull(loginUser)){
            new JSYException(-1,"用户认证失败！");
        }
        Long userId = loginUser.getId();//用户id

        /**
         * 收藏类型：0 商品  1:服务  2：套餐  3：商店
         */
        if (type==0){
            userCollectMapper.delete(new QueryWrapper<UserCollect>().eq("goods_id",id).eq("user_id",userId));

        }
        if (type==1){
            userCollectMapper.delete(new QueryWrapper<UserCollect>().eq("goods_id",id).eq("user_id",userId));

        }
        if (type==2){
            userCollectMapper.delete(new QueryWrapper<UserCollect>().eq("menu_id",id).eq("user_id",userId));

        }
        if (type==3){
            userCollectMapper.delete(new QueryWrapper<UserCollect>().eq("shop_id",id).eq("user_id",userId));
        }
    }

    /**
     * 购物车移入收藏
     */
    @Override
    @Transactional
    public void userCartToCollect(List<Long> shopIds) {
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
        if (Objects.isNull(loginUser)){
            new JSYException(-1,"用户认证失败！");
        }

        if (shopIds.size()==0){
            throw new JSYException(-1,"传入的商店id不能为空！");
        }
        QueryUserCartDto data = shoppingCartClient.queryUserCart(shopIds).getData();

        List<NewShopInfo> shopDtoList = data.getNewShopDtoList();
        for (NewShopInfo newShopDto : shopDtoList) {
            UserCollect userCollect = new UserCollect();
            userCollect.setType(3);
            userCollect.setShopId(newShopDto.getId());
            userCollect.setTitle(newShopDto.getShopName());
            userCollect.setImage(newShopDto.getShopLogo());
            userCollect.setUserId(loginUser.getId());
            String shopTreeId = newShopDto.getShopTreeId();
            if (Objects.nonNull(shopTreeId)){
                String shopTreeIdName = getShopTreeIdName(shopTreeId.split(","));
                userCollect.setShopTypeName(Objects.isNull(shopTreeIdName)?null:shopTreeIdName);
            }
            SelectShopCommentScoreDto rut = commentClent.selectShopCommentScore(newShopDto.getId()).getData();
            userCollect.setShopScore(Objects.isNull(rut)?5:rut.getScore());
            userCollectMapper.insert(userCollect);
        }
        List<ShoppingCart> goodsList = data.getGoodsList();
        for (ShoppingCart cart : goodsList) {
            UserCollect userCollect = new UserCollect();
            BeanUtils.copyProperties(cart,userCollect);
            userCollect.setMenuId(cart.getSetMenuId());
            userCollectMapper.insert(userCollect);
        }
    }


    private String getShopTreeIdName(String[] split) {
        String shopTreeIdName = "";
        if (split.length<=2){
            //医疗
            Tree data = treeClient.getTree(Long.valueOf(split[split.length-1])).getData();
            System.out.println("医疗");
            return data.getName();
        }else {
            //其他
            String id = split[split.length-1];
            Tree data1= treeClient.getTree(Long.valueOf(split[split.length-1])).getData();
            Tree data2= treeClient.getTree(Long.valueOf(split[split.length-2])).getData();
            System.out.println("其他");
            return data2.getName()+"-"+data1.getName();

        }
    }
}
