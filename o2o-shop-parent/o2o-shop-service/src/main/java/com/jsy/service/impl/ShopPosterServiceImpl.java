package com.jsy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jsy.basic.util.exception.JSYException;
import com.jsy.client.FileClient;
import com.jsy.domain.ShopPoster;
import com.jsy.mapper.ShopPosterMapper;
import com.jsy.service.IShopPosterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jsy.vo.SortVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yu
 * @since 2021-03-27
 */
@Service
public class ShopPosterServiceImpl extends ServiceImpl<ShopPosterMapper, ShopPoster> implements IShopPosterService {

    @Resource
    private ShopPosterMapper shopPosterMapper;

    @Autowired
    private FileClient fileClient;

    @Override
    public ShopPoster getPoster(String shopUuid) {
        ShopPoster shopPoster = shopPosterMapper.getPoster(shopUuid);
        return shopPoster;


    }

    //海报关联商品排序
    @Override
    public void setSort(String shopUuid, List<SortVo> sortVo) {
        shopPosterMapper.setSort(shopUuid,sortVo);

    }

    //海报排序
    @Override
    public void setSort2(String shopUuid, List<SortVo> sortVo) {
        shopPosterMapper.setSort2(shopUuid,sortVo);
    }


    /**
     * 传入状态  1 展示中 2 即将发布 -1已过期  0 停用（撤销中）
     * @param type
     * @param shopUuid
     * @return
     */
    @Override
    public List<ShopPoster> listPoster(Integer type,String shopUuid) {
        long localTime = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));//当前系统时间戳
        List<ShopPoster> list = shopPosterMapper.selectList(new QueryWrapper<ShopPoster>().eq("shop_uuid",shopUuid));

        //展示中接口 true 需要 false不需要
        if (type==1){
            List<ShopPoster> collect = list.stream().filter(x -> {
                //拼接永久状态的
                if (x.getTermValidity() == 0 && x.getState() != 0) {
                    return true;
                }
                /*//没有开始时间，有结束时间
                if (Objects.isNull(x.getStartTime()) && Objects.nonNull(x.getEndTime()) && x.getSort() != 0) {
                    if (localTime<=x.getEndTime().toEpochSecond(ZoneOffset.of("+8"))){
                        return true;
                    }
                    return false;
                }
                //没有结束时间，有开始时间
                if (Objects.isNull(x.getEndTime()) && Objects.nonNull(x.getStartTime()) && x.getSort() != 0) {
                    if (localTime>=x.getStartTime().toEpochSecond(ZoneOffset.of("+8"))){
                        return true;
                    }
                    return false;
                }*/
                //开始时间和结束时间都有
                if (Objects.nonNull(x.getStartTime()) && Objects.nonNull(x.getEndTime()) && x.getState() != 0) {
                    if (localTime>=x.getStartTime().toEpochSecond(ZoneOffset.of("+8"))&&localTime<=x.getEndTime().toEpochSecond(ZoneOffset.of("+8"))){
                        return true;
                    }
                    return false;
                }else {
                    return false;
                }
            }).sorted(Comparator.comparing(ShopPoster::getSort)).collect(Collectors.toList());


            return collect;
        }
        //即将发布接口
        if (type==2){
            List<ShopPoster> collect = list.stream().filter(x -> {
               /* if (Objects.isNull(x.getStartTime())) {
                    return false;
                }*/
                if (localTime < x.getStartTime().toEpochSecond(ZoneOffset.of("+8")) && x.getState() != 0) {
                    return true;
                }
                return false;
            }).collect(Collectors.toList());

            return collect;
        }
        //已过期接口
        if (type==-1){
            List<ShopPoster> collect = list.stream().filter(x -> {
                /*if(Objects.isNull(x.getEndTime())){
                    return false;
                }*/
                if (localTime>x.getEndTime().toEpochSecond(ZoneOffset.of("+8"))&&x.getState()!=0){
                    return true;
                }
                return false;
            }).collect(Collectors.toList());

            return collect;
        }
        //停用（撤销中）接口
        if (type==0){
            List<ShopPoster> collect = shopPosterMapper.selectList(new QueryWrapper<ShopPoster>().eq("state", 0));
            return collect;
        }
     return null;
    }

    @Override
    public void savePoster(ShopPoster shopPoster) {
        //如果是永久展示
        if (shopPoster.getTermValidity()==0){
            shopPosterMapper.insert(shopPoster);
            return;
        }

        //自定义时间段
        long localTime = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));//当前系统时间戳

        if(localTime>shopPoster.getStartTime().toEpochSecond(ZoneOffset.of("+8"))){
            throw new JSYException(-1,"海报开始时间不能设置在当前时间之前！");
        }

        //自定义时间段-->即将发布-->需要传入开始时间和结束时间
        if (shopPoster.getTermValidity()==1){
            if (Objects.isNull(shopPoster.getStartTime())||Objects.isNull(shopPoster.getEndTime())){
                throw new JSYException(-1,"开始和结束时间为必填项！");
            }

        }
        List<ShopPoster> list = this.listPoster(1, shopPoster.getShopUuid());


        //展示+即将发布超过5张
        if (list.size()>=5){
            throw new JSYException(-1,"最多只能发布5张海报！");
        }

        shopPosterMapper.insert(shopPoster);
    }

    @Override
    public void deletePoster(String posterUuid, String shopUuid) {

        ShopPoster shopPoster = shopPosterMapper.selectOne(new QueryWrapper<ShopPoster>().eq("poster_uuid",posterUuid).eq("shop_uuid",shopUuid));
        if (shopPoster.getState()!=0){
            new JSYException(-1,"请先撤销才能删除!");
        }
        shopPosterMapper.delete(new QueryWrapper<ShopPoster>().eq("poster_uuid",posterUuid).eq("shop_uuid",shopUuid));
    }

}
