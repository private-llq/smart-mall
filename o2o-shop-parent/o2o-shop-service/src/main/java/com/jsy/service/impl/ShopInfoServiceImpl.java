package com.jsy.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jsy.DO.ShopQueryDO;
import com.jsy.DO.ShopStarDo;
import com.jsy.basic.util.exception.JSYException;
import com.jsy.basic.util.utils.*;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.basic.util.vo.UserDto;
import com.jsy.client.*;
import com.jsy.domain.*;
import com.jsy.dto.*;
import com.jsy.mapper.ShopGenreMapper;
import com.jsy.mapper.ShopInfoMapper;
import com.jsy.mapper.ShopOwnerMapper;
import com.jsy.mapper.ShopTypeMapper;
import com.jsy.parameter.*;
import com.jsy.query.AdminShopQuery;
import com.jsy.query.ShopInfoQuery;
import com.jsy.service.*;
import com.jsy.vo.ShopAssetsVO;
import com.jsy.vo.ShopInfoParamVo;
import com.jsy.vo.ShopInfoVo;
import com.zhsj.baseweb.support.ContextHolder;
import com.zhsj.baseweb.support.LoginUser;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lijin
 * @since 2020-11-12
 */
@Service
public class ShopInfoServiceImpl extends ServiceImpl<ShopInfoMapper, ShopInfo> implements IShopInfoService {

    @Autowired
    private ShopInfoMapper shopInfoMapper;

    @Autowired
    private GoodsBasicFeign goodsBasicFeign;

    @Autowired
    private ShopOwnerServiceImpl shopOwnerService;

    @Autowired
    private IActivityService activityService;

    @Autowired
    private ShopOwnerMapper ownerMapper;

    @Autowired
    private ShopAssetsClient shopAssetsClient;

    @Autowired
    private ShopTypeMapper shopTypeMapper;

    @Autowired
    private UserClient userClient;

    @Autowired
    private FileClient fileClient;
    @Resource
    private BusinessHoursService businessHoursService;
    @Resource
    private IShopTypeService shopTypeService;
    @Resource
    private ShopGenreMapper shopGenreMapper;
    @Autowired
    private ActivityClient activityClient;
    @Autowired
    private IShopRedpacketService shopRedpacketService;

    //店铺申请
    @Override
    public Boolean applyShop(ShopInfoParamVo shopInfoParam) {
        boolean mobile = RegexUtils.isLandline(shopInfoParam.getMobile());//验证电话
        if (!mobile) {
            throw new JSYException(-1, "座机电话格式错误");
        }
        boolean phone = RegexUtils.isMobile(shopInfoParam.getShopPhone());
        if (!phone){
            throw new JSYException(-1, "经营者/法人电话格式错误");
        }

        if (shopInfoParam.getShopName().length() > 15) {
            throw new JSYException(-1, "店铺名太长");
        }
        List<String> shopLogo = shopInfoParam.getShopLogo();
        if (shopLogo.size()>5){
            throw new JSYException(-1,"照片只能上传5张");
        }
        ShopInfo shopInfo = new ShopInfo();
        BeanUtils.copyProperties(shopInfoParam, shopInfo);
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();//解析token ;//获取登录者的uuid
        if (loginUser==null){
            throw  new  JSYException(-1,"请先登录");
        }
        //登陆者的id
        shopInfo.setOwnerUuid(loginUser.getId().toString());
/*        //座机电话
        shopInfo.setMobile(shopInfoParam.getMobile());*/
        //门店照片
        shopInfo.setShopLogo(shopInfoParam.getShopLogo().toString());
        //门店类型
        shopInfo.setIndustry_category_id(shopInfoParam.getIndustry_category_id());

        shopInfo.setUuid(UUIDUtils.getUUID());

//        ShopInfoParam infoParam = new ShopInfoParam();
//        BeanUtils.copyProperties(shopInfoParam,infoParam);
//
//        shopInfo.setShopLogo(infoParam.getShopFront());//将门脸设置为默认的log
//        shopInfo.setOwnerUuid(loginUser.getId().toString());//设置店铺拥有者uuid
//        shopInfo.setStatus(0);//默认状态没审核
//        shopInfo.setBusinessStatus(3);//默认是暂停营业的
//        shopInfo.setDeliveryArea(0L);//默认配送距离为0
//        int insert = shopInfoMapper.insert(shopInfo);
//        if (insert > 0) {
//            return true;
//        }
        return false;
    }

    //分页查询待审核的店铺
    @Override
    public IPage<ShopInfo> selectShopInfoByUnrevised(PageParam pageParam) {
        QueryWrapper<ShopInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 0);
        Page<ShopInfo> page = new Page<>(pageParam.getCurrent(), pageParam.getSize());
        Page<ShopInfo> page1 = shopInfoMapper.selectPage(page, queryWrapper);
        return page1;
    }

    //批量店铺审核
    @Override
    @Transactional
    public Boolean approveShop(List<String> uuidS) {
        int i = 0;
        for (String uuid : uuidS) {
            ShopInfo shopInfo = new ShopInfo();
            shopInfo.setStatus(1);
            int uuid1 = shopInfoMapper.update(shopInfo, new UpdateWrapper<ShopInfo>().eq("uuid", uuid));
            businessHoursService.defaultBusinessTime(uuid);//审核通过,添加默认的营业时间
            if (uuid1 > 0) {
                i++;
            }
        }
        if (uuidS.size() == i) {

            return true;
        }
        return false;
    }

    //根据账户查看自己的门店列表
    @Override
    public List<ShopInfoByOwnerParam> selectShopListByShopOwnerUuid() {
        String uuid = CurrentUserHolder.getCurrentUser().getUuid();
        List<ShopInfo> ShopInfoList = shopInfoMapper.selectList(new QueryWrapper<ShopInfo>().eq("owner_uuid", uuid));
        ArrayList<ShopInfoByOwnerParam> shopInfoByOwnerParams = new ArrayList<>();
        for (ShopInfo shopInfo : ShopInfoList) {
            ShopInfoByOwnerParam shopInfoByOwnerParam = new ShopInfoByOwnerParam();
            BeanUtils.copyProperties(shopInfo, shopInfoByOwnerParam);
            shopInfoByOwnerParams.add(shopInfoByOwnerParam);
        }
        return shopInfoByOwnerParams;
    }

    //删除没有审核通过的其他店铺
    @Override
    public Boolean deleteShopInfo(String uuid) {
        ShopInfo shopInfo = shopInfoMapper.selectOne(new QueryWrapper<ShopInfo>().eq("uuid", uuid));
        Integer status = shopInfo.getStatus();
        if (status == 1) {
            throw new JSYException(-1, "申请通过不能删除");
        }
        int i = shopInfoMapper.delete(new QueryWrapper<ShopInfo>().eq("uuid", uuid));
        if (i > 0) {
            return true;
        }
        return false;
    }

    //查询删除的店铺列表
    @Override
    public List<ShopInfoByOwnerParam> selectDeletedShopList() {
        UserDto currentUser = CurrentUserHolder.getCurrentUser();//获取登录者的uuid
        List<ShopInfo> shopInfos = shopInfoMapper.selectDeletedShopList(currentUser.getUuid());
        ArrayList<ShopInfoByOwnerParam> shopInfoByOwnerParams1 = new ArrayList<>();
        for (ShopInfo shopInfo : shopInfos) {
            ShopInfoByOwnerParam shopInfoByOwnerParam = new ShopInfoByOwnerParam();
            BeanUtils.copyProperties(shopInfo, shopInfoByOwnerParam);
            shopInfoByOwnerParams1.add(shopInfoByOwnerParam);
        }
        return shopInfoByOwnerParams1;
    }

    //查询店铺信息
    @Override
    public ShopMessageDto selectShopMessage(String shopUuid) {
        ShopMessageDto shopMessageDto = new ShopMessageDto();
        ShopInfo shopInfo = shopInfoMapper.selectOne(new QueryWrapper<ShopInfo>().eq("uuid", shopUuid));
        List<SelectShopBusinessTimeDto> selectShopBusinessTimeDtos = businessHoursService.selectShopBusinessTime(shopUuid);
        //BeanUtils.copyProperties(shopInfo, shopMessageDto);
        BeanUtil.copyProperties(shopInfo, shopMessageDto,true);
        shopMessageDto.setBusinessTimeDtoS(selectShopBusinessTimeDtos);
        return shopMessageDto;
    }

    //查询店铺资料
    @Override
    public SelectShopMeansDto selectShopMeans(String shopUuid) {
        SelectShopMeansDto selectShopMeansDto = new SelectShopMeansDto();
        ShopInfo shopInfo = shopInfoMapper.selectOne(new QueryWrapper<ShopInfo>().eq("uuid", shopUuid));
        BeanUtils.copyProperties(shopInfo, selectShopMeansDto);
        String shopManageTypeUuid = shopInfo.getShopManageTypeUuid();//经营品类uuid
        selectShopMeansDto.setShopManageType(shopTypeService.selectShopType(shopManageTypeUuid));
        String shopGenreUuid = shopInfo.getShopGenreUuid();//门店类型uuid
        String name = shopGenreMapper.selectOne(new QueryWrapper<ShopGenre>().eq("uuid", shopGenreUuid)).getName();
        selectShopMeansDto.setShopGenre(name);//等待常见实体类
        return selectShopMeansDto;
    }

    //修改店铺公告
    @Override
    public Boolean updateNotice(UpdateNoticeParam updateNoticeParam) {
        ShopInfo shopInfo = new ShopInfo();
        shopInfo.setNotice(updateNoticeParam.getNotice());
        int uuid = shopInfoMapper.update(shopInfo, new UpdateWrapper<ShopInfo>().eq("uuid", updateNoticeParam.getUuid()));
        if (uuid > 0) {
            return true;
        }
        return false;
    }

    //修改店铺环境
    @Override
    public Boolean updateShopEnvironment(UpdateShopEnvironmentParam updateShopEnvironmentParam) {
        ShopInfo shopInfo = new ShopInfo();
        shopInfo.setShopImages1(updateShopEnvironmentParam.getShopImages1());
        shopInfo.setShopImages2(updateShopEnvironmentParam.getShopImages2());
        shopInfo.setShopImages3(updateShopEnvironmentParam.getShopImages3());
        int uuid = shopInfoMapper.update(shopInfo, new UpdateWrapper<ShopInfo>().eq("uuid", updateShopEnvironmentParam.getUuid()));
        if (uuid > 0) {
            return true;
        }
        return false;

    }

    //修改营业状态
    @Override
    public Boolean updateBusinessStatus(UpdateBusinessStatusParam updateBusinessStatusParam) {
        ShopInfo shopInfo = new ShopInfo();
        shopInfo.setBusinessStatus(updateBusinessStatusParam.getBusinessStatus());
        int uuid = shopInfoMapper.update(shopInfo, new UpdateWrapper<ShopInfo>().eq("uuid", updateBusinessStatusParam.getUuid()));
        if (uuid > 0) {
            return true;
        }
        return false;
    }

    //修改接单电话
    @Override
    public Boolean updateOrderReceivingPhone(UpdateOrderReceivingPhoneParam updateOrderReceivingPhoneParam) {
        String orderReceivingPhone = updateOrderReceivingPhoneParam.getOrderReceivingPhone();
        String[] split = orderReceivingPhone.split(";");
        if (split.length > 3) {
            throw new JSYException(-1, "最多3个接单电话");
        }
        for (String s : split) {
            boolean landline = RegexUtils.isLandline(s);
            boolean mobile = RegexUtils.isMobile(s);
            if (!landline && !mobile) {
                throw new JSYException(-1, s + "格式错误");
            }
        }
        ShopInfo shopInfo = new ShopInfo();
        shopInfo.setOrderReceivingPhone(updateOrderReceivingPhoneParam.getOrderReceivingPhone());
        int uuid = shopInfoMapper.update(shopInfo, new UpdateWrapper<ShopInfo>().eq("uuid", updateOrderReceivingPhoneParam.getUuid()));
        if (uuid > 0) {
            return true;
        }
        return false;

    }
    //修改店铺单笔配送费
    @Override
    public Boolean updatePostage(UpdatePostageParam updatePostageParam) {
        ShopInfo shopInfo = new ShopInfo();
        shopInfo.setPostage(updatePostageParam.getPostage());
        int uuid = shopInfoMapper.update(shopInfo, new UpdateWrapper<ShopInfo>().eq("uuid", updatePostageParam.getShopUuid()));
                if(uuid>0){
                    return true;
                }
        return false;
    }
    //查询商家单笔配送费
    @Override
    public BigDecimal selectPostage(String shopUUid) {
        ShopInfo shopInfo = shopInfoMapper.selectOne(new QueryWrapper<ShopInfo>().eq("uuid", shopUUid));
        BigDecimal postage=BigDecimal.ZERO;
        if (shopInfo!=null) {
            postage = shopInfo.getPostage();
        }
        return postage;
    }

    /***********************************************************************************/

    @Override
    public PagerUtils<ShopQueryDTO> selectByConditon(ShopInfoQuery shopInfoQuery) {

        if (Objects.isNull(shopInfoQuery.getLatitude())||Objects.isNull(shopInfoQuery.getLongitude())){
            throw new JSYException(-1,"经纬度不能为空！");

        }
        //搜索半径,单位为千米不能为0
        if (Objects.isNull(shopInfoQuery.getRadius()) || shopInfoQuery.getRadius() == 0) {
            shopInfoQuery.setRadius(5 * 1000);//默认半径5千米
        }
        /*筛选对象不为空的时候*/
        if (Objects.nonNull(shopInfoQuery.getShopScreen())) {
            int estimatedTime = shopInfoQuery.getShopScreen().getEstimatedTime();//送达时间好多分钟
            double star = shopInfoQuery.getShopScreen().getStar();//星级（送达时间）
            //将时间变为公里
            if (estimatedTime > 0) {
                shopInfoQuery.setRadius((estimatedTime - LatLonUtil.BASIC_TIME/*25*/) / (LatLonUtil.ONEKM/*3*/) + 2);
            }
        }

        //计算经纬度范围
        Double longtitud = shopInfoQuery.getLongitude().doubleValue();
        Double latitude = shopInfoQuery.getLatitude().doubleValue();
        Map<String, Double> doubles = LatLonUtil.GetAround(latitude, longtitud, shopInfoQuery.getRadius() * 1000);

        //根据店铺类型查询条件
        ArrayList<Long> idList = new ArrayList<>();

        if (!Objects.isNull(shopInfoQuery.getId()) && shopInfoQuery.getId() != 0) {
            if (shopInfoQuery.getId() == 1) {
                shopInfoQuery.setId(25L);
            }
            if (shopInfoQuery.getId() == 2) {
                shopInfoQuery.setId(26L);
            }
            if (shopInfoQuery.getId() == 3) {
                shopInfoQuery.setId(27L);
            }
            if (shopInfoQuery.getId() == 4) {
                shopInfoQuery.setId(28L);
            }
        }

        //没有关键字
        if (StringUtils.isEmpty(shopInfoQuery.getKeyword())) {
            shopInfoQuery.setLimit((shopInfoQuery.getPage() - 1) * shopInfoQuery.getRows());

            Page<ShopQueryDTO> page = new Page<>(shopInfoQuery.getPage(), shopInfoQuery.getRows());

            //用户定位后显示附件的商家（分页）
            IPage<ShopQueryDTO> shopAll = shopInfoMapper.findAll(page, doubles, shopInfoQuery);

            Long total=shopAll.getTotal();//条数
            List<ShopQueryDTO> resultList = shopAll.getRecords();//总记录数

            ArrayList<String> shopUuidList = new ArrayList<>();//商家的UUID集合
            resultList.stream().forEach(x ->{
                shopUuidList.add(x.getUuid());
            });

            HashMap<String, Object> map = new HashMap<>();//参数传递
            map.put("shopUuid", shopUuidList);
            map.put("num", 3);
            //查出销量高的店铺（从高到低排序）
            List<GoodsCommendDTO> goodsList = goodsBasicFeign.commendGoods(map).getData();

            for (ShopQueryDTO record : resultList) {
                //每一个店铺的距离
                double distance = LatLonUtil.GetDistance(record.getLongitude().doubleValue(), record.getLatitude().doubleValue(), longtitud, latitude);
                record.setDistance(distance);
                record.setEstimatedTime(LatLonUtil.getMin(distance));

                //每一个店铺活动，红包的情况
                ShopInfoDTO shopInfoDTO = selectShop(record.getUuid(), shopInfoQuery.getLongitude().doubleValue(), shopInfoQuery.getLatitude().doubleValue());
                record.setActivityNameList(shopInfoDTO.getActivityList());
                record.setShopRedpacketList(shopInfoDTO.getShopRedpacketList());

                //从销量高的店铺拿数据到 返回对象
                ArrayList<ShopQueryDTO> goodsQueries = new ArrayList<>();
                for (GoodsCommendDTO goodsDto : goodsList) {
                    if (record.getUuid().equals(goodsDto.getShopUuid())) {
                        ShopQueryDTO query = new ShopQueryDTO();
                        query.setName(goodsDto.getTitle());
                        query.setImages(goodsDto.getImages().split(",")[0]);
                        query.setPrice(goodsDto.getPrice());
                        query.setDiscountPrice(goodsDto.getDiscountPrice());
                        goodsQueries.add(query);
                    }
                }
                record.setGoodsList(goodsQueries);
            }
            int order = shopInfoQuery.getOrder();
            if (order > 0) {
                //根据星级排序(由高到低)
                if (order == 1) {
                    resultList = resultList.stream().sorted(Comparator.comparing(ShopQueryDTO::getStar, Comparator.reverseOrder())).collect(Collectors.toList());
                }
                //根据距离排序(由近到远)/距离相同的按照星级推荐
                if (order == 2) {
                    resultList = resultList.stream().sorted(Comparator.comparing((ShopQueryDTO x) -> x.getDistance())
                            .thenComparing(ShopQueryDTO::getStar, Comparator.reverseOrder())).collect(Collectors.toList());
                }
                //根据速度排序(快到慢)
                if (order == 3) {
                    resultList = resultList.stream().sorted(Comparator.comparing(shopQueryDTO -> shopQueryDTO.getEstimatedTime())).collect(Collectors.toList());
                }
            }
            PagerUtils<ShopQueryDTO> pagerUtils = new PagerUtils<>();
            pagerUtils.setTotal(Math.toIntExact(total));
            pagerUtils.setRecords(resultList);
            return pagerUtils;
        }
        //带关键字查询
        PagerUtils<ShopQueryDTO> byKeyWrods = selectByKeyWrods(doubles, shopInfoQuery);
        return byKeyWrods;
    }

    //首页带关键字查询
    //返回 商铺携带相关商品
    private PagerUtils<ShopQueryDTO> selectByKeyWrods(Map doubles, ShopInfoQuery shopInfoQuery) {
        List<ShopQueryDO> list = shopInfoMapper.selectByConditon(doubles, shopInfoQuery);

        ArrayList<ShopQueryDTO> dtos = new ArrayList<>();
        //处理数据
        //1.分离店铺 商品数据
        //找出所所有的店铺
        HashMap<String, ShopQueryDTO> map = new HashMap<>();
        for (ShopQueryDO queryDO : list) {
            if (!map.containsKey(queryDO.getShopUuid())) {
                ShopQueryDTO dto = new ShopQueryDTO();
                dto.setUuid(queryDO.getShopUuid());
                dto.setName(queryDO.getShopName());
                dto.setImages(queryDO.getShopImage().split(",")[0]);
                map.put(queryDO.getShopUuid(), dto);
            }
        }
        //2.封装商品到店铺
        ArrayList<ShopQueryDTO> result = new ArrayList<>();
        for (Map.Entry<String, ShopQueryDTO> entry : map.entrySet()) {
            ArrayList<ShopQueryDTO> goodsQueryDTO = new ArrayList<>();
            for (int x = 0; x < list.size(); x++) {
                if ("t_goods_basic".equals(list.get(x).getTb()) && entry.getKey().equals(list.get(x).getShopUuid())) {
                    ShopQueryDTO dto = new ShopQueryDTO();
                    dto.setUuid(list.get(x).getUuid());
                    dto.setName(list.get(x).getName());
                    dto.setImages(list.get(x).getGoodsImage().split(",")[0]);
                    goodsQueryDTO.add(dto);
                }
            }
            if (goodsQueryDTO.size() == 0) {
                entry.getValue().setGoodsList(null);
            } else {
                entry.getValue().setGoodsList(goodsQueryDTO);
            }
            result.add(entry.getValue());
        }
        PagerUtils<ShopQueryDTO> pagerUtils = new PagerUtils<>();
        PagerUtils<ShopQueryDTO> queryPage = pagerUtils.queryPage(shopInfoQuery.getPage(), shopInfoQuery.getRows(), result);
        return queryPage;
    }


    @Override
    @Transactional
    public void addShop(ShopInfoVo shopInfoVo) {
        //校验数据
        if (!StringUtils.isEmpty(shopInfoVo.getMobile()) && !RegexUtils.isLandline(shopInfoVo.getMobile()) && !RegexUtils.isMobile(shopInfoVo.getMobile())) {
            throw new JSYException(-1, "请输入正确的店铺号码");
        }
        if (!RegexUtils.isMobile(shopInfoVo.getShopPhone()))
            throw new JSYException(-1, "请输入正确的联系人电话");
        if (!RegexUtils.isIDCard(shopInfoVo.getShopCid()))
            throw new JSYException(-1, "请输入正确的身份证号码");
        ShopInfo info = new ShopInfo();
        BeanUtils.copyProperties(shopInfoVo, info);
        info.setUuid(UUIDUtils.getUUID());
        //新增未审核
        info.setStatus(0);
        shopInfoMapper.insert(info);
        ShopOwner shopOwner = new ShopOwner();
        shopOwner.setName(shopInfoVo.getShopUsername());
        shopOwner.setPhone(shopInfoVo.getShopPhone());
        shopOwner.setPassword(shopInfoVo.getPassword());
        shopOwnerService.addShopOwner(shopOwner);
    }
    @Override
    public Integer updateShop(ShopInfoVo shopInfoVo) {
        UserDto user = CurrentUserHolder.getCurrentUser();
        if (Objects.isNull(user))
            throw new JSYException(-1, "请登录后操作");
        if (!user.getRelationUuid().equals(shopInfoVo.getUuid())) {
            throw new JSYException(-1, "不合法操作");
        }
        ShopInfo info = new ShopInfo();
        BeanUtils.copyProperties(shopInfoVo, info);
        info.setUpdateTime(LocalDateTime.now());
        return shopInfoMapper.update(info, new QueryWrapper<ShopInfo>().eq("uuid", info.getUuid()));
    }

    @Override
    @Transactional
    public Integer changeStatus(String uuid, String status) {
        //修改店铺审核状态
        ShopInfo shopInfo = new ShopInfo();
        shopInfo.setStatus(Integer.getInteger(status));
        int i = shopInfoMapper.update(shopInfo, new QueryWrapper<ShopInfo>().eq("uuid", uuid));
        //审核通过后为用户创建初始化账户用户进出帐
        ShopAssetsVO vo = new ShopAssetsVO();
        vo.setAssets(new BigDecimal("0"));
        vo.setOwnerUuid(uuid);
        vo.setUuid(UUIDUtils.getUUID());
        shopAssetsClient.save(vo);

        return i;
    }

    @Override
    public ShopInfoDTO selectShop(String uuid, double longitude, double latitude) {
        ShopInfoDTO shopInfoDTO = shopInfoMapper.selectShop(uuid);
        ShopInfo shopInfo = shopInfoMapper.selectOne(new QueryWrapper<ShopInfo>().eq("uuid", uuid));
        String shopImages1 = shopInfo.getShopImages1();
        String shopImages2 = shopInfo.getShopImages2();
        String shopImages3 = shopInfo.getShopImages3();
        shopInfoDTO.setShopImages(shopImages1 + "," + shopImages2 + "," + shopImages3);//环境图片封装
        int min = LatLonUtil.getMin(LatLonUtil.GetDistance(shopInfoDTO.getLongitude().doubleValue(),
                shopInfoDTO.getLatitude().doubleValue(), longitude, latitude));
        shopInfoDTO.setEstimatedTime(min);
        shopInfoDTO.setName(shopInfo.getShopName());
        List<Activity> data = activityClient.runActivities(uuid).getData();
        shopInfoDTO.setActivityList(data);
        SelectShopRedpacketByUserDto data1 = shopRedpacketService.SelectShopRedpacketByUser(uuid);
        if (data1 != null) {
            ShopRedpacket shopRedpacket = new ShopRedpacket();
            BeanUtils.copyProperties(data1, shopRedpacket);
            List<ShopRedpacket> list = new ArrayList<>();
            list.add(shopRedpacket);
            shopInfoDTO.setShopRedpacketList(list);
        }
//        //转换图片
//        ArrayList<String> strings = new ArrayList<>();
//        strings.add(shopInfoDTO.getShopLogo());
//
//        String shopImages = shopInfoDTO.getShopImages();
//        String[] split = shopImages.split(",");
//        for (String s : split) {
//            strings.add(s);
//        }
//
//        Map<String, String> picUrl = fileClient.getPicUrl(strings);
//        shopInfoDTO.setShopLogo(picUrl.get(shopInfoDTO.getShopLogo()));
//        StringBuilder builder = new StringBuilder();
//        for (int x = 0; x < split.length; x++) {
//            if (x >= 1) {
//                builder.append(",");
//            }
//            builder.append(picUrl.get(split[x]));
//        }
//        shopInfoDTO.setShopImages(builder.toString());

        return shopInfoDTO;

    }

    @Override
    public void closeShop(String uuid) {
        UserDto user = CurrentUserHolder.getCurrentUser();
        if (Objects.isNull(user)) {
            throw new JSYException(-1, "请登录后操作");
        }
        if (!user.getRelationUuid().equals(uuid)) {
            throw new JSYException(-1, "不合法操作");
        }

        ShopInfo shopInfo = new ShopInfo();
        shopInfo.setUuid(uuid);
        shopInfo.setStatus(3);

        shopInfoMapper.update(shopInfo, new UpdateWrapper<ShopInfo>().eq("uuid", uuid));

        //修改店铺下所有商品的信息
        goodsBasicFeign.deleteBySid(uuid);

    }

    @Override
    public void selectShopStar(String shopUuid, Double star) {
        //查询所有一共有多少条评论，和评论星级的综
        ShopStarDo starDo = shopInfoMapper.selectShopStar(shopUuid);
        //修改店铺评分
        double sum = starDo.getStar() + star;
        double newStar = sum / starDo.getTotal();
        ShopInfo shopInfo = new ShopInfo();
        shopInfo.setStar(newStar);
        shopInfo.setUuid(shopUuid);
        shopInfoMapper.update(shopInfo, new UpdateWrapper<ShopInfo>().eq("uuid", shopUuid));
    }

    // TODO: 2020/12/15 首页推荐 范围内三家星级排名
    @Override
    public List<ShopQueryDTO> commendShop(ShopInfoQuery query) {
        //获取经纬度
        Map<String, Double> doubles = LatLonUtil.GetAround(query.getLatitude().doubleValue(), query.getLongitude().doubleValue(), query.getRadius() * 1000);
        List<ShopQueryDTO> list = shopInfoMapper.selectOrderByStar(doubles);



        ArrayList<String> uuidList = new ArrayList<>();

        //图片
        ArrayList<String> strings = new ArrayList<>();

        list.forEach(x -> {
            strings.add(x.getImages());
            uuidList.add(x.getUuid());
        });

        //查找店铺下好的商品三个
        HashMap<String, Object> map = new HashMap<>();
        map.put("shopUuid", uuidList);
        map.put("num", 1);

//        ObjectMapperUtil mapper=new ObjectMapperUtil();
//        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//        mapper.registerModule(new JavaTimeModule());

        CommonResult<List<GoodsCommendDTO>> goods = goodsBasicFeign.commendGoods(map);
        if (goods.getCode() != 0) {
            throw new JSYException(-1, goods.getMessage());
        }
        List<GoodsCommendDTO> data = goods.getData();

        for (GoodsCommendDTO datum : data) {
            strings.add(datum.getImages());
        }

        //图片
       // Map<String, String> picUrl = fileClient.getPicUrl(strings);

        for (ShopQueryDTO<GoodsCommendDTO> dto : list) {
            ArrayList<GoodsCommendDTO> goodsList = new ArrayList<>();
            //dto.setImages(picUrl.get(dto.getImages()));
            ShopInfoDTO shopInfoDTO = selectShop(dto.getUuid(), query.getLongitude().doubleValue(), query.getLatitude().doubleValue());
            dto.setActivityNameList(shopInfoDTO.getActivityList());
            dto.setShopRedpacketList(shopInfoDTO.getShopRedpacketList());

            for (GoodsCommendDTO datum : data) {
                if (dto.getUuid().equals(datum.getShopUuid())) {
                    datum.setImages(Arrays.asList(datum.getImages().split(";")).get(0));
                    goodsList.add(datum);
                }
            }
            dto.setGoodsList(goodsList);
        }
        return list;
    }

    @Override
    public Map<String, ShopInfo> getMapByUuid(List<String> uuids) {
        List<ShopInfo> list = baseMapper.selectList(new QueryWrapper<ShopInfo>().in("uuid", uuids));
        Map<String, ShopInfo> map = new HashMap<>();
        list.forEach(x -> {
            map.put(x.getUuid(), x);
        });
        return map;
    }

    @Override
    public PagerUtils<ShopInfo> getList(AdminShopQuery adminShopQuery) {
        Page<ShopInfo> page = new Page<>(adminShopQuery.getPage(), adminShopQuery.getRows());

        QueryWrapper<ShopInfo> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(adminShopQuery.getStatus())) {
            wrapper.eq("status", adminShopQuery.getStatus());
        }

        Page<ShopInfo> infoPage = baseMapper.selectPage(page, wrapper);
        PagerUtils<ShopInfo> pagerUtils = new PagerUtils<>();
        pagerUtils.setRecords(infoPage.getRecords());
        pagerUtils.setTotal((int) infoPage.getTotal());
        return pagerUtils;
    }

    @Override
    public PagerUtils<ShopActiveDTO> getActivity(ShopInfoQuery query) {

        PagerUtils<ShopActiveDTO> returnPager = new PagerUtils<>();

        //计算经纬度范围
        Double longtitud = query.getLongitude().doubleValue();
        Double latitude = query.getLatitude().doubleValue();
        Map<String, Double> doubles = LatLonUtil.GetAround(latitude, longtitud, 5000);

        //找到有满减活动的商家
        List<ShopActiveDTO> shopActiveDTOList = shopInfoMapper.selectByActive(doubles);

        List<ShopActiveDTO> returnList = new ArrayList<>();

        ArrayList<String> activityNameList = null;

        //去重的店铺uuid
        Set<String> shopUuidSet = new HashSet<>();

        //去重找到店铺uuid
        shopActiveDTOList.stream().forEach(x -> {
            shopUuidSet.add(x.getUuid());
        });
        ArrayList<String> shopUuidList = new ArrayList<>(shopUuidSet);

        //分页
        PagerUtils<String> stringPagerUtils = null;
        stringPagerUtils = new PagerUtils<>();
        stringPagerUtils = stringPagerUtils.queryPage(query.getPage(), query.getRows(), shopUuidList);

        HashMap<String, Object> map = new HashMap<>();
        map.put("shopUuid", stringPagerUtils.getRecords());
        map.put("num", 3);

        //查询店铺下的商品
        List<GoodsCommendDTO> data = goodsBasicFeign.commendGoods(map).getData();

        List<GoodsBasic> goodsBasicList = null;

        //封装属性
        for (String s : stringPagerUtils.getRecords()) {
            ShopActiveDTO dto = new ShopActiveDTO();
            dto.setUuid(s);
            activityNameList = new ArrayList<>();
            goodsBasicList = new ArrayList<>();
            for (ShopActiveDTO shopActiveDTO : shopActiveDTOList) {
                if (dto.getEstimatedTime() == 0) {
                    double distance = LatLonUtil.GetDistance(shopActiveDTO.getLongitude().doubleValue(), shopActiveDTO.getLatitude().doubleValue(), longtitud, latitude);
                    dto.setEstimatedTime(LatLonUtil.getMin(distance));
                }
                if (s.equals(shopActiveDTO.getUuid())) {
                    activityNameList.add(shopActiveDTO.getActivityName());
                    dto.setShopLogo(shopActiveDTO.getShopLogo());
                    dto.setName(shopActiveDTO.getName());
                    dto.setShopAddress(shopActiveDTO.getShopAddress());
                }
            }
            for (GoodsCommendDTO datum : data) {
                GoodsBasic goodsBasic = new GoodsBasic();
                if (s.equals(datum.getShopUuid())) {
                    goodsBasic.setTitle(datum.getTitle());
                    goodsBasic.setPrice(datum.getPrice());
                    goodsBasic.setDiscountPrice(datum.getDiscountPrice());
                    goodsBasic.setImagesUrl(datum.getImages());
                    goodsBasicList.add(goodsBasic);
                }
            }
            dto.setGoodsBasicList(goodsBasicList);
            dto.setActivityNameList(activityNameList);
            returnList.add(dto);
        }

       /* ArrayList<String> strings = new ArrayList<String>();
        for (ShopActiveDTO shopActiveDTO : returnList) {
            strings.add(shopActiveDTO.getShopLogo());
            for (GoodsBasic goodsBasic : shopActiveDTO.getGoodsBasicList()) {
                strings.add(goodsBasic.getImagesUrl());
            }
        }

        Map<String, String> picUrl = fileClient.getPicUrl(strings);*/
        for (ShopActiveDTO shopActiveDTO : returnList) {
            shopActiveDTO.setShopLogo(shopActiveDTO.getShopLogo());
            for (GoodsBasic goodsBasic : shopActiveDTO.getGoodsBasicList()) {
                goodsBasic.setImagesUrl(Arrays.asList(goodsBasic.getImagesUrl().split(";")).get(0));
            }
        }

        returnPager.setTotal(stringPagerUtils.getTotal());
        returnPager.setRecords(returnList);
        return returnPager;

    }

    @Override
    public GoodsOverViewDTO overview() {
        UserDto user = CurrentUserHolder.getCurrentUser();
        GoodsOverViewDTO overview = baseMapper.overview(user.getRelationUuid());
        return overview;
    }


}
