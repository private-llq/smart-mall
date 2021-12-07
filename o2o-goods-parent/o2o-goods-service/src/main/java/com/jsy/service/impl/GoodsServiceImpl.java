package com.jsy.service.impl;
import java.time.LocalDateTime;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsy.basic.util.PageInfo;
import com.jsy.basic.util.exception.JSYException;
import com.jsy.basic.util.utils.BeansCopyUtils;
import com.jsy.basic.util.utils.SnowFlake;
import com.jsy.client.*;
import com.jsy.domain.*;
import com.jsy.dto.*;
import com.jsy.mapper.GoodsMapper;
import com.jsy.parameter.GoodsParam;
import com.jsy.parameter.GoodsServiceParam;
import com.jsy.query.BackstageGoodsQuery;
import com.jsy.query.BackstageServiceQuery;
import com.jsy.query.GoodsPageQuery;
import com.jsy.query.NearTheServiceQuery;
import com.jsy.service.IGoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhsj.baseweb.support.ContextHolder;
import com.zhsj.baseweb.support.LoginUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    private NewShopClient shopClient;

    @Autowired
    private TreeClient treeClient;

    @Autowired
    private BrowseClient browseClient;

    @Autowired
    private GoodsTypeClient goodsTypeClient;



    /**
     * 添加 商品
     */
    @Override
    @Transactional
    public void saveGoods(GoodsParam goodsParam) {
        Goods goods = new Goods();
        if (Objects.nonNull(goodsParam.getShopId())){
            NewShopDto newShop = shopClient.get(goodsParam.getShopId()).getData();
            if (Objects.nonNull(newShop)){
                goods.setShopName(newShop.getShopName());
            }
        }
        if (Objects.nonNull(goodsParam.getGoodsTypeId())){
            GoodsTypeDto data = goodsTypeClient.get(goodsParam.getGoodsTypeId()).getData();
            if (Objects.isNull(data)){
                throw new JSYException(-1,"商品分类不存在！");
            }
            goods.setGoodsTypeName(data.getClassifyName());
        }
        goods.setGoodsNumber(String.valueOf(SnowFlake.nextId()));
        //String[] ids = goodsParam.getServiceCharacteristicsIds().split(",");//服务特点ids
       /* ArrayList<ServiceCharacteristics> list = new ArrayList<>();
        for (String id : ids) {
           list.add(client.get(Long.valueOf(id)).getData());
        }

        list.forEach(x->{
            if (StringUtils.containsAny(x.getName(),"上门服务","上门","到家","到家服务")){
                goods.setIsVisitingService(1);//支持上门服务
            }
        });*/

        if (Objects.nonNull(goodsParam.getDiscountPrice())){
            goods.setDiscountState(1);//开启折扣
        }else {
            goods.setDiscountState(0);
        }

        goods.setType(0);//商品类
        goods.setIsPutaway(0);//默认未上架
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
        if (Objects.nonNull(goodsServiceParam.getShopId())){
            NewShopDto newShop = shopClient.get(goodsServiceParam.getShopId()).getData();
            if (Objects.isNull(newShop)){
                throw new JSYException(-1,"商家不存在！");
            }
            goods.setShopName(newShop.getShopName());
        }
        if (Objects.nonNull(goodsServiceParam.getGoodsTypeId())){
            GoodsTypeDto data = goodsTypeClient.get(goodsServiceParam.getGoodsTypeId()).getData();
            if (Objects.isNull(data)){
                throw new JSYException(-1,"商品分类不存在！");
            }
            goods.setGoodsTypeName(data.getClassifyName());
        }
        goods.setGoodsNumber(String.valueOf(SnowFlake.nextId()));
        /*String[] ids = goodsServiceParam.getServiceCharacteristicsIds().split(",");//服务特点ids
        ArrayList<ServiceCharacteristics> list = new ArrayList<>();
        StringBuffer strName=new StringBuffer();
        for (String id : ids) {
            ServiceCharacteristics data = client.get(Long.valueOf(id)).getData();
            strName.append(data.getName()+",");
            list.add(data);
        }

        list.forEach(x->{
            if (StringUtils.containsAny(x.getName(),"上门服务","上门","到家","到家服务")){
                goods.setIsVisitingService(1);//支持上门服务
            }
        });*/

        if (Objects.nonNull(goodsServiceParam.getDiscountPrice())){
            goods.setDiscountState(1);//开启折扣
        }else {
            goods.setDiscountState(0);
        }
        //String substring = strName.substring(0, strName.length() - 1);
       // goods.setServiceCharacteristicsName(substring);
        goods.setType(1);//服务类
        goods.setIsPutaway(0);//默认未上架
        BeanUtil.copyProperties(goodsServiceParam,goods);

        goodsMapper.insert(goods);
    }




    /**
     * 添加服务查询分类
     * @param shopId
     * @return
     */
    @Override
    public List<Tree> selectServiceType(Long shopId) {
        NewShopDto newShop = shopClient.get(shopId).getData();
        if (Objects.nonNull(newShop)){
            String[] ids = newShop.getShopTreeId().split(",");
            Tree tree = treeClient.getTree(Long.valueOf(ids[ids.length - 1])).getData();
            if (Objects.nonNull(tree)){
                List<Tree> treeList = treeClient.selectAllTree(tree.getParentId()).getData();
                return treeList;
            }
        }
        return null;
    }

    /**
     * 最新上架 商品、服务、套餐
     * @param shopId
     * @return
     */
    @Override
    public Goods latelyGoods(Long shopId) {
        return  goodsMapper.latelyGoods(shopId);
    }


    /**
     * 批量查询 商品
     */
    @Override
    public List<GoodsDto> batchGoods(List<Long> goodsList) {
        List<Goods> list = goodsMapper.selectList(new QueryWrapper<Goods>().in("id", goodsList).eq("type", 0));
        List<GoodsDto> goodsDtoList = BeansCopyUtils.listCopy(list, GoodsDto.class);
        return goodsDtoList;
    }

    /**
     * 批量查询 服务
     */
    @Override
    public List<GoodsServiceDto> batchGoodsService(List<Long> goodsServiceList) {

        List<Goods> list = goodsMapper.selectList(new QueryWrapper<Goods>().in("id", goodsServiceList).eq("type", 1));
        ArrayList<GoodsServiceDto> goodsServiceDtos = new ArrayList<>();
        for (Goods goods : list) {
            GoodsServiceDto goodsServiceDto = new GoodsServiceDto();
            BeanUtils.copyProperties(goods,goodsServiceDto);
            goodsServiceDtos.add(goodsServiceDto);
        }
        return goodsServiceDtos;
    }

    /**
     * 查看一条商品的所有详细信息 B端+C端
     * @param id
     *
     */
    @Override
    public GoodsDto getGoods(Long id) {
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
        Goods goods = goodsMapper.selectOne(new QueryWrapper<Goods>().eq("id", id).eq("type",0));
        if (Objects.isNull(goods)){
            throw new JSYException(-1,"没有找到该商品！");
        }

            //添加商品访问量
            long pvNum = goods.getPvNum() + 1;
            goodsMapper.update(null,new UpdateWrapper<Goods>().eq("id",id).set("pv_num",pvNum));
            //添加一条用户的浏览记录
            Browse browse = new Browse();
            browse.setShopId(goods.getShopId());
            browse.setUserId(loginUser.getId());
            browse.setName(goods.getTitle());
            browse.setTextDescription(goods.getTextDescription());
            browse.setRealPrice(goods.getPrice());
            browse.setSellingPrice(goods.getDiscountPrice());
            //browse.setIsVisitingService(goods.getIsVisitingService());
            browseClient.save(browse);

        Goods twoGoods = goodsMapper.selectOne(new QueryWrapper<Goods>().eq("id", id));
        GoodsDto goodsDto = new GoodsDto();
        BeanUtils.copyProperties(twoGoods,goodsDto);
        Long aLong = goodsMapper.sumGoodsSales(twoGoods.getId());
        goodsDto.setSums(Objects.isNull(aLong)?0:aLong);
        return goodsDto;
    }


    /**
     * 查看一条服务的所有详细信息 B端+C端
     * @param id
     *
     */
    @Override
    @Transactional
    public GoodsServiceDto getGoodsService(Long id) {
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
        Goods goods = goodsMapper.selectOne(new QueryWrapper<Goods>().eq("id", id).eq("type",1));
        if (Objects.isNull(goods)){
            throw new JSYException(-1,"没有找到该服务！");
        }
            //添加服务访问量
            long pvNum = goods.getPvNum() + 1;
            goodsMapper.update(null,new UpdateWrapper<Goods>().eq("id",id).set("pv_num",pvNum));
            //添加一条用户的浏览记录
            Browse browse = new Browse();
            browse.setShopId(goods.getShopId());
            browse.setUserId(loginUser.getId());
            browse.setName(goods.getTitle());
            browse.setTextDescription(goods.getTextDescription());
            browse.setRealPrice(goods.getPrice());
            browse.setSellingPrice(goods.getDiscountPrice());
            //browse.setIsVisitingService(goods.getIsVisitingService());
            browseClient.save(browse);

        Goods twoGoods = goodsMapper.selectOne(new QueryWrapper<Goods>().eq("id", id));
        GoodsServiceDto goodsServiceDto = new GoodsServiceDto();
        BeanUtils.copyProperties(twoGoods,goodsServiceDto);
        Long aLong = goodsMapper.sumServiceSales(twoGoods.getId());
        goodsServiceDto.setSums(Objects.isNull(aLong)?0:aLong);
        return goodsServiceDto;
    }

    /**
     * 查询店铺下面的商品 B端+C端
     * @param goodsPageQuery
     * @return
     */
    @Override
    @Transactional
    public PageInfo<GoodsDto> getGoodsAll(GoodsPageQuery goodsPageQuery) {
        Long shopId = goodsPageQuery.getShopId();
        Integer isPutaway = goodsPageQuery.getIsPutaway();
        Integer state = goodsPageQuery.getState();

        Page<Goods> page = new Page<>(goodsPageQuery.getPage(),goodsPageQuery.getRows());
            Page<Goods> goodsPage = goodsMapper.selectPage(page, new QueryWrapper<Goods>()
                    .eq("shop_id", shopId)
                    .eq(Objects.nonNull(isPutaway), "is_putaway", isPutaway)
                    .eq("type", 0)
                    .eq(Objects.nonNull(state),"state",state)
            );
            List<Goods> records = goodsPage.getRecords();

        ArrayList<GoodsDto> goodsDtos = new ArrayList<>();
        records.forEach(x->{
            GoodsDto goods = new GoodsDto();
            BeanUtils.copyProperties(x,goods);
            Long aLong = goodsMapper.sumGoodsSales(x.getId());
            if (Objects.nonNull(aLong)){
                goods.setSums(aLong);
            }else {
                goods.setSums(0L);
            }
            goodsDtos.add(goods);
        });
        PageInfo<GoodsDto> pageInfo = new PageInfo<>();
        pageInfo.setRecords(goodsDtos);
        pageInfo.setCurrent(goodsPage.getCurrent());
        pageInfo.setTotal(goodsPage.getTotal());
        pageInfo.setSize(goodsPage.getSize());
        return pageInfo;

    }

    /**
     * 查询店铺下面的服务 B端+C端
     * @param goodsPageQuery
     * @return
     */
    @Override
    public PageInfo<GoodsServiceDto> getGoodsServiceAll(GoodsPageQuery goodsPageQuery) {
        Long shopId = goodsPageQuery.getShopId();
        Integer isPutaway = goodsPageQuery.getIsPutaway();
        Integer state = goodsPageQuery.getState();
        Page<Goods> page = new Page<>(goodsPageQuery.getPage(),goodsPageQuery.getRows());
            Page<Goods> goodsPage = goodsMapper.selectPage(page, new QueryWrapper<Goods>()
                    .eq("shop_id", shopId)
                    .eq(Objects.nonNull(isPutaway), "is_putaway", isPutaway)
                    .eq("type", 1)
                    .eq(Objects.nonNull(state),"state",state)
            );
            List<Goods> records = goodsPage.getRecords();

        ArrayList<GoodsServiceDto> goodsServiceDtos = new ArrayList<>();
        records.forEach(x->{
                GoodsServiceDto goods = new GoodsServiceDto();
                BeanUtils.copyProperties(x,goods);
                Long aLong = goodsMapper.sumServiceSales(x.getId());
                if (Objects.nonNull(aLong)){
                    goods.setSums(aLong);
                }else {
                    goods.setSums(0L);
                }
            goodsServiceDtos.add(goods);
            });
            PageInfo<GoodsServiceDto> pageInfo = new PageInfo<>();
            pageInfo.setRecords(goodsServiceDtos);
            pageInfo.setCurrent(goodsPage.getCurrent());
            pageInfo.setTotal(goodsPage.getTotal());
            pageInfo.setSize(goodsPage.getSize());

        return pageInfo;
    }


    /**
     * 大后台查询商品列表
     * @param backstageGoodsQuery
     * @return
     */
    @Override
    public PageInfo<BackstageGoodsDto> backstageGetGoodsAll(BackstageGoodsQuery backstageGoodsQuery) {
        String goodsName = backstageGoodsQuery.getGoodsName();
        Long goodsTypeId = backstageGoodsQuery.getGoodsTypeId();
        LocalDateTime startTime = backstageGoodsQuery.getStartTime();
        LocalDateTime endTime = backstageGoodsQuery.getEndTime();
        Page<Goods> page = new Page<>(backstageGoodsQuery.getPage(), backstageGoodsQuery.getRows());
        Page<Goods> goodsPage = goodsMapper.selectPage(page, new QueryWrapper<Goods>()
                .eq("type", 0)
                .like(StringUtils.isNotBlank(goodsName), "title", goodsName)
                .eq(Objects.nonNull(goodsTypeId), "goods_type_id", goodsTypeId)
                .between(Objects.nonNull(startTime) && Objects.nonNull(endTime), "create_time", startTime, endTime)
        );
        List<Goods> records = goodsPage.getRecords();
        List<BackstageGoodsDto> list = BeansCopyUtils.listCopy(records, BackstageGoodsDto.class);
        PageInfo<BackstageGoodsDto> pageInfo = new PageInfo<>();
        pageInfo.setRecords(list);
        pageInfo.setCurrent(goodsPage.getCurrent());
        pageInfo.setSize(goodsPage.getSize());
        pageInfo.setTotal(goodsPage.getTotal());
        return pageInfo;
    }

    /**
     * 大后台查询服务列表
     * @param backstageServiceQuery
     * @return
     */
    @Override
    public PageInfo<BackstageServiceDto> backstageGetServiceAll(BackstageServiceQuery backstageServiceQuery) {
        String serviceName = backstageServiceQuery.getServiceName();
        Integer state = backstageServiceQuery.getState();
        LocalDateTime startTime = backstageServiceQuery.getStartTime();
        LocalDateTime endTime = backstageServiceQuery.getEndTime();
        Long goodsTypeId = backstageServiceQuery.getGoodsTypeId();
        Page<Goods> page = new Page<>(backstageServiceQuery.getPage(), backstageServiceQuery.getRows());
        Page<Goods> servicePage = goodsMapper.selectPage(page, new QueryWrapper<Goods>()
                .eq("type", 1)
                .like(StringUtils.isNotBlank(serviceName), "title", serviceName)
                .eq(Objects.nonNull(goodsTypeId), "goods_type_id", goodsTypeId)
                .eq(Objects.nonNull(state), "state", state)
                .between(Objects.nonNull(startTime) && Objects.nonNull(endTime), "create_time", startTime, endTime)


        );
        List<Goods> records = servicePage.getRecords();
        List<BackstageServiceDto> list = BeansCopyUtils.listCopy(records, BackstageServiceDto.class);
        PageInfo<BackstageServiceDto> pageInfo = new PageInfo<>();
        pageInfo.setRecords(list);
        pageInfo.setTotal(servicePage.getTotal());
        pageInfo.setSize(servicePage.getSize());
        pageInfo.setCurrent(servicePage.getSize());
        return pageInfo;
    }

    /**
     * 大后台屏蔽商家的商品
     * @param id
     */
    @Override
    @Transactional
    public void shieldGoods(Long id) {
        goodsMapper.update(null,new UpdateWrapper<Goods>().eq("id",id).set("state",1));
    }

    /**
     * 大后台显示商家的商品
     * @param id
     */
    @Override
    public void showGoods(Long id) {
        goodsMapper.update(null,new UpdateWrapper<Goods>().eq("id",id).set("state",0));
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


    /**
     * 医疗端：附近的服务
     */
    @Override
    public PageInfo<GoodsServiceDto> NearTheService(NearTheServiceQuery nearTheServiceQuery) {
        Page<GoodsServiceDto> page = new Page<>(nearTheServiceQuery.getPage(),nearTheServiceQuery.getRows());
        IPage<GoodsServiceDto> iPage = goodsMapper.NearTheService(page, nearTheServiceQuery);
        PageInfo<GoodsServiceDto> pageInfo = new PageInfo<>();
        pageInfo.setRecords(iPage.getRecords());
        pageInfo.setCurrent(iPage.getCurrent());
        pageInfo.setTotal(iPage.getTotal());
        pageInfo.setSize(pageInfo.getSize());
        return  pageInfo;
    }



}
