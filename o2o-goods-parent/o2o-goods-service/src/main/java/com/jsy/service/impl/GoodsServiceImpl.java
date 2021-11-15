package com.jsy.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jsy.client.ServiceCharacteristicsClient;
import com.jsy.domain.Goods;
import com.jsy.domain.ServiceCharacteristics;
import com.jsy.mapper.GoodsMapper;
import com.jsy.parameter.GoodsParam;
import com.jsy.parameter.GoodsServiceParam;
import com.jsy.service.IGoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 *  商品表 服务实现类
 * </p>
 *
 * @author lijin
 * @since 2021-11-09
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements IGoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private ServiceCharacteristicsClient client;



    /**
     * 添加 商品
     */
    @Override
    @Transactional
    public void saveGoods(GoodsParam goodsParam) {

        Goods goods = new Goods();

        String[] ids = goodsParam.getServiceCharacteristicsIds().split(",");//服务特点ids

        ArrayList<ServiceCharacteristics> list = new ArrayList<>();
        for (String id : ids) {
           list.add(client.get(Long.valueOf(id)).getData());
        }

        list.forEach(x->{
            if (StringUtils.containsAny(x.getName(),"上门服务","上门","到家","到家服务")){
                goods.setIsVisitingService(1);//支持上门服务
            }
        });

        if (Objects.nonNull(goodsParam.getDiscountPrice())){
            goods.setDiscountState(1);//开启折扣
        }else {
            goods.setDiscountState(0);
        }

        goods.setType(0);//商品类

        BeanUtil.copyProperties(goodsParam,goods);
        goodsMapper.insert(goods);
    }


    /**
     * 添加 服务
     */
    @Override
    @Transactional
    public void saveService(GoodsServiceParam goodsServiceParam) {

        Goods goods = new Goods();

        String[] ids = goodsServiceParam.getServiceCharacteristicsIds().split(",");//服务特点ids

        ArrayList<ServiceCharacteristics> list = new ArrayList<>();
        for (String id : ids) {
            list.add(client.get(Long.valueOf(id)).getData());
        }

        list.forEach(x->{
            if (StringUtils.containsAny(x.getName(),"上门服务","上门","到家","到家服务")){
                goods.setIsVisitingService(1);//支持上门服务
            }
        });

        if (Objects.nonNull(goodsServiceParam.getDiscountPrice())){
            goods.setDiscountState(1);//开启折扣
        }else {
            goods.setDiscountState(0);
        }

        goods.setType(1);//服务类
        BeanUtil.copyProperties(goodsServiceParam,goods);

        goodsMapper.insert(goods);
    }

    /**
     * 查看一条商品/服务的所有信息
     * @param id
     */
    @Override
    public Goods getGoodsService(Long id) {
        Goods goods = goodsMapper.selectOne(new QueryWrapper<Goods>().eq("id", id));
        return goods;
    }

    /**
     * 查询店铺下面的所有商品+服务
     */
    @Override
    public Map<Integer, List<Goods>> getGoodsAll(Long shopId) {
        List<Goods> list = goodsMapper.selectList(new QueryWrapper<Goods>().eq("shop_id", shopId).eq("is_putaway",1).eq("deleted",0));
        Map<Integer, List<Goods>> collect = list.stream().collect(Collectors.groupingBy(Goods::getType));
        return collect;
    }

    /**
     * 上架商品/服务
     * @param id
     */
    @Override
    public void putaway(Long id) {
        Goods goods = new Goods();
        goods.setIsPutaway(1);
        goodsMapper.update(goods,new QueryWrapper<Goods>().eq("id",id));

    }

    /**
     * 下架商品/服务
     * @param id
     */
    @Override
    public void outaway(Long id) {
        Goods goods = new Goods();
        goods.setIsPutaway(0);
        goodsMapper.update(goods,new QueryWrapper<Goods>().eq("id",id));
    }


    /**
     * 一键上架商品/服务
     */
    @Override
    public void putawayAll(List idList) {
        Goods goods = new Goods();
        goods.setIsPutaway(1);
        goodsMapper.update(goods,new QueryWrapper<Goods>().in("id",idList));
    }

}
