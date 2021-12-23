package com.jsy.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jsy.basic.util.PageInfo;
import com.jsy.basic.util.exception.JSYException;
import com.jsy.clent.CommentClent;
import com.jsy.client.*;
import com.jsy.domain.Goods;
import com.jsy.domain.ShoppingCart;
import com.jsy.domain.Tree;
import com.jsy.domain.UserCollect;
import com.jsy.dto.NewShopDto;
import com.jsy.dto.QueryUserCartDto;
import com.jsy.dto.SelectShopCommentScoreDto;
import com.jsy.dto.SetMenuDto;
import com.jsy.mapper.UserCollectMapper;
import com.jsy.param.UserCollectParam;
import com.jsy.query.UserCollectQuery;
import com.jsy.service.IUserCollectService;
import com.zhsj.baseweb.support.ContextHolder;
import com.zhsj.baseweb.support.LoginUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    public void addorDelUserCollect(UserCollectParam userCollectParam) {
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
        if (Objects.isNull(loginUser)){
            new JSYException(-1,"用户认证失败！");
        }
        Long userId = loginUser.getId();//用户id
        Integer type = userCollectParam.getType();
        Long goodsId = userCollectParam.getGoodsId();
        Long shopId = userCollectParam.getShopId();
        Long menuId = userCollectParam.getMenuId();
        Boolean state = userCollectParam.getState();
        if (Objects.isNull(state)){
            throw new JSYException(-1,"状态不能为空！");
        }

        if (state==false){//取消收藏
            if (type==0){//商品
                this.cancelUserCollect(0,goodsId);
            }
            if (type==1){//服务
                this.cancelUserCollect(1,goodsId);
            }
            if (type==2){//套餐
                this.cancelUserCollect(2,menuId);
            }
            if (type==3){//商家
                this.cancelUserCollect(3,shopId);
            }
            return ;
        }
        UserCollect userCollect = new UserCollect();
        if (type==0) {//商品

            UserCollect rult = userCollectMapper.selectOne(new QueryWrapper<UserCollect>()
                    .eq("user_id", userId)
                    .eq("goods_id", goodsId)
                    .eq("type",0)
            );
            if (Objects.nonNull(rult)) {
                throw new JSYException(-1, "该商品已经收藏");
            }

            Goods data = goodsClient.getByGoods(goodsId).getData();
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
                    .eq("goods_id", goodsId)
                    .eq("type",1)
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
                    .eq("menu_id", menuId)
                    .eq("type",2)
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
                    .eq("shop_id", shopId)
                    .eq("type",3)
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
            userCollect.setPhone(data.getShopPhone());
            SelectShopCommentScoreDto rut = commentClent.selectShopCommentScore(shopId).getData();
            userCollect.setShopScore(Objects.isNull(rut)?5:rut.getScore());
            userCollect.setTitle(data.getShopName());
            String shopTreeId = data.getShopTreeId();
            if (Objects.nonNull(shopTreeId)){
                String shopTreeIdName = getShopTreeIdName(shopTreeId.split(","));
                userCollect.setShopTypeName(shopTreeIdName);
            }
        }
        userCollectMapper.insert(userCollect);
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

        List<UserCollect> records = selectPage.getRecords();
        List<UserCollect> collect = records.stream().sorted(Comparator.comparing(UserCollect::getCreateTime).reversed()).collect(Collectors.toList());
        PageInfo<UserCollect> pageInfo = new PageInfo<>();
        pageInfo.setSize(selectPage.getSize());
        pageInfo.setTotal(selectPage.getTotal());
        pageInfo.setCurrent(selectPage.getCurrent());
        pageInfo.setRecords(collect);
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
            UserCollect goodsService = userCollectMapper.selectOne(new QueryWrapper<UserCollect>()
                    .eq("goods_id", id)
                    .eq("user_id",userId)
                    .eq("type",0));
            if (Objects.nonNull(goodsService)){//已收藏
                return true;
            }else {
                return false;
            }
        }
        if (type==1){
            UserCollect goodsService = userCollectMapper.selectOne(new QueryWrapper<UserCollect>()
                    .eq("goods_id", id)
                    .eq("user_id",userId)
                    .eq("type",1));
            if (Objects.nonNull(goodsService)){//已收藏
                return true;
            }else {
                return false;
            }

        }
        if (type==2){
            UserCollect menu = userCollectMapper.selectOne(new QueryWrapper<UserCollect>()
                    .eq("menu_id", id)
                    .eq("user_id",userId)
                    .eq("type",2)
            );
            if (Objects.nonNull(menu)){
                return true;
            }else {
                return false;
            }

        }
        if (type==3){
            UserCollect shop = userCollectMapper.selectOne(new QueryWrapper<UserCollect>()
                    .eq("shop_id", id)
                    .eq("user_id",userId)
                    .eq("type",3)
            );
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
            userCollectMapper.delete0(id,userId);
        }
        if (type==1){
            userCollectMapper.delete1(id,userId);
        }
        if (type==2){
            userCollectMapper.delete2(id,userId);
        }
        if (type==3){
            userCollectMapper.delete3(id,userId);
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


        //查询用户购物车商店信息
        QueryUserCartDto data = shoppingCartClient.queryUserCart(shopIds).getData();
       /* List<NewShopInfo> shopDtoList = data.getNewShopDtoList();

        List<NewShopInfo> shopDtoListCollect = shopDtoList.stream().filter(x -> {
            UserCollect userCollect = userCollectMapper.selectOne(new QueryWrapper<UserCollect>().eq("type", 3).eq("shop_id", x.getId()));
            if (Objects.isNull(userCollect)) {//没收藏过的才添加到收藏
                return true;
            }
            return false;
        }).collect(Collectors.toList());


        for (NewShopInfo newShopDto : shopDtoListCollect) {
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
        }*/


        //查询用户购物车商品信息
        List<ShoppingCart> goodsList = data.getGoodsList();
        List<ShoppingCart> goodsListCollect = goodsList.stream().filter(x -> {
            if (x.getType()==0 || x.getType()==1){
                UserCollect userCollect = userCollectMapper.selectOne(new QueryWrapper<UserCollect>()
                        .eq("type",x.getType())
                        .eq("goods_id", x.getGoodsId())
                );
                if (Objects.isNull(userCollect)){//没收藏过的才添加到收藏
                    return true;
                }
            }
            if (x.getType()==2){
                UserCollect userCollect = userCollectMapper.selectOne(new QueryWrapper<UserCollect>()
                        .eq("type",2)
                        .eq("menu_id", x.getSetMenuId())
                );
                if (Objects.isNull(userCollect)){//没收藏过的才添加到收藏
                    return true;
                }
            }
            return false;
        }).collect(Collectors.toList());
        for (ShoppingCart cart : goodsListCollect) {
            UserCollect userCollect = new UserCollect();
            BeanUtils.copyProperties(cart,userCollect);
            userCollect.setImage(cart.getImages().split(",")[0]);
            userCollect.setMenuId(cart.getSetMenuId());
            userCollectMapper.insert(userCollect);
        }
    }

    /**
     * 列表删除多条收藏记录
     * @param ids
     * @return
     */
    @Override
    @Transactional
    public void delMultiUserCollect(List<Long> ids) {
        userCollectMapper.myDeleteBatchIds(ids);
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
