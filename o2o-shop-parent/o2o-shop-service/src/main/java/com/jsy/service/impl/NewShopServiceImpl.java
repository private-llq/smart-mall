package com.jsy.service.impl;

import com.jsy.basic.util.MyPageUtils;
import com.jsy.basic.util.PageInfo;
import com.jsy.basic.util.exception.JSYException;
import com.jsy.basic.util.utils.*;
import com.jsy.clent.CommentClent;
import com.jsy.client.GoodsClient;
import com.jsy.client.SetMenuClient;
import com.jsy.client.TreeClient;
import com.jsy.domain.Goods;
import com.jsy.domain.NewShop;
import com.jsy.domain.Tree;
import com.jsy.dto.*;
import com.jsy.mapper.NewShopMapper;
import com.jsy.parameter.NewShopModifyParam;
import com.jsy.parameter.NewShopParam;
import com.jsy.parameter.NewShopQualificationParam;
import com.jsy.parameter.NewShopSetParam;
import com.jsy.query.MainSearchQuery;
import com.jsy.query.NewShopQuery;
import com.jsy.service.INewShopService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhsj.baseweb.support.ContextHolder;
import com.zhsj.baseweb.support.LoginUser;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

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
     * @Description: 创建店铺基本信息
     * @Param: [shopPacketParam]
     * @Return: void
     * @Author: Tian
     * @Date: 2021/11/8-11:49
     */
    @Override
    public Long addNewShop(NewShopParam shopPacketParam) {
//        boolean mobile = RegexUtils.isLandline(shopPacketParam.getMobile());//验证电话
//        if (!mobile) {
//            throw new JSYException(-1, "座机电话格式错误");
//        }
//        boolean phone = RegexUtils.isMobile(shopPacketParam.getShopPhone());
//        if (!phone) {
//            throw new JSYException(-1, "经营者/法人电话格式错误");
//        }

        if (shopPacketParam.getShopName().length() > 15) {
            throw new JSYException(-1, "店铺名太长");
        }
        List<String> shopLogo = shopPacketParam.getShopLogo();
        if (shopLogo.size() > 1) {
            throw new JSYException(-1, "照片只能上传1张");
        }

        NewShop newShop = new NewShop();
        BeanUtils.copyProperties(shopPacketParam, newShop);

        newShop.setShopLogo(shopPacketParam.getShopLogo().toString());
        //店铺拥有者id
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
        newShop.setOwnerUuid(loginUser.getId());

        //设置state 状态   "审核状态 0未审核 1已审核 2审核未通过  3资质未认证")
        newShop.setState(3);


        //行业分类的  一级二级三级标题  逗号分隔，最少有俩级
        String treeId = shopPacketParam.getShopTreeId();
        //数组
        try {
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
        } catch (NumberFormatException e) {
            e.printStackTrace();
            throw new JSYException(-1,"店铺创建分类错误");
        }
        shopMapper.insert(newShop);
        Long shopId = newShop.getId();
        return shopId;
    }

     /**
      * @author Tian
      * @since 2021/12/1-11:08
      * @description 根据店铺id预览店铺基本信息
      **/
    @Override
    public NewShopPreviewDto getPreviewDto(Long shopId) {
        NewShop newShop = shopMapper.selectById(shopId);
        if (newShop==null){
            throw new JSYException(-1,"店铺为空");
        }
        NewShopPreviewDto newShopPreviewDto = new NewShopPreviewDto();
        try {
            if (newShop.getShopTreeId()!=null){
                String treeId = newShop.getShopTreeId();

                String[] split = treeId.split(",");
                String shopTreeIdName = getString(split);
                newShopPreviewDto.setShopTreeIdName(shopTreeIdName);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new JSYException(-1,"店铺分类错误");
        }

        BeanUtils.copyProperties(newShop, newShopPreviewDto);

        return newShopPreviewDto;
    }
 /**
  * @author Tian
  * @since 2021/12/1-11:09
  * @description 修改所有店铺的参数
  **/
    @Override
        public void update(NewShopModifyParam modifyParam) {
//        boolean mobile = RegexUtils.isLandline(modifyParam.getMobile());//验证电话
//        if (!mobile) {
//            throw new JSYException(-1, "座机电话格式错误");
//        }
        if(!RegexUtils.isIDCard(modifyParam.getIdNumber())){
            throw  new JSYException(-1,"身份证号有误");
        }
        boolean phone = RegexUtils.isMobile(modifyParam.getShopPhone());
        if (!phone) {
            throw new JSYException(-1, "经营者/法人电话格式错误");
        }

        if (modifyParam.getShopName().length() > 15) {
            throw new JSYException(-1, "店铺名太长");
        }
        List<String> shopLogo = modifyParam.getShopLogo();
        if (shopLogo.size() > 1) {
            throw new JSYException(-1, "照片只能上传1张");
        }

        NewShop newShop = new NewShop();
        BeanUtils.copyProperties(modifyParam, newShop);
        try {
            //行业分类的  二级三级标题  逗号分隔
            String treeId = modifyParam.getShopTreeId();
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
        } catch (NumberFormatException e) {
            e.printStackTrace();
            throw new JSYException(-1,"店铺分类错误");
        }
        newShop.setState(0);

        shopMapper.updateById(newShop);
    }

    /**
     * @author Tian
     * @since 2021/12/1-11:09
     * @description 修改店铺设置
     **/
    @Override
    public void setSetShop(NewShopSetParam shopSetParam) {
        NewShop newShop = shopMapper.selectById(shopSetParam.getId());
        BeanUtils.copyProperties(shopSetParam, newShop);
        System.out.println(newShop);
        shopMapper.updateById(newShop);
    }

     /**
      * @author Tian
      * @since 2021/12/1-11:09
      * @description C端查询店铺
      **/
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
            System.out.println("评分");
                        //评分
                        SelectShopCommentScoreDto data = commentClent.selectShopCommentScore(newShop.getId()).getData();
            System.out.println(data.getScore());
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
            try {
                if (newShop.getShopTreeId()!=null){
                    String treeId = newShop.getShopTreeId();

                    String[] split = treeId.split(",");
                    String shopTreeIdName = getString(split);
                    newShopDto.setShopTreeIdName(shopTreeIdName);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new JSYException(-1,"店铺分类错误");
            }

            BeanUtils.copyProperties(newShop,newShopDto);
            dtoList.add(newShopDto);
        }


        PageInfo<NewShopDto> newShopDtoPageInfo = MyPageUtils.pageMap(shopQuery.getPage(), shopQuery.getRows(), dtoList);
        return newShopDtoPageInfo;
    }

    @Override
    public PageInfo<NewShopHotDto> getHot(NewShopQuery newShopQuery) {
        List<NewShopHotDto> newShopHotDtos = shopMapper.selectHot();
        PageInfo<NewShopHotDto> hotDtoPageInfo = MyPageUtils.pageMap(newShopQuery.getPage(), newShopQuery.getRows(), newShopHotDtos);
        return hotDtoPageInfo;
    }
 /**
  * @author Tian
  * @since 2021/12/1-11:10
  * @description 新增店铺资质认证
  **/
 @Override
    public void addQualification(NewShopQualificationParam qualificationParam) {
        if(!RegexUtils.isIDCard(qualificationParam.getIdNumber())){
            throw  new JSYException(-1,"身份证号有误");
        }
        if (!RegexUtils.isMobile(qualificationParam.getShopPhone())) {
            throw new JSYException(-1, "经营者/法人电话格式错误");
        }
        NewShop newShop = new NewShop();
        BeanUtils.copyProperties(qualificationParam,newShop);
        //设置state 状态   "审核状态 0未审核 1已审核 2审核未通过  3资质未认证")
        newShop.setState(0);
        shopMapper.updateById(newShop);
    }
 /**
  * @author Tian
  * @since 2021/12/1-11:19
  * @description 店铺预览基本信息查询
  **/
    @Override
    public NewShopBasicDto selectBasic(Long shopId) {
        NewShop newShop = shopMapper.selectById(shopId);
//        if (newShop.getState()==3){
//
//        }
        NewShopBasicDto basicDto = new NewShopBasicDto();
        BeanUtils.copyProperties(newShop,basicDto);
        try {
            if (newShop.getShopTreeId()!=null){
                String treeId = newShop.getShopTreeId();

                String[] split = treeId.split(",");
                String shopTreeIdName = getString(split);
                basicDto.setShopTreeIdName(shopTreeIdName);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new JSYException(-1,"店铺分类错误");
        }
        return basicDto;
    }
 /**
  * @author Tian
  * @since 2021/12/1-11:47
  * @description 修改店铺基本信息填写
  **/
    @Override
    public void updateBasic(NewShopParam shopPacketParam) {

        if (shopPacketParam.getShopName().length() > 15) {
            throw new JSYException(-1, "店铺名太长");
        }
        List<String> shopLogo = shopPacketParam.getShopLogo();
        if (shopLogo.size() > 1) {
            throw new JSYException(-1, "照片只能上传1张");
        }

        NewShop newShop = new NewShop();
        BeanUtils.copyProperties(shopPacketParam, newShop);

        newShop.setShopLogo(shopPacketParam.getShopLogo().toString());
        //店铺拥有者id
//        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
//        newShop.setOwnerUuid(loginUser.getId());

        //设置state 状态   "审核状态 0未审核 1已审核 2审核未通过  3资质未认证")
        newShop.setState(3);


        //行业分类的  一级二级三级标题  逗号分隔，最少有俩级
        String treeId = shopPacketParam.getShopTreeId();
        //数组

        try {
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
        } catch (NumberFormatException e) {
            e.printStackTrace();
            throw new JSYException(-1,"店铺创建分类错误");
        }
        shopMapper.updateById(newShop);
    }
 /** 
  * @author Tian
  * @since 2021/12/2-10:10
  * @description 本月入驻商家数量
  **/
    @Override
    public Integer newShopAudit() {
        ///审核状态 0未审核 1已审核 2审核未通过 3资质未认证
        Integer state = 1;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        Date date = new Date();
        String startDate = new SimpleDateFormat("yyyy-MM").format(new Date()).toString()+"-01";

        //日历
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date); // 设置为当前时间
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) +1); // 设置为上一个月
        date = calendar.getTime();
        String endDate = format.format(date)+"-01";
        Integer count  = shopMapper.newShopAudit(startDate,endDate,state);
        return count;
    }

}
