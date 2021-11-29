package com.jsy.service.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jsy.basic.util.MyPageUtils;
import com.jsy.basic.util.PageInfo;
import com.jsy.basic.util.exception.JSYException;
import com.jsy.basic.util.utils.*;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.clent.CommentClent;
import com.jsy.client.GoodsClient;
import com.jsy.client.SetMenuClient;
import com.jsy.client.TreeClient;
import com.jsy.domain.Goods;
import com.jsy.domain.NewShop;
import com.jsy.domain.SetMenu;
import com.jsy.domain.Tree;
import com.jsy.dto.*;
import com.jsy.mapper.NewShopMapper;
import com.jsy.parameter.NewShopParam;
import com.jsy.parameter.NewShopSetParam;
import com.jsy.query.MainSearchQuery;
import com.jsy.query.NewShopQuery;
import com.jsy.service.INewShopService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 新——店铺表 服务实现类
 * </p>
 *
 * @author yu
 * @since 2021-11-08
 */
@Service
public class NewShopServiceImpl extends ServiceImpl<NewShopMapper, NewShop> implements INewShopService {

    @Resource
    private NewShopMapper shopMapper;
    @Resource
    private TreeClient treeClient;

    @Resource
    private GoodsClient goodsClient;
    @Resource
    private SetMenuClient setMenuClient;

    @Resource
    private CommentClent commentClent;

    /**
     * @param shopPacketParam
     * @Description: 创建店铺
     * @Param: [shopPacketParam]
     * @Return: void
     * @Author: Tian
     * @Date: 2021/11/8-11:49
     */
    @Override
    public void addNewShop(NewShopParam shopPacketParam) {
        boolean mobile = RegexUtils.isLandline(shopPacketParam.getMobile());//验证电话
        if (!mobile) {
            throw new JSYException(-1, "座机电话格式错误");
        }
        boolean phone = RegexUtils.isMobile(shopPacketParam.getShopPhone());
        if (!phone) {
            throw new JSYException(-1, "经营者/法人电话格式错误");
        }

        if (shopPacketParam.getShopName().length() > 15) {
            throw new JSYException(-1, "店铺名太长");
        }
        List<String> shopLogo = shopPacketParam.getShopLogo();
        if (shopLogo.size() > 1) {
            throw new JSYException(-1, "照片只能上传1张");
        }

        NewShop newShop = new NewShop();
        BeanUtils.copyProperties(shopPacketParam, newShop);
        //行业分类的  一级二级三级标题  逗号分隔，最少有俩级
        String treeId = shopPacketParam.getShopTreeId();
        //数组
        String[] split = treeId.split(",");
        Long aLong = Long.valueOf(split[1]);
        System.out.println(aLong);
        Tree tree = treeClient.getTree(aLong).getData();
        //1是服务行业  0 套餐行业
        if (tree.getParentId() == 1) {
            newShop.setType(1);
        } else {
            newShop.setType(0);
        }
        //获取登录用户
//        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
//        newShop.setOwnerUuid(loginUser.getId().toString());
//        String lonLat = GouldUtil.getLonLat(shopPacketParam.getAddressDetail());
//        String[] split1 = lonLat.split(",");
//        if (split1!=null){
//            //经度
//            newShop.setLongitude(BigDecimal.valueOf(Long.valueOf(split[0])));
//            //维度
//            newShop.setLatitude(BigDecimal.valueOf(Long.valueOf(split[1])));
//        }
//        newShop.setUuid(UUIDUtils.getUUID());
        shopMapper.insert(newShop);
    }

    @Override
    public NewShopPreviewDto getPreviewDto(Long shopId) {
        NewShop newShop = shopMapper.selectById(shopId);
        NewShopPreviewDto newShopPreviewDto = new NewShopPreviewDto();
        String treeId = newShop.getShopTreeId();
        String[] split = treeId.split(",");
        String shopTreeIdName = getString(split);
//        for (String s : split) {
//            Tree tree = treeClient.getTree(Long.valueOf(s)).getData();
//            shopTreeIdName = shopTreeIdName + "-" + tree.getName();
//        }
        newShopPreviewDto.setShopTreeIdName(shopTreeIdName);
        BeanUtils.copyProperties(newShop, newShopPreviewDto);
        return newShopPreviewDto;
    }

    @Override
    public void update(NewShopParam shopPacketParam) {
        boolean mobile = RegexUtils.isLandline(shopPacketParam.getMobile());//验证电话
        if (!mobile) {
            throw new JSYException(-1, "座机电话格式错误");
        }
        boolean phone = RegexUtils.isMobile(shopPacketParam.getShopPhone());
        if (!phone) {
            throw new JSYException(-1, "经营者/法人电话格式错误");
        }

        if (shopPacketParam.getShopName().length() > 15) {
            throw new JSYException(-1, "店铺名太长");
        }
        List<String> shopLogo = shopPacketParam.getShopLogo();
        if (shopLogo.size() > 1) {
            throw new JSYException(-1, "照片只能上传1张");
        }

        NewShop newShop = new NewShop();
        BeanUtils.copyProperties(shopPacketParam, newShop);
        //行业分类的  二级三级标题  逗号分隔
        String treeId = shopPacketParam.getShopTreeId();
        //数组
        String[] split = treeId.split(",");
        Long aLong = Long.valueOf(split[1]);
        Tree tree = treeClient.getTree(aLong).getData();

        //1是服务行业  0 套餐行业
        if (tree.getParentId() == 1) {
            newShop.setType(1);
        } else {
            newShop.setType(0);
        }
        //获取登录用户
//        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
//        newShop.setOwnerUuid(loginUser.getId().toString());
        // 返回输入地址address的经纬度信息, 格式是 经度,纬度
//        String lonLat = GouldUtil.getLonLat(shopPacketParam.getAddressDetail());
//        String[] split1 = lonLat.split(",");
//        if (split1!=null){
//            //经度
//            newShop.setLongitude(BigDecimal.valueOf(Long.valueOf(split[0])));
//            //维度
//            newShop.setLatitude(BigDecimal.valueOf(Long.valueOf(split[1])));
//        }
//        newShop.setUuid(UUIDUtils.getUUID());
        shopMapper.updateById(newShop);
    }

    @Override
    public void setSetShop(NewShopSetParam shopSetParam) {
        NewShop newShop = shopMapper.selectById(shopSetParam.getId());
        BeanUtils.copyProperties(shopSetParam, newShop);
        System.out.println(newShop);
        shopMapper.updateById(newShop);
    }

    //C端查询店铺
    @Override
    public PageInfo<NewShopRecommendDto> getShopAllList(NewShopQuery shopQuery) {
        System.out.println(shopQuery);
       /* List<NewShop> newShopList = shopMapper.selectList(null);
        List<NewShopRecommendDto> shopList = new ArrayList<>();
        for (NewShop newShop : newShopList) {
            String shopTreeIdName = "";
            String[] split = newShop.getShopTreeId().split(",");
            for (String s : split) {
                shopTreeIdName = getString(split);
                //判断是否的当前分类下面的店铺
                if (Long.valueOf(s) == treeId) {
                    //店铺的地址名称
                    String addressDetail = newShop.getAddressDetail();
                    long byAddress = GouldUtil.getDistanceByAddress(addressDetail, location);
                    //默认3km
                    if (byAddress <= 3000) {
                        NewShopRecommendDto recommendDto = new NewShopRecommendDto();
                        BeanUtils.copyProperties(newShop, recommendDto);
                        recommendDto.setDistance(String.valueOf(byAddress/1000+"km"));
                        //把商品最近发布的东西查出来   有可能是服务又可能是商品 有可能是 套餐
                        Goods goods = goodsClient.getShopIdGoods(newShop.getId()).getData();
                        SetMenu menu = setMenuClient.getShopIdMenus(newShop.getId()).getData();
                        recommendDto.setShopName(newShop.getShopName());
                        recommendDto.setShopTreeIdName(shopTreeIdName);
                        if (goods!=null||menu!=null){
                            if (goods!=null&&menu!=null){
                                if (goods.getCreateTime().compareTo(menu.getCreateTime()) < 0) {
                                    recommendDto.setTitle(menu.getName());
                                    recommendDto.setPrice(menu.getSellingPrice());
                                    shopList.add(recommendDto);
                                } else {
                                    recommendDto.setTitle(goods.getTitle());
                                    recommendDto.setPrice(goods.getDiscountPrice());
                                    shopList.add(recommendDto);
                                }
                            }else if (goods!=null&&menu==null){
                                        recommendDto.setTitle(goods.getTitle());
                                        recommendDto.setPrice(goods.getDiscountPrice());
                                        shopList.add(recommendDto);
                                    }else {
                                        recommendDto.setTitle(menu.getName());
                                        recommendDto.setPrice(menu.getSellingPrice());
                                        shopList.add(recommendDto);
                            }
                        }else {
                            recommendDto.setTitle(null);
                            recommendDto.setPrice(null);
                        }
                    }

                }
            }

        }*/
        //用户地址
        //分类id
        Long treeId = shopQuery.getTreeId();
        //获取用户经纬度
        BigDecimal longitude =  new BigDecimal((shopQuery.getLongitude()));
        BigDecimal latitude = new BigDecimal((shopQuery.getLatitude()));

        List<NewShop> newShopList = shopMapper.selectAddress(longitude,latitude);
        List<NewShopRecommendDto> shopList = new ArrayList<>();
//        long byAddress = 0;
        for (NewShop newShop : newShopList) {
                //判断是否的当前分类下面的店
                    //店铺的地址名称
//                    String addressDetail = newShop.getAddressDetail();
//                    if (StringUtils.isNotEmpty(longitude)){
//                        byAddress = GouldUtil.getDistanceByAddress(addressDetail, longitude);
//                    }
                    //默认3km
                        NewShopRecommendDto recommendDto = new NewShopRecommendDto();
                        BeanUtils.copyProperties(newShop, recommendDto);
                        recommendDto.setShopName(newShop.getShopName());

                        String[] ids = newShop.getShopTreeId().split(",");
                        Tree tree = treeClient.getTree(Long.valueOf(ids[ids.length - 1])).getData();
                        if (Objects.nonNull(tree)){
                            recommendDto.setShopTreeIdName(tree.getName());
                        }

                        //评分
                        SelectShopCommentScoreDto data = commentClent.selectShopCommentScore(newShop.getId()).getData();
                        recommendDto.setGrade(data.getScore());

                        //把商品最近发布的东西查出来   有可能是服务又可能是商品 有可能是 套餐
                        Goods goods = goodsClient.latelyGoods(newShop.getId()).getData();
                        if (goods!=null){
                            recommendDto.setTitle(goods.getTitle());
                            recommendDto.setPrice(goods.getPrice());
                            shopList.add(recommendDto);
                        }
        }
        PageInfo<NewShopRecommendDto> pageInfo = MyPageUtils.pageMap(shopQuery.getPage(), shopQuery.getRows(), shopList);

        return pageInfo;
    }

    private String getString(String[] split) {
        String shopTreeIdName = "";
        for (String s : split) {
            Tree tree = treeClient.getTree(Long.valueOf(s)).getData();
            shopTreeIdName = shopTreeIdName + "-" + tree.getName();
        }
        //截取  第二个-  开始
        String s1= shopTreeIdName.substring(shopTreeIdName.indexOf("-", shopTreeIdName.indexOf("-") + 1));
        return s1.substring(1);

    }
    /**
     * 首页搜索
     */
    @Override
    public PageInfo<MyNewShopDto> mainSearch(MainSearchQuery mainSearchQuery) {

        String keyword = mainSearchQuery.getKeyword();
//        String location = mainSearchQuery.getLocation();


        List<NewShop> newShops= shopMapper.mainSearch(keyword);
        if (newShops.size()==0){
          return new PageInfo<>();
        }

        ArrayList<MyNewShopDto> list = new ArrayList<>();
        for (NewShop newShop : newShops) {
            MyNewShopDto myNewShopDto = new MyNewShopDto();
            myNewShopDto.setId(newShop.getId());
            myNewShopDto.setShopName(newShop.getShopName());
            myNewShopDto.setGrade(5.0f);
            //查询
            Goods goods = goodsClient.latelyGoods(newShop.getId()).getData();
            if (Objects.nonNull(goods)){
                myNewShopDto.setTitle(goods.getTitle());
                myNewShopDto.setPrice(goods.getPrice());
            }
            String l1= newShop.getLongitude()+","+newShop.getLatitude();
            String l2= mainSearchQuery.getLongitude()+","+mainSearchQuery.getLatitude();
            long addr = GouldUtil.getApiDistance(l1, l2);
            myNewShopDto.setDistance(addr/1000+"km");

            String[] ids = newShop.getShopTreeId().split(",");

            Tree tree = treeClient.getTree(Long.valueOf(ids[ids.length - 1])).getData();
            if (Objects.nonNull(tree)){
                myNewShopDto.setShopTreeIdName(tree.getName());
            }
            list.add(myNewShopDto);

        }
        PageInfo<MyNewShopDto> pageInfo = MyPageUtils.pageMap(mainSearchQuery.getPage(), mainSearchQuery.getRows(), list);
        return pageInfo;

    }

    @Override
    public List<NewShopDto> batchIds(List<Long> ids) {
        List<NewShop> newShops = baseMapper.selectBatchIds(ids);
        List<NewShopDto> dtoList= new ArrayList<>();
        for (NewShop newShop : newShops) {
            NewShopDto newShopDto = new NewShopDto();
            BeanUtils.copyProperties(newShop,newShopDto);
            dtoList.add(newShopDto);
        }
        return dtoList;
    }
 /**
  * @author Tian
  * @since 2021/11/29-9:35
  * @description 大后台分页
  **/
    @Override
    public PageInfo<NewShopDto> newShopPage(NewShopQuery shopQuery) {
        List<NewShop> shopList = shopMapper.selecctNewShopPage(shopQuery);
        List<NewShopDto> dtoList = new ArrayList<>();
        for (NewShop newShop : shopList) {
            NewShopDto newShopDto = new NewShopDto();
            BeanUtils.copyProperties(newShop,newShopDto);
            dtoList.add(newShopDto);
        }
        PageInfo<NewShopDto> newShopDtoPageInfo = MyPageUtils.pageMap(shopQuery.getPage(), shopQuery.getRows(), dtoList);
        return newShopDtoPageInfo;
    }


}
