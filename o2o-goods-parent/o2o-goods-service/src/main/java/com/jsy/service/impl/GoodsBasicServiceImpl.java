package com.jsy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jsy.basic.util.PageList;
import com.jsy.basic.util.exception.JSYException;
import com.jsy.basic.util.utils.CurrentUserHolder;
import com.jsy.basic.util.utils.ImagesToList;
import com.jsy.basic.util.utils.UUIDUtils;
import com.jsy.basic.util.vo.UserDto;
import com.jsy.client.FileClient;
import com.jsy.domain.*;
import com.jsy.dto.*;
import com.jsy.mapper.*;
import com.jsy.parameter.*;
import com.jsy.query.GoodsBasicQuery;
import com.jsy.service.IGoodsBasicService;
import com.jsy.service.IGoodsSpecService;
import com.jsy.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lijin
 * @since 2020-11-12
 */
@Service
@Slf4j
public class GoodsBasicServiceImpl extends ServiceImpl<GoodsBasicMapper, GoodsBasic> implements IGoodsBasicService {

    @Resource
    private GoodsBasicMapper goodsBasicMapper;
    @Resource
    private IGoodsBasicService goodsBasicService;
    @Resource
    private GoodsTypeMapper goodsTypeMapper;
    @Resource
    private FileClient fileClient;
    @Resource
    private GoodsSpecMapper goodsSpecMapper;
    @Resource
    private IGoodsSpecService goodsSpecService;
    @Resource
    private GoodsPropertyMapper goodsPropertyMapper;
    @Resource
    private GoodsOtherCostMapper goodsOtherCostMapper;

    //添加商品
    @Override
    @Transactional
    public Boolean addGoods(GoodsBasicParam goodsBasicParam) {
        GoodsBasic goodsBasic = new GoodsBasic();
        BeanUtils.copyProperties(goodsBasicParam, goodsBasic);
        goodsBasic.setUuid(UUIDUtils.getUUID());
        goodsBasic.setSales(1);//正常
        goodsBasic.setSales(0);//销量为0
        goodsBasic.setStock(10000);//默认库存10000
        goodsBasic.setStatus("1");//默认状态正常
        goodsBasic.setIsMarketable("0");//默认下架
        goodsBasicMapper.insert(goodsBasic);

        List<GoodsSpecParam> goodsSpecParams = goodsBasicParam.getGoodsSpecParams();//添加商品的规格
        goodsSpecParams.forEach((s) -> {
            GoodsSpec spec = new GoodsSpec();
            BeanUtils.copyProperties(s, spec);
            spec.setUuid(UUIDUtils.getUUID());
            spec.setGoodsUuid(goodsBasic.getUuid());
            goodsSpecMapper.insert(spec);
        });
        List<GoodsPropertyParam> goodsPropertyParams = goodsBasicParam.getGoodsPropertyParams();//添加商品的属性
        goodsPropertyParams.forEach((s) -> {
            GoodsProperty property = new GoodsProperty();
            BeanUtils.copyProperties(s, property);
            property.setUuid(UUIDUtils.getUUID());
            property.setGoodsUuid(goodsBasic.getUuid());
            goodsPropertyMapper.insert(property);
        });
        List<GoodsOtherCostParam> goodsOtherCostParams = goodsBasicParam.getGoodsOtherCostParams();//添加商品的其他收费
        goodsOtherCostParams.forEach((s) -> {
            GoodsOtherCost otherCost = new GoodsOtherCost();
            BeanUtils.copyProperties(s, otherCost);
            otherCost.setUuid(UUIDUtils.getUUID());
            otherCost.setGoodsUuid(goodsBasic.getUuid());
            goodsOtherCostMapper.insert(otherCost);
        });
        return true;
    }


    //根据商品分类uuid类型查询商品集合
    @Override
    public List<GoodsBasicDto> selectGoodSByType(GoodsBasicByTypeAndMarketable gbtm) {
        QueryWrapper<GoodsBasic> para = new QueryWrapper<>();
        para.eq("shop_uuid", gbtm.getShopUuid());
        para.eq("goods_type_uuid", gbtm.getTypeUuid());
        para.eq("status", 1);
        para.eq("is_marketable", gbtm.getIsMarketable());
        para.orderByDesc("ranks");
        List<GoodsBasic> goodsBasics = goodsBasicMapper.selectList(para);
        ArrayList<GoodsBasicDto> GoodsBasicDtoS = new ArrayList<>(goodsBasics.size());
        goodsBasics.forEach((goodsBasic) -> {
            GoodsBasicDto goodsBasicDto = new GoodsBasicDto();
            BeanUtils.copyProperties(goodsBasic, goodsBasicDto);
            GoodsBasicDtoS.add(goodsBasicDto);
        });
        return GoodsBasicDtoS;
    }


    //批量上/下架
    @Override
    @Transactional
    public Boolean putAngDownGoods(PutAndDownGoodsParam param) {
        int length = param.getGoodUuidS().size();
        int a = 0;
        for (String goodUuid : param.getGoodUuidS()) {
            GoodsBasic goodsBasic = new GoodsBasic();
            goodsBasic.setIsMarketable(param.getIsMarketable());
            int update = goodsBasicMapper.update(goodsBasic, new UpdateWrapper<GoodsBasic>().eq("uuid", goodUuid));
            a = a + update;
        }
        if (a == length) {
            return true;
        }
        return false;
    }

    //批量调整分类
    @Override
    @Transactional
    public Boolean adjustClassify(GoodsAdjustClassifyParam goodsAdjustClassifyParam) {
        int size = goodsAdjustClassifyParam.getGoodUuidS().size();
        int a = 0;
        for (String goodUuid : goodsAdjustClassifyParam.getGoodUuidS()) {
            GoodsBasic goodsBasic = new GoodsBasic();
            goodsBasic.setGoodsTypeUuid(goodsAdjustClassifyParam.getGoods_type_uuid());
            int uuid = goodsBasicMapper.update(goodsBasic, new UpdateWrapper<GoodsBasic>().eq("uuid", goodUuid));
            a = a + uuid;
        }
        if (a == size) {
            return true;
        }
        return false;
    }

    //排序商品
    @Transactional
    @Override
    public Boolean rankGoods(List<GoodRankParam> goodRankParams) {
        int size = goodRankParams.size();
        int a = 0;

        for (GoodRankParam goodRankParam : goodRankParams) {
            GoodsBasic goodsBasic = new GoodsBasic();
            goodsBasic.setRanks(goodRankParam.getRanks());
            int uuid = goodsBasicMapper.update(goodsBasic, new UpdateWrapper<GoodsBasic>().eq("uuid", goodRankParam.getUuid()));
            a = a + uuid;
        }
        if (size == a) {
            return true;
        }
        return false;
    }

    //根据商品uuid查询商品的详情
    @Override
    @Transactional
    public GoodsSelectDetailsDTO selectDetails(String goodsUUid) {
        GoodsSelectDetailsDTO goodsSelectDetailsDTO = new GoodsSelectDetailsDTO();
        GoodsBasic goodsBasic = goodsBasicMapper.selectOne(new QueryWrapper<GoodsBasic>().eq("uuid", goodsUUid));
        GoodsType goodsType = goodsTypeMapper.selectOne(new QueryWrapper<GoodsType>().eq("uuid", goodsBasic.getGoodsTypeUuid()));
        String name = goodsType.getName();
        ArrayList<GoodsSpec> specS = (ArrayList<GoodsSpec>) goodsSpecMapper.selectList(new QueryWrapper<GoodsSpec>().eq("goods_uuid", goodsUUid));
        ArrayList<GoodsProperty> properties = (ArrayList<GoodsProperty>) goodsPropertyMapper.selectList(new QueryWrapper<GoodsProperty>().eq("goods_uuid", goodsUUid));
        ArrayList<GoodsOtherCost> Others = (ArrayList<GoodsOtherCost>) goodsOtherCostMapper.selectList(new QueryWrapper<GoodsOtherCost>().eq("goods_uuid", goodsUUid));
        BeanUtils.copyProperties(goodsBasic, goodsSelectDetailsDTO);
        goodsSelectDetailsDTO.setGoodsType(name);
        goodsSelectDetailsDTO.setGoodsSpecs(specS);
        goodsSelectDetailsDTO.setGoodsOtherCosts(Others);
        goodsSelectDetailsDTO.setGoodsPropertys(properties);
        return goodsSelectDetailsDTO;
    }

    //对下架商品重新编辑修改
    @Override
    @Transactional
    public Boolean updateGoods(GoodsBasicUpdateParam goodsBasicUpdateParam) {
        String uuid1 = goodsBasicUpdateParam.getUuid();
        //查看商品的上架状态
        GoodsBasic goodsBasic1 = goodsBasicMapper.selectOne(new QueryWrapper<GoodsBasic>().eq("uuid", uuid1));
        if (goodsBasic1.getIsMarketable().equals("1")) {
            throw new JSYException(-1, "请先下架商品");
        }
        GoodsBasic goodsBasic = new GoodsBasic();
        BeanUtils.copyProperties(goodsBasicUpdateParam, goodsBasic);
        //将商品的基本信息更新
        goodsBasicMapper.update(goodsBasic, new UpdateWrapper<GoodsBasic>().eq("uuid", goodsBasic.getUuid()));


        //更新商品的属性信息
        List<GoodsPropertyUpdateParam> goodsPropertyParams = goodsBasicUpdateParam.getGoodsPropertyParams();
        for (GoodsPropertyUpdateParam goodsPropertyParam : goodsPropertyParams) {
            if (goodsPropertyParam.getPropertyName().isEmpty()) {//商品的属性名称为空，表示删除
                int uuid = goodsPropertyMapper.delete(new QueryWrapper<GoodsProperty>().eq("uuid", goodsPropertyParam.getUuid()));
            }
            GoodsProperty goodsProperty = new GoodsProperty();
            BeanUtils.copyProperties(goodsPropertyParam, goodsProperty);
            goodsPropertyMapper.update(goodsProperty, new UpdateWrapper<GoodsProperty>().eq("uuid", goodsPropertyParam.getUuid()));
        }
        //跟新商品的其他收费信息
        List<GoodsOtherCostUpdateParam> goodsOtherCostParams = goodsBasicUpdateParam.getGoodsOtherCostParams();
        for (GoodsOtherCostUpdateParam goodsOtherCostParam : goodsOtherCostParams) {
            //查询出商品的所有(商品其他收费信息)数组
            List<GoodsOtherCost> goods_uuid = goodsOtherCostMapper.selectList(new QueryWrapper<GoodsOtherCost>().eq("goods_uuid", goodsBasicUpdateParam.getUuid()));

            if (goodsOtherCostParam.getName().isEmpty()) {//商品的其他收费名称为空，表示删除
                goodsOtherCostMapper.delete(new QueryWrapper<GoodsOtherCost>().eq("uuid", goodsOtherCostParam.getUuid()));
            }
            GoodsOtherCost goodsOtherCost = new GoodsOtherCost();
            BeanUtils.copyProperties(goodsOtherCostParam, goodsOtherCost);
            goodsOtherCostMapper.update(goodsOtherCost, new UpdateWrapper<GoodsOtherCost>().eq("uuid", goodsOtherCostParam.getUuid()));
        }
        //更新商品的规格信息
        List<GoodsSpecUpdateParam> goodsSpecParams = goodsBasicUpdateParam.getGoodsSpecParams();

        for (GoodsSpecUpdateParam goodsSpecParam : goodsSpecParams) {
            if (goodsSpecParam.getSpecName().isEmpty()) {//商品规格名称为空，表示删除
                goodsSpecMapper.delete(new QueryWrapper<GoodsSpec>().eq("uuid", goodsSpecParam.getUuid()));
            }
            GoodsSpec goodsSpec = new GoodsSpec();
            BeanUtils.copyProperties(goodsSpecParam, goodsSpec);
            goodsSpecMapper.update(goodsSpec, new UpdateWrapper<GoodsSpec>().eq("uuid", goodsSpecParam.getUuid()));
        }
        return true;
    }

    //查询参加活动的商品集合（1(进行中)0(已结束)）
    @Override
    public ArrayList<SelectJoinActivityGoodsDto> SelectJoinActivityGoods(SelectJoinActivityGoodsParam param) {

        QueryWrapper<GoodsBasic> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("shop_uuid", param.getShopUuid());
        queryWrapper.eq("status", "1");//商品处于正常状态
        queryWrapper.eq("activity_status", 1);//商品有参加活动
        if (param.getRanks() == 0) {
            queryWrapper.orderByDesc("discount_end_time");//按结束时间排序
        } else {
            queryWrapper.orderByDesc("discount");//按照折扣排序
        }
        List<GoodsBasic> goodsBasics = goodsBasicMapper.selectList(queryWrapper);
        long time = new Date().getTime();//当前时间
        ArrayList<SelectJoinActivityGoodsDto> proceed = new ArrayList<>();//正在进行活动的商品集合
        ArrayList<SelectJoinActivityGoodsDto> over = new ArrayList<>();//已经结束的商品集合

        for (GoodsBasic goodsBasic : goodsBasics) {
            long startTime = goodsBasic.getDiscountStartTime().toInstant(ZoneOffset.of("+8")).toEpochMilli();//开始时间
            long endTime = goodsBasic.getDiscountEndTime().toInstant(ZoneOffset.of("+8")).toEpochMilli();//结束时间
            if (startTime < time && endTime > time) {//时间搓在开始和结束时间之间说明是正在进行
                SelectJoinActivityGoodsDto s = new SelectJoinActivityGoodsDto();
                BeanUtils.copyProperties(goodsBasic, s);
                if (goodsBasic.getDiscountStatus().equals(1)) {
                    Double discount = goodsBasic.getDiscount();
                    BigDecimal bigDecimal = new BigDecimal(discount);
                    BigDecimal price = goodsBasic.getPrice();
                    s.setDiscountPrice(price.multiply(bigDecimal));
                }
                proceed.add(s);
            } else {
                SelectJoinActivityGoodsDto s = new SelectJoinActivityGoodsDto();
                BeanUtils.copyProperties(goodsBasic, s);
                if (goodsBasic.getDiscountStatus().equals(1)) {
                    Double discount = goodsBasic.getDiscount();
                    BigDecimal bigDecimal = new BigDecimal(discount);
                    BigDecimal price = goodsBasic.getPrice();
                    s.setDiscountPrice(price.multiply(bigDecimal));
                }
                over.add(s);
            }
        }

        if (param.getStatus() == 1) {
            return proceed;
        }
        return over;
    }

    //批量修改商品的折扣
    @Override
    public Integer batchUpdateGoodsDiscount(ArrayList<BatchUpdateGoodsDiscountParam> BatchUpdateGoodsDiscountParam) {
        int i = 0;
        for (BatchUpdateGoodsDiscountParam Param : BatchUpdateGoodsDiscountParam) {
            long l = Param.getDiscountStartTime().toInstant(ZoneOffset.of("+8")).toEpochMilli();//开始时间的时间搓
            long l1 = Param.getDiscountEndTime().toInstant(ZoneOffset.of("+8")).toEpochMilli();//结束时间的时间搓
            long time = new Date().getTime();//当前时间的时间搓
            if (l < time) {
                throw new JSYException(-1, "开始时间不能小于当前时间");
            }
            if (l > l1) {
                throw new JSYException(-1, "开始时间不能大于结束时间");
            }
            GoodsBasic goodsBasic = new GoodsBasic();
            BeanUtils.copyProperties(Param, goodsBasic);
            int uuid = goodsBasicMapper.update(goodsBasic, new UpdateWrapper<GoodsBasic>().eq("uuid", Param.getUuid()));
            i = i + uuid;
        }
        return i;
    }

    //根据商品类型和折扣状态查询商品信息
    @Override
    public List<GoodsBasicDiscountDto> selectGoodsByDiscountStatusAndType(SelectGoodsByDiscountStatusAndTypeParam param) {
        QueryWrapper<GoodsBasic> para = new QueryWrapper<>();
        para.eq("shop_uuid", param.getShopUuid());
        para.eq("goods_type_uuid", param.getTypeUuid());
        para.eq("status", 1);
        List<GoodsBasic> goodsBasics = goodsBasicMapper.selectList(para);
        ArrayList<GoodsBasicDiscountDto> GoodsBasicDtoS1 = new ArrayList<>();//没参加活动
        ArrayList<GoodsBasicDiscountDto> GoodsBasicDtoS2 = new ArrayList<>();//进行中折扣活动
        ArrayList<GoodsBasicDiscountDto> GoodsBasicDtoS3 = new ArrayList<>();//已结束折扣活动
        for (GoodsBasic goodsBasic : goodsBasics) {
            long time = new Date().getTime();//当前时间的时间搓
            long l = goodsBasic.getDiscountEndTime().toInstant(ZoneOffset.of("+8")).toEpochMilli();//结束时间的时间搓;
            long l1 = goodsBasic.getDiscountStartTime().toInstant(ZoneOffset.of("+8")).toEpochMilli();//开始时间的时间搓;
            System.out.println(time + "********" + l + "**" + l1);
            Integer activityStatus = goodsBasic.getActivityStatus();//折扣活动状态(1启用0未启动)
            GoodsBasicDiscountDto goodsBasicDto = new GoodsBasicDiscountDto();
            BeanUtils.copyProperties(goodsBasic, goodsBasicDto);
            if (goodsBasic.getDiscountStatus() == 1) {//如果是按照折扣
                BigDecimal bigDecimal = new BigDecimal(goodsBasic.getDiscount());
                System.out.println(bigDecimal + "*********");
                goodsBasicDto.setDiscountPrice(goodsBasic.getPrice().multiply(bigDecimal));//折扣价格按原价*折扣=现在的折扣价格
            }
            if (activityStatus == 0) {
                GoodsBasicDtoS1.add(goodsBasicDto);
            }
            if (l1 < time && time < l) {
                GoodsBasicDtoS2.add(goodsBasicDto);
            }
            if (time > l) {
                GoodsBasicDtoS3.add(goodsBasicDto);
            }
        }
        if (param.getStatus().equals("0")) {
            return GoodsBasicDtoS1;
        }
        if (param.getStatus().equals("1")) {
            return GoodsBasicDtoS2;
        }
        if (param.getStatus().equals("2")) {
            return GoodsBasicDtoS3;
        }
        return null;
    }

    //批量修改商品的折扣
    @Override
    public Boolean batchUpdateGoodsDiscountTwo(BatchUpdateGoodsDiscountParamTwo paramTwo) {
        int a = 0;
        for (String uuid : paramTwo.getUuids()) {
            GoodsBasic goodsBasic = new GoodsBasic();
            BeanUtils.copyProperties(paramTwo, goodsBasic);
            int uuid1 = goodsBasicMapper.update(goodsBasic, new QueryWrapper<GoodsBasic>().eq("uuid", uuid));
            a = a + uuid1;
        }
        if (a == paramTwo.getUuids().size()) {
            return true;
        }

        return false;
    }

    //根据商品的uuid查询商品当前是否有折扣以及商品的折扣后的价格
    @Override
    public SelectGoodsDiscountStatuNowDto selectGoodsDiscountStatuNow(String goodUuid) {

        GoodsBasic goodsBasic = goodsBasicMapper.selectOne(new QueryWrapper<GoodsBasic>().eq("uuid", goodUuid).eq("status", 1).eq("is_marketable", 1));
        if(goodsBasic!=null){
            SelectGoodsDiscountStatuNowDto statuNowDto= new SelectGoodsDiscountStatuNowDto();
            statuNowDto.setABoolean(false);
            statuNowDto.setPrice(goodsBasic.getPrice());
            Integer activityStatus = goodsBasic.getActivityStatus();
            long time = new Date().getTime();//当前时间搓
            long l = goodsBasic.getDiscountStartTime().toInstant(ZoneOffset.of("+8")).toEpochMilli();//活动开始时间
            long l1 = goodsBasic.getDiscountEndTime().toInstant(ZoneOffset.of("+8")).toEpochMilli();//活动结束时间
            if(activityStatus==1){
                if(l<time && time<l1){
                    statuNowDto.setABoolean(true);
                    if(goodsBasic.getDiscountStatus()==1){
                        BigDecimal bigDecimal = new BigDecimal(goodsBasic.getDiscount());
                        statuNowDto.setPrice(goodsBasic.getPrice().multiply(bigDecimal));
                    }else {
                        statuNowDto.setPrice(goodsBasic.getDiscountPrice());
                    }
                }
            }
            return statuNowDto;
        }
       return null;
    }


    /******************************************************************************************/
    @Override
    @Transactional
    public void add(AddGoodsVo goodsVo) {
        log.info("新增商品入参数:{}", goodsVo);
        //1.保存商品的基本信息
        UserDto dto = CurrentUserHolder.getCurrentUser();
        GoodsBasic goodsBasic = new GoodsBasic();
        BeanUtils.copyProperties(goodsVo, goodsBasic);
        goodsBasic.setStatus("1");//默认商品状态为正常
        goodsBasic.setIsMarketable("0");//默认上架状态没有上架
        goodsBasic.setSales(0);//默认销量为0
        goodsBasic.setShopUuid(dto.getRelationUuid());//设置店铺的uuid
        goodsBasic.setUuid(UUIDUtils.getUUID());//生成商品的uuid
        goodsBasicMapper.insert(goodsBasic);
        //2.保存商品的规格信息
        if ("1".equals(goodsBasic.getIsEnableSpec())) {
            List<GoodsSpec> specList = new ArrayList<>();


            for (GoodsSpecVo goodsSpecVo : goodsVo.getSpecList()) {//遍历赋值
                GoodsSpec goodsBasic1 = new GoodsSpec();
                BeanUtils.copyProperties(goodsSpecVo, goodsBasic1);//赋值
                specList.add(goodsBasic1);//添加进specList
            }

            for (GoodsSpec goodsSpec : specList) {
                goodsSpec.setGoodsUuid(goodsBasic.getUuid());
                goodsSpec.setUuid(UUIDUtils.getUUID());
                goodsSpec.setSpecStatus("1");
                goodsSpecMapper.insert(goodsSpec);
            }
        }
    }

    /**
     * 修改商品信息
     *
     * @param goodsVo
     * @return
     */
    @Transactional
    public Integer update(GoodsVo goodsVo) {
        GoodsBasic goodsBasic = goodsBasicMapper.selectOne(new QueryWrapper<GoodsBasic>().eq("uuid", goodsVo.getUuid()));
        if (goodsBasic.getStatus().equals("0")) {
            throw new JSYException(2001, "该商品已失效");
        }
        if (goodsBasic.getIsMarketable().equals("1")) {
            throw new JSYException(2000, "上架商品不能修改,请先下架");
        }
        Integer i = null;
        //1.修改基本信息
        BeanUtils.copyProperties(goodsVo, goodsBasic);
        i = goodsBasicMapper.update(goodsBasic, new UpdateWrapper<GoodsBasic>().eq("uuid", goodsBasic.getUuid()));
        //修改规格信息
        List<GoodsSpec> gid = goodsSpecMapper.selectList(new QueryWrapper<GoodsSpec>().eq("goods_uuid", goodsVo.getUuid()));
        if (goodsVo.getSpecList().size() > 0 && !Objects.isNull(goodsVo.getSpecList())) {//判断是否有规格参数
            if (gid.size() <= 0) {//如果查询出来没有规格，进行循环添加
                for (GoodsSpec goodsSpec : gid) {
                    int insert = goodsSpecMapper.insert(goodsSpec);
                }
            }
            for (GoodsSpec goodsSpec : gid) {//循环查询出来的商品规格
                for (GoodsSpecUpdateVo spec : goodsVo.getSpecList()) { //循环传入的商品规格
                    if (goodsSpec.getUuid().equals(spec.getUuid())) {//如果uuid相等进行修改
                        BeanUtils.copyProperties(spec, goodsSpec);
                        int uuid = goodsSpecMapper.update(goodsSpec, new UpdateWrapper<GoodsSpec>().eq("uuid", goodsSpec.getUuid()));
                    }
                }
            }
        }
        return i;
    }


    @Override
    @Transactional
    //批量放入回收站
    public void batchDelete(ArrayList<String> ids) {
        if (ids.size() <= 0) {
            new JSYException(-1, "数据不能为空");
        }
        for (String id : ids) {
            goodsBasicService.deleteOne(id);
        }


    }

    @Override
    public GoodsVo findOne(String uuid) {

        //根据商品id查询规格信息
        List<GoodsSpec> specList = goodsSpecService.getByGuuid(uuid);
        List<GoodsSpecUpdateVo> gsuv = new ArrayList<>();
        for (GoodsSpec goodsSpec : specList) {
            GoodsSpecUpdateVo g = new GoodsSpecUpdateVo();
            BeanUtils.copyProperties(goodsSpec, g);
            gsuv.add(g);
        }


        //查询商品信息
        GoodsBasic goodsBasic = goodsBasicMapper.selectOne(new QueryWrapper<GoodsBasic>().eq("uuid", uuid));
        GoodsVo goodsVo = new GoodsVo();
        BeanUtils.copyProperties(goodsBasic, goodsVo);
        goodsVo.setImagesUrl(Arrays.asList(goodsBasic.getImagesUrl().split(";")).get(0));
        goodsVo.setSpecList(gsuv);

        return goodsVo;
    }

    @Override
    @Transactional
    public Boolean deleteBySid(String uuid) {
        //查出所有的商品id
        List<GoodsBasic> sid = goodsBasicMapper.selectList(new QueryWrapper<GoodsBasic>().eq("shop_uuid", uuid));

        //拼接uuid
//        String ids = null;
//        for (int x = 0; x < sid.size(); x ++) {
//            if (x >= 1) {
//                ids = ids+"," ;
//            }
//            ids = ids + sid.get(x).getUuid();
//        }
        ArrayList<String> ids = new ArrayList<>();
        for (GoodsBasic goodsBasic : sid) {
            ids.add(goodsBasic.getUuid());
        }
        batchDelete(ids);
        return true;
    }

    @Override
    public PageList<GoodsBasic> findGoodsInShop(String uuid, GoodsBasicQuery query) {
        Page<GoodsBasic> goodsBasicPage = new Page<>(query.getPage(), query.getRows());

        QueryWrapper queryWrapper = new QueryWrapper();
        if (!Objects.isNull(query.getKeyword())) {
            queryWrapper.like("title", query.getKeyword());
        }
        if (!StringUtils.isEmpty(query.getOrder()) && "1".equals(query.getOrder())) {
            queryWrapper.orderByDesc("sales");
        }
        queryWrapper.eq("shop_uuid", uuid);
        Page goodsBasics = goodsBasicMapper.selectPage(goodsBasicPage, queryWrapper);
        return new PageList<GoodsBasic>(goodsBasics.getTotal(), goodsBasics.getRecords());
    }

    @Transactional
    @Override
    public int updateGoods(String Uuid) {
        return goodsBasicMapper.updateGoods(Uuid);
    }

    @Transactional
    @Override
    public void insertProductRecord(RecordVo recordVo) {
        goodsBasicMapper.insertProductRecord(recordVo);
    }

    @Transactional
    @Override
    public void updateGoodsReturn(String Uuid) {
        goodsBasicMapper.updateGoodsReturn(Uuid);
    }

    @Override
    public List<GoodsCommendDTO> commendGoods(Map<String, Object> map) {
        return goodsBasicMapper.commendGoods(map);
    }

    @Override
    public void batchActualDel(String uuids) {

        String[] split = uuids.split(",");
        for (String s : split) {
            GoodsBasic goodsBasic = goodsBasicMapper.selectOne(new QueryWrapper<GoodsBasic>().eq("uuid", s));
            if (goodsBasic.getIsMarketable().equals("1")) {
                throw new JSYException(2003, "请先下架商品");
            }
        }

        if (StringUtils.isEmpty(uuids))
            new JSYException(-1, "数据的数据不能为空");

        List<String> list = Arrays.asList(uuids.split(","));
        baseMapper.batchActualDel(list);
    }

    @Override
    public void deleteOne(String uuid) {
        GoodsBasic goodsBasic = goodsBasicMapper.selectOne(new QueryWrapper<GoodsBasic>().eq("uuid", uuid));
        if (goodsBasic.getIsMarketable().equals("1")) {
            throw new JSYException(2003, "请先下架商品");
        }
        baseMapper.deleteOne(uuid);
    }

    @Override
    public void actualDelOne(String uuid) {
        GoodsBasic goodsBasic = goodsBasicMapper.selectOne(new QueryWrapper<GoodsBasic>().eq("uuid", uuid));
        if (goodsBasic.getIsMarketable().equals("1")) {
            throw new JSYException(2003, "请先下架商品");
        }
        baseMapper.actualDelOne(uuid);
    }

    @Override
    public void recover(List<String> uuids) {
        int i = baseMapper.recoverOne(uuids);
        if (i == 0) {
            new JSYException(-1, "参数错误");
        }
    }


    @Override
    public void onshelf(List<String> uuids, String shelfType) {
        int type = 0;

        if ("onshelf".equals(shelfType)) {
            //上架
            type = 1;
            baseMapper.onshelf(uuids, type);
            return;
        } else if ("outshelf".equals(shelfType)) {
            //下架
            baseMapper.onshelf(uuids, type);
            //删除goodsBasic表 海报外键
            for (String uuid : uuids) {
                baseMapper.update(new GoodsBasic(), new UpdateWrapper<GoodsBasic>().eq("uuid", uuid).set("poster_uuid", null));
            }
            return;
        }

        throw new JSYException(-1, "参数错误");

    }

    @Override
    public void toOtherType(String gUuid, String newTypeUuid) {
        GoodsBasic goodsBasic = goodsBasicMapper.selectOne(new QueryWrapper<GoodsBasic>().eq("uuid", gUuid));
        if (goodsBasic.getIsMarketable().equals("1")) {
            throw new JSYException(2003, "请先下架商品");
        }

        int i = baseMapper.toOtherType(gUuid, newTypeUuid);

        if (i == 0)
            throw new JSYException(-1, "参数错误");
    }

    @Override
    @Transactional
    public void saveRelatedGoods(String shopUuid, String goodsUuidIDS, String posterUuid) {
        String[] split = goodsUuidIDS.split(",");
        for (String goodsUuid : split) {
            goodsBasicMapper.update(new GoodsBasic(), new UpdateWrapper<GoodsBasic>().eq("uuid", goodsUuid).eq("shop_uuid", shopUuid).set(Objects.nonNull(posterUuid), "poster_uuid", posterUuid).set("sort", 0));
        }

    }

    @Override
    @Transactional
    public void deleteRelatedGoods(String shopUuid, String goodsUuid) {
        goodsBasicMapper.update(new GoodsBasic(), new UpdateWrapper<GoodsBasic>().eq("shop_uuid", shopUuid).eq("uuid", goodsUuid).set("poster_uuid", null).set("sort", null));
    }


}
