package com.jsy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsy.basic.util.MyPageUtils;
import com.jsy.basic.util.PageInfo;
import com.jsy.basic.util.exception.JSYException;
import com.jsy.client.NewShopClient;
import com.jsy.client.ShopClient;
import com.jsy.domain.Goods;
import com.jsy.domain.PushGoods;
import com.jsy.mapper.GoodsMapper;
import com.jsy.mapper.PushGoodsMapper;
import com.jsy.query.PushGoodsQuery;
import com.jsy.service.IPushGoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    /**
     * 医疗端：推送产品（智能手环、手表）
     */
    @Override
    public void pushGoods(Long id) {
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
        PushGoods pushGoods = new PushGoods();
        BeanUtils.copyProperties(goods,pushGoods);
        pushGoodsMapper.insert(pushGoods);
    }

    /**
     * 医疗端：查询推送的产品（智能手环、手表）
     */
    @Override
    public PageInfo<PushGoods> pageListPushGoods(PushGoodsQuery pushGoodsQuery) {
        String keyword = pushGoodsQuery.getKeyword();
        List<PushGoods> list = pushGoodsMapper.selectList(new QueryWrapper<PushGoods>()
                .like(StringUtils.isNotBlank(keyword), "title", keyword));

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

}
