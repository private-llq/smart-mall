package com.jsy.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jsy.basic.util.MyPageUtils;
import com.jsy.basic.util.PageInfo;
import com.jsy.basic.util.exception.JSYException;
import com.jsy.basic.util.utils.GouldUtil;
import com.jsy.basic.util.utils.RegexUtils;
import com.jsy.clent.CommentClent;
import com.jsy.client.GoodsClient;
import com.jsy.client.SetMenuClient;
import com.jsy.client.TreeClient;
import com.jsy.domain.Goods;
import com.jsy.domain.NewShop;
import com.jsy.domain.Tree;
import com.jsy.dto.*;
import com.jsy.mapper.NewShopMapper;
import com.jsy.parameter.*;
import com.jsy.query.MainSearchQuery;
import com.jsy.query.NearTheServiceQuery;
import com.jsy.query.NewShopQuery;
import com.jsy.service.INewShopService;
import com.jsy.service.IUserSearchHistoryService;
import com.zhsj.baseweb.support.ContextHolder;
import com.zhsj.baseweb.support.LoginUser;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.math.BigDecimal.ROUND_HALF_UP;

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
    private IUserSearchHistoryService searchHistoryService;

    @Resource
    private CommentClent commentClent;
//    @Resource
//    private StringRedisTemplate redisTemplate;
//    @Resource
//    private RedisUtils redisUtils;
//    private  final int shopType = 1;

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
        String[] split1 = shopPacketParam.getShopLogo().split(",");
        if (split1.length>1) {
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
//            Long aLong = Long.valueOf(split[1]);
            Long aLong = Long.valueOf(treeId.substring(0,1));
            String data = treeClient.getParentTreeAll(aLong).getData();
            String s = data.substring(1, data.length() - 1);
            String substring = s.substring(s.length() - 1);
            newShop.setShopTreeId(substring+","+shopPacketParam.getShopTreeId());
            //1是服务行业  0 套餐行业
            if (Integer.parseInt(substring)== 1) {
                newShop.setType(1);
            } else {
                newShop.setType(0);
            }
        } catch (NumberFormatException e) {
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
        if (newShop==null|| newShop.getShielding()==1){
            throw new JSYException(-1,"店铺为空");
        }
        NewShopPreviewDto newShopPreviewDto = new NewShopPreviewDto();
        try {
            if (newShop.getShopTreeId()!=null){
                String treeId = newShop.getShopTreeId();

                String[] split = treeId.split(",");
                String shopTreeIdName = getShopTreeIdName(split);
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
        if (ObjectUtil.isNull(newShop)){
            return ;
        }
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
        //用户地址
        //分类id
        Long treeId = shopQuery.getTreeId();
        //获取用户经纬度
        BigDecimal longitude =  new BigDecimal((shopQuery.getLongitude()));
        BigDecimal latitude = new BigDecimal((shopQuery.getLatitude()));

        List<NewShop> newShopList = shopMapper.selectAddress(longitude,latitude,shopQuery.getTreeId());
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
                        recommendDto.setShopTreeId(newShop.getShopTreeId());
                        recommendDto.setDistance(newShop.getDistance().divide(new BigDecimal(1000)).setScale(2,ROUND_HALF_UP));

                        String[] ids = newShop.getShopTreeId().split(",");
                        String treeName = getShopTreeIdName(ids);
//            Tree tree = treeClient.getTree(Long.valueOf(ids[ids.length - 1])).getData();
                        if (Objects.nonNull(treeName)){
                            recommendDto.setShopTreeIdName(treeName);
                        }
            System.out.println("评分");
                        //评分
                        SelectShopCommentScoreDto data = commentClent.selectShopCommentScore(newShop.getId()).getData();
            System.out.println(data.getScore());
                        recommendDto.setGrade(data.getScore());

                        //把商品最近发布的东西查出来   有可能是服务又可能是商品 有可能是 套餐
                        Goods goods = goodsClient.latelyGoods(newShop.getId()).getData();
            System.out.println(goods);
                        if (goods!=null){
                            recommendDto.setTitle(goods.getTitle());
                            recommendDto.setPrice(goods.getPrice());
                            recommendDto.setShopLogo(newShop.getShopLogo());
                            recommendDto.setDiscountPrice(goods.getDiscountPrice());
                            recommendDto.setDiscountState(goods.getDiscountState());
                            shopList.add(recommendDto);
                        }
        }
        System.out.println(shopList);
        PageInfo<NewShopRecommendDto> pageInfo = MyPageUtils.pageMap(shopQuery.getPage(), shopQuery.getRows(), shopList);

        return pageInfo;
    }
 /**
  * @author Tian
  * @since 2021/12/3-11:32
  * @description 店铺分类名称
  **/
    private String getShopTreeIdName(String[] split) {
        String shopTreeIdName = "";
        if (split.length >= 0) {
            String name = treeClient.getTree(Long.valueOf(split[split.length - 1])).getData().getName();
            return name;
        }else {
            throw new JSYException(-1,"商品分类错误");
        }

    }

    /**
     * 首页搜索
     */
    @Override
    public PageInfo<MyNewShopDto> mainSearch(MainSearchQuery mainSearchQuery) {
        Long userId = ContextHolder.getContext().getLoginUser().getId();
        searchHistoryService.addSearchKey(userId,mainSearchQuery.getKeyword());
        String keyword = mainSearchQuery.getKeyword();
        List<NewShop> newShops= shopMapper.mainSearch(keyword);
        if (newShops.size()==0){
          return new PageInfo<>();
        }

        ArrayList<MyNewShopDto> list = new ArrayList<>();
        for (NewShop newShop : newShops) {
            MyNewShopDto myNewShopDto = new MyNewShopDto();
            myNewShopDto.setId(newShop.getId());
            myNewShopDto.setShopName(newShop.getShopName());
            SelectShopCommentScoreDto data = commentClent.selectShopCommentScore(newShop.getId()).getData();
            if (Objects.nonNull(data)){
                myNewShopDto.setGrade(data.getScore());
            }
            myNewShopDto.setImage(newShop.getShopLogo());
            //查询
            try {
                Goods goods = goodsClient.latelyGoods(newShop.getId()).getData();
                if (Objects.nonNull(goods)){
                    myNewShopDto.setTitle(goods.getTitle());
                    myNewShopDto.setPrice(goods.getPrice());
                    myNewShopDto.setDiscountState(goods.getDiscountState());
                    myNewShopDto.setDiscountPrice(goods.getDiscountPrice());
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new JSYException(-1,"无法找到商家最新上架的商品！");
            }
            DecimalFormat df = new DecimalFormat("#0.00");
            Double distance = GouldUtil.GetDistance(Double.parseDouble(mainSearchQuery.getLatitude()),Double.parseDouble(mainSearchQuery.getLongitude()),newShop.getLatitude().doubleValue(),newShop.getLongitude().doubleValue());
            myNewShopDto.setDistance(df.format(distance/1000)+"km");

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
//        List<NewShop> shopList = shopMapper.selecctNewShopPage(shopQuery);
//        List<NewShopDto> dtoList = new ArrayList<>();
//        for (NewShop newShop : shopList) {
//            NewShopDto newShopDto = new NewShopDto();
//            try {
//                if (newShop.getShopTreeId()!=null){
//                    String treeId = newShop.getShopTreeId();
//
//                    String[] split = treeId.split(",");
//                    String shopTreeIdName = getShopTreeIdName(split);
//                    newShopDto.setShopTreeIdName(shopTreeIdName);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                throw new JSYException(-1,"店铺分类错误");
//            }
//
//            BeanUtils.copyProperties(newShop,newShopDto);
//            dtoList.add(newShopDto);
//        }
//
//
//        PageInfo<NewShopDto> newShopDtoPageInfo = MyPageUtils.pageMap(shopQuery.getPage(), shopQuery.getRows(), dtoList);
        return null;
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
        if (newShop.getShielding()==1){
            throw new JSYException(-1,"店铺不存在");
        }
        NewShopBasicDto basicDto = new NewShopBasicDto();
        BeanUtils.copyProperties(newShop,basicDto);
        try {
            if (newShop.getShopTreeId()!=null){
                String treeId = newShop.getShopTreeId();

                String[] split = treeId.split(",");
                String shopTreeIdName = getShopTreeIdName(split);
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
        String[] split1 = shopPacketParam.getShopLogo().split(",");
        if (split1.length>1) {
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
//            Long aLong = Long.valueOf(split[1]);
            Long aLong = Long.valueOf(treeId.substring(0,1));
            String data = treeClient.getParentTreeAll(aLong).getData();
            String s = data.substring(1, data.length() - 1);
            String substring = s.substring(s.length() - 1);
            newShop.setShopTreeId(substring+","+shopPacketParam.getShopTreeId());
            //1是服务行业  0 套餐行业
            if (Integer.parseInt(substring)== 1) {
                newShop.setType(1);
            } else {
                newShop.setType(0);
            }
        } catch (NumberFormatException e) {
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

    @Override
    public NewShopSupportDto getSupport(Long shopId) {
        NewShop newShop = shopMapper.selectOne(new QueryWrapper<NewShop>().eq("id", shopId));
        if (newShop.getShielding()==1){
            throw new JSYException(-1,"店铺不存在");
        }
        NewShopSupportDto suportDto = new NewShopSupportDto();
        if (ObjectUtil.isNull(newShop)){
            return suportDto;
        }
        suportDto.setIsVirtualShop(newShop.getIsVirtualShop());
        suportDto.setIsVisitingService(newShop.getIsVisitingService());
        suportDto.setMobile(newShop.getMobile());
        suportDto.setShopPhone(newShop.getShopPhone());
        suportDto.setShopId(shopId);
        suportDto.setShopName(newShop.getShopName());
        suportDto.setShopLogo(newShop.getShopLogo());
        return suportDto;
    }
 /**
  * @author Tian
  * @since 2021/12/8-11:42
  * @description C端查询店铺的距离多远
  **/
    @Override
    public NewShopDistanceDto getDistance(NewShopDistanceParam distanceParam) {
        if (distanceParam.getLongitude()==null&&distanceParam.getLatitude()==null){
            throw new JSYException(-1,"用户位置不能为空");
        }
        NewShop newShop = shopMapper.selectById(distanceParam.getId());
        DecimalFormat df = new DecimalFormat("#0.00");

        Double distance = GouldUtil.GetDistance(distanceParam.getLatitude().doubleValue(), distanceParam.getLongitude().doubleValue(),newShop.getLatitude().doubleValue(),newShop.getLongitude().doubleValue());
        NewShopDistanceDto distanceDto = new NewShopDistanceDto();
        BeanUtils.copyProperties(newShop,distanceDto);
        distanceDto.setDistance(df.format(distance / 1000)+"km");
        String imId = ContextHolder.getContext().getLoginUser().getImId();
       distanceDto.setImId(imId);
        return distanceDto;
    }

    @Override
    public PageInfo<NewShopRecommendDto> getMedicalShop(NewShopQuery shopQuery) {
        if(shopQuery.getTreeId()==null){
            shopQuery.setTreeId(6L);
        }
        List<NewShop> newShops= shopMapper.getMedicalShop(shopQuery);
        if (newShops.size()==0){
            return new PageInfo<>();
        }
        List<NewShopRecommendDto> recommendDtoList = new ArrayList<>();
        for (NewShop newShop : newShops) {
            NewShopRecommendDto recommendDto = new NewShopRecommendDto();
            BeanUtils.copyProperties(newShop,recommendDto);
            //.divide(new BigDecimal(1000)
            recommendDto.setDistance(newShop.getDistance().divide(new BigDecimal(1000)).setScale(2,ROUND_HALF_UP));
            recommendDtoList.add(recommendDto);
        }
        PageInfo<NewShopRecommendDto> dtoPageInfo = MyPageUtils.pageMap(shopQuery.getPage(), shopQuery.getRows(), recommendDtoList);
        return dtoPageInfo;
    }

    @Override
    public NewShopServiceDto getShopService(NewShopQuery shopQuery) {
        NewShopServiceDto serviceDto = new NewShopServiceDto();
        if(shopQuery.getTreeId()==null){
            shopQuery.setTreeId(6L);
        }
        List<NewShop> newShops= shopMapper.getMedicalShop(shopQuery);
        List<NewShopRecommendDto> recommendDtoList = new ArrayList<>();
        Integer number = 0;
        for (NewShop newShop : newShops) {
            if (number>=5){
                break;
            }
            number++;
            NewShopRecommendDto recommendDto = new NewShopRecommendDto();
            BeanUtils.copyProperties(newShop,recommendDto);
            //.divide(new BigDecimal(1000)
            recommendDto.setDistance(newShop.getDistance().divide(new BigDecimal(1000).setScale(2, ROUND_HALF_UP)));
            String[] spilt = newShop.getShopTreeId().split(",");
            String shopTreeIdName = getShopTreeIdName(spilt);
            recommendDto.setShopTreeIdName(shopTreeIdName);
            recommendDtoList.add(recommendDto);
            System.out.println(recommendDto.getDistance());
        }
        serviceDto.setShopList(recommendDtoList);
        NearTheServiceQuery serviceQuery = new NearTheServiceQuery();
        serviceQuery.setLatitude(shopQuery.getLatitude());
        serviceQuery.setLongitude(shopQuery.getLongitude());
        serviceQuery.setKeyword(shopQuery.getShopName());
        List<GoodsServiceDto> goodsList = goodsClient.NearTheService2(serviceQuery).getData();
        DecimalFormat   fnum  =   new  DecimalFormat("##0.00");
        if (goodsList.size()>0){
            for (GoodsServiceDto goodsServiceDto : goodsList) {
                Float aFloat = Float.parseFloat(goodsServiceDto.getDistance())/1000;
                goodsServiceDto.setDistance(fnum.format(aFloat).toString());
            }
        }
        serviceDto.setGoodsList(goodsList);
        return serviceDto;
    }


}
