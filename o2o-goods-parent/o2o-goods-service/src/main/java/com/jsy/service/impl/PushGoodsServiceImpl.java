package com.jsy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.jsy.basic.util.MyPageUtils;
import com.jsy.basic.util.PageInfo;
import com.jsy.basic.util.exception.JSYException;
import com.jsy.client.NewShopClient;
import com.jsy.domain.Goods;
import com.jsy.domain.PushGoods;
import com.jsy.dto.NewShopDto;
import com.jsy.mapper.GoodsMapper;
import com.jsy.mapper.PushGoodsMapper;
import com.jsy.parameter.PushGoodsParam;
import com.jsy.query.PushGoodsQuery;
import com.jsy.service.IPushGoodsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
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
    @LcnTransaction
    public void pushGoods(PushGoodsParam pushGoodsParam) {
        Long id = pushGoodsParam.getId();
        Integer type = pushGoodsParam.getType();
        Long sort = pushGoodsParam.getSort();
        Integer pushState = pushGoodsParam.getPushState();
        if (Objects.isNull(id)){
            throw new JSYException(-1,"商品id不能为空！");
        }
        if (Objects.isNull(type)){
            throw new JSYException(-1,"推送的模块type不能为空！");
        }
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

        if (pushState==0){//直接取消返回
            //每次推送把已推送的数据取消推送
            this.outPushGoodsSort(id);
            return;
        }

        //如果是正常插入，以这一次数据为准，把之前的删除掉
        this.outPushGoodsSort(id);


        PushGoods pushGoods = new PushGoods();
        BeanUtils.copyProperties(goods,pushGoods);
        pushGoods.setLongitude(data.getLongitude());
        pushGoods.setLatitude(data.getLatitude());
        pushGoods.setSort(sort);
        pushGoods.setGoodsId(goods.getId());
        pushGoods.setPushType(type);


        if (Objects.nonNull(sort)){//设置排序插入 依次加一
            setPushGoodsSort(sort);
        }else {//不设置排序 就找到最大的sort 放在最后
            List<PushGoods> selectList = pushGoodsMapper.selectList(null);
            Optional<Long> max = selectList.stream().map(x ->
                    x.getSort()
            ).max(Comparator.comparingLong(x -> x));
            if (max.isPresent()){
                pushGoods.setSort(max.get()+1);
            }else {
                pushGoods.setSort(0L);
            }
        }

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
            List<PushGoods> collect = list.stream().sorted(Comparator.comparing(PushGoods::getSort)).collect(Collectors.toList());
            return MyPageUtils.pageMap(pushGoodsQuery.getPage(),pushGoodsQuery.getRows(),collect);
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
            List<PushGoods> collect = pushGoodsList.stream().sorted(Comparator.comparing(PushGoods::getSort)).sorted(Comparator.comparing(PushGoods::getSums)).collect(Collectors.toList());
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
            List<PushGoods> collect = pushGoodsList.stream().sorted(Comparator.comparing(PushGoods::getSort)).sorted(Comparator.comparing(PushGoods::getSums).reversed()).collect(Collectors.toList());
            return MyPageUtils.pageMap(pushGoodsQuery.getPage(),pushGoodsQuery.getRows(),collect);
        }
        if (pushGoodsQuery.getType()==3){// 价格升序
            List<PushGoods> collect = list.stream().sorted(Comparator.comparing(PushGoods::getSort)).sorted(Comparator.comparing(PushGoods::getDiscountPrice)).collect(Collectors.toList());

            return MyPageUtils.pageMap(pushGoodsQuery.getPage(),pushGoodsQuery.getRows(),collect);
        }
        if (pushGoodsQuery.getType()==4){// 价格降序
            List<PushGoods> collect = list.stream().sorted(Comparator.comparing(PushGoods::getSort)).sorted(Comparator.comparing(PushGoods::getDiscountPrice).reversed()).collect(Collectors.toList());
            return MyPageUtils.pageMap(pushGoodsQuery.getPage(),pushGoodsQuery.getRows(),collect);
        }
        return null;
    }

    /**
     * 取消推送
     * @param goodsId
     */
    @Override
    @Transactional
    @LcnTransaction
    public void outPushGoodsSort(Long goodsId) {
         pushGoodsMapper.outPushGoodsSort(goodsId);//删除数据
         PushGoods pushGoods = pushGoodsMapper.selectOne(new QueryWrapper<PushGoods>().eq("goods_id", goodsId));
         if (Objects.nonNull(pushGoods)){
             delPushGoodsSort(pushGoods.getSort());//复原sort序号
         }
        goodsMapper.update(null,new UpdateWrapper<Goods>().eq("id",goodsId).set("push_state",0));//修改未推送状态

    }

    public void setPushGoodsSort(Long sort) {

        List<PushGoods> selectList = pushGoodsMapper.selectList(new QueryWrapper<PushGoods>().eq("sort", sort));
        if (selectList.size()!=0){
            pushGoodsMapper.setPushGoodsSort(sort);
        }

    }

    public void delPushGoodsSort(Long sort) {
        List<PushGoods> selectList = pushGoodsMapper.selectList(new QueryWrapper<PushGoods>().eq("sort", sort));
        if (selectList.size()!=0){
            pushGoodsMapper.delPushGoodsSort(sort);
        }


    }

}
