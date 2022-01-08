package com.jsy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jsy.basic.util.MyPageUtils;
import com.jsy.basic.util.PageInfo;
import com.jsy.basic.util.exception.JSYException;
import com.jsy.client.NewShopClient;
import com.jsy.domain.Goods;
import com.jsy.domain.PushGoods;
import com.jsy.dto.NewShopDto;
import com.jsy.mapper.GoodsMapper;
import com.jsy.mapper.PushGoodsMapper;
import com.jsy.query.PushGoodsQuery;
import com.jsy.service.IPushGoodsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lijin
 * @since 2021-12-06
 */
@Service
public class PushGoodsServiceImpl extends ServiceImpl<PushGoodsMapper, PushGoods> implements IPushGoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private PushGoodsMapper pushGoodsMapper;

    @Autowired
    private NewShopClient newShopClient;

    /**
     * 医疗端：推送产品（智能手环、手表）
     */
    @Override
    @Transactional
    public void pushGoods(Long id,Integer type) {
        Goods goods = goodsMapper.selectOne(new QueryWrapper<Goods>().eq("id", id));
        if (Objects.isNull(goods)){
            throw new JSYException(-1,"未找到该商品！");
        }
        if (goods.getIsPutaway()==0){
            throw new JSYException(-1,"该商品处于下架状态不能推送！");
        }
        if (goods.getState()==1){
            throw new JSYException(-1,"该商品已被禁用，不能推送！");
        }
        if (goods.getShopId()==null){
            throw new JSYException(-1,"商店id不能为空！");
        }
        NewShopDto data = newShopClient.get(goods.getShopId()).getData();
        if (Objects.isNull(data)){
           throw new JSYException(-1,"该店铺不存在！");
        }
        if (data.getShielding()==1){
           throw new JSYException(-1,"该店铺已经被屏蔽！");
        }
        PushGoods pushGoods = new PushGoods();
        BeanUtils.copyProperties(goods,pushGoods);
        pushGoods.setLongitude(data.getLongitude());
        pushGoods.setLatitude(data.getLatitude());
        int insert = pushGoodsMapper.insert(pushGoods);
        if (insert>0){//修改商品的推送状态
            goodsMapper.update(null,new UpdateWrapper<Goods>().eq("id",id).set("push_state",type));
        }

    }

    /**
     * 医疗端：查询推送的产品（智能手环、手表）
     */
    @Override
    public PageInfo<PushGoods> pageListPushGoods(PushGoodsQuery pushGoodsQuery) {
        BigDecimal longitude = pushGoodsQuery.getLongitude();
        BigDecimal latitude = pushGoodsQuery.getLatitude();
        if (Objects.isNull(longitude)||Objects.isNull(latitude)){
            throw  new JSYException(-1,"经纬度不能为空！");
        }
        List<PushGoods> list = pushGoodsMapper.pageListPushGoods(pushGoodsQuery);

        if (pushGoodsQuery.getType()==0){//默认
            return MyPageUtils.pageMap(pushGoodsQuery.getPage(),pushGoodsQuery.getRows(),list);
        }

        if (pushGoodsQuery.getType()==1){//销量升序
            ArrayList<PushGoods> pushGoodsList = new ArrayList<>();
            list.forEach(x->{
                PushGoods pushGoods = new PushGoods();
                BeanUtils.copyProperties(x,pushGoods);
                Long aLong = pushGoodsMapper.sumGoodsSales(x.getId());
                if (Objects.nonNull(aLong)){
                    pushGoods.setSums(aLong);
                }else {
                    pushGoods.setSums(0L);
                }
                pushGoodsList.add(pushGoods);

            });
            List<PushGoods> collect = pushGoodsList.stream().sorted(Comparator.comparing(PushGoods::getSums)).collect(Collectors.toList());
            return MyPageUtils.pageMap(pushGoodsQuery.getPage(),pushGoodsQuery.getRows(),collect);
        }
        if (pushGoodsQuery.getType()==2){//销量降序
            ArrayList<PushGoods> pushGoodsList = new ArrayList<>();
            list.forEach(x->{
                PushGoods pushGoods = new PushGoods();
                BeanUtils.copyProperties(x,pushGoods);
                Long aLong = pushGoodsMapper.sumGoodsSales(x.getId());
                if (Objects.nonNull(aLong)){
                    pushGoods.setSums(aLong);
                }else {
                    pushGoods.setSums(0L);
                }
                pushGoodsList.add(pushGoods);

            });
            List<PushGoods> collect = pushGoodsList.stream().sorted(Comparator.comparing(PushGoods::getSums).reversed()).collect(Collectors.toList());
            return MyPageUtils.pageMap(pushGoodsQuery.getPage(),pushGoodsQuery.getRows(),collect);
        }
        if (pushGoodsQuery.getType()==3){// 价格升序
            List<PushGoods> collect = list.stream().sorted(Comparator.comparing(PushGoods::getDiscountPrice)).collect(Collectors.toList());

            return MyPageUtils.pageMap(pushGoodsQuery.getPage(),pushGoodsQuery.getRows(),collect);
        }
        if (pushGoodsQuery.getType()==4){// 价格降序
            List<PushGoods> collect = list.stream().sorted(Comparator.comparing(PushGoods::getDiscountPrice).reversed()).collect(Collectors.toList());
            return MyPageUtils.pageMap(pushGoodsQuery.getPage(),pushGoodsQuery.getRows(),collect);
        }
        return null;
    }

    @Override
    public void setPushGoodsSort(Integer sort) {

    }

}
