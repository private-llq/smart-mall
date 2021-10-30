package com.jsy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jsy.client.FileClient;
import com.jsy.domain.GoodsBasic;
import com.jsy.domain.GoodsType;
import com.jsy.dto.GoodsTypeDTO;
import com.jsy.mapper.GoodsBasicMapper;
import com.jsy.mapper.GoodsTypeMapper;
import com.jsy.service.IGoodsBasicService;
import com.jsy.service.IGoodsTypeService;
import io.reactivex.rxjava3.internal.operators.flowable.FlowableOnErrorReturn;
import org.aspectj.weaver.ast.Var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.xml.crypto.Data;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lijin
 * @since 2020-11-12
 */
@Service
public class GoodsTypeServiceImpl extends ServiceImpl<GoodsTypeMapper, GoodsType> implements IGoodsTypeService {

    @Resource
    private GoodsBasicMapper goodsBasicMapper;

    @Resource
    private GoodsTypeMapper goodsTypeMapper;

    @Resource
    private IGoodsBasicService goodsBasicService;

    @Autowired
    private FileClient fileClient;

    @Override
    @Transactional
    public List<GoodsBasic> findGoodsByTypeId(String uuid) {
        List<GoodsBasic> basicList = goodsBasicMapper.selectList(new QueryWrapper<GoodsBasic>().eq("goods_type_uuid", uuid));
        return basicList;
    }

    @Override
    @Transactional
    public boolean deleteByUuid(String uuid) {
        try {
            //删除分类
            goodsTypeMapper.delete(new QueryWrapper<GoodsType>().eq("uuid",uuid));

            List<GoodsBasic> goodsByTypeId = findGoodsByTypeId(uuid);
            //查询类型下所有的商品的uuid
            List<String> list = goodsByTypeId.stream().map((entity) -> {
                return entity.getUuid();
            }).collect(Collectors.toList());

            String ids = "";
            for (int x = 0; x<list.size(); x ++) {
                if (x >= 1) {
                    ids += ",";
                }
                ids += list.get(x);
            }
           // goodsBasicService.batchDelete(ids);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<GoodsTypeDTO> findGoodsType(String uuid) {
        List<GoodsTypeDTO> goodsType = goodsTypeMapper.findGoodsType(uuid);
        for (GoodsTypeDTO goodsTypeDTO : goodsType) {
            for (GoodsBasic goodsBasic : goodsTypeDTO.getBasicList()) {
                goodsBasic.setDiscountIngStatus(0);
                long l1 = goodsBasic.getDiscountStartTime().toEpochSecond(ZoneOffset.of("+8"));
                long l = goodsBasic.getDiscountEndTime().toEpochSecond(ZoneOffset.of("+8"));
                long time = new Date().getTime();

                if(l1< time && time<l){
                    goodsBasic.setDiscountIngStatus(1);
                }

                String s = Arrays.stream(goodsBasic.getImagesUrl().split(";")).collect(Collectors.toList()).get(0);
                goodsBasic.setImagesUrl(s);
            }
        }


//        //更改图片传输信息
//        ArrayList<String> strings = new ArrayList<>();
//        for (GoodsTypeDTO goodsTypeDTO : goodsType) {
//            for (GoodsBasic goodsBasic : goodsTypeDTO.getBasicList()) {
//                strings.add(goodsBasic.getImagesUrl());
//            }
//        }
//
//        Map<String, String> picUrl = fileClient.getPicUrl(strings);
//        for (GoodsTypeDTO goodsTypeDTO : goodsType) {
//            for (GoodsBasic goodsBasic : goodsTypeDTO.getBasicList()) {
//                goodsBasic.setImagesUrl(picUrl.get(goodsBasic.getImagesUrl()));
//            }
//        }

        return goodsType;

    }
}
