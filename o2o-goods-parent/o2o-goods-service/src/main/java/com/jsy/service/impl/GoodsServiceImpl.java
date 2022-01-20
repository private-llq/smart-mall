package com.jsy.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.jsy.basic.util.PageInfo;
import com.jsy.basic.util.exception.JSYException;
import com.jsy.basic.util.utils.BeansCopyUtils;
import com.jsy.basic.util.utils.SnowFlake;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.client.*;
import com.jsy.domain.Browse;
import com.jsy.domain.Goods;
import com.jsy.domain.Tree;
import com.jsy.dto.*;
import com.jsy.mapper.GoodsMapper;
import com.jsy.parameter.GoodsParam;
import com.jsy.parameter.GoodsServiceParam;
import com.jsy.query.BackstageGoodsQuery;
import com.jsy.query.BackstageServiceQuery;
import com.jsy.query.GoodsPageQuery;
import com.jsy.query.NearTheServiceQuery;
import com.jsy.service.IGoodsService;
import com.zhsj.baseweb.support.ContextHolder;
import com.zhsj.baseweb.support.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 *  商品表 服务实现类
 * </p>
 *
 * @author lijin
 * @since 2021-11-09
 */
@Service
@Slf4j
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

    @Autowired
    private HotClient hotClient;

    @Autowired
    private SetMenuClient setMenuClient;

    @Resource
    private StringRedisTemplate redisTep;



    @Autowired
    private PushGoodsServiceImpl pushGoodsService;


    /**
     * 添加 商品
     */
    @Override
    @Transactional
    public void saveGoods(GoodsParam goodsParam) {

        if (Objects.isNull(goodsParam)){
            throw new JSYException(500,"传入对象为空！");
        }
        String[] split = goodsParam.getImages().split(",");
        if (split.length>3){
            throw new JSYException(500,"图片最多上传3张！");
        }

        Goods goods = new Goods();
        if (Objects.nonNull(goodsParam.getShopId())){
            NewShopDto newShop = shopClient.get(goodsParam.getShopId()).getData();
            if (Objects.nonNull(newShop)){
                goods.setShopName(newShop.getShopName());
                goods.setIsOfficialGoods(newShop.getIsOfficialShop());

            }
        }
        if (Objects.nonNull(goodsParam.getGoodsTypeId())){

            GoodsTypeDto data = goodsTypeClient.get(goodsParam.getGoodsTypeId()).getData();
            if (Objects.isNull(data)){
                throw new JSYException(-1,"商品分类不存在！");
            }
            CommonResult<String> commonResult = goodsTypeClient.getGoodsTypeId(data.getId());
            String goodsTypeName = commonResult.getData();
            goods.setGoodsTypeName(goodsTypeName);
        }
        goods.setGoodsNumber(String.valueOf(SnowFlake.nextId()));

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

        if (Objects.isNull(goodsServiceParam)){
            throw new JSYException(500,"传入对象为空！");
        }
        String[] split = goodsServiceParam.getImages().split(",");
        if (split.length>3){
            throw new JSYException(500,"图片最多上传3张！");
        }

        Goods goods = new Goods();
        if (Objects.nonNull(goodsServiceParam.getShopId())){
            NewShopDto newShop = shopClient.get(goodsServiceParam.getShopId()).getData();
            if (Objects.isNull(newShop)){
                throw new JSYException(-1,"商家不存在！");
            }
            goods.setShopName(newShop.getShopName());
            goods.setIsOfficialGoods(newShop.getIsOfficialShop());
        }
        if (Objects.nonNull(goodsServiceParam.getGoodsTypeId())){
            GoodsTypeDto data = goodsTypeClient.get(goodsServiceParam.getGoodsTypeId()).getData();
            if (Objects.isNull(data)){
                throw new JSYException(-1,"商品分类不存在！");
            }
            CommonResult<String> commonResult = goodsTypeClient.getGoodsTypeId(data.getId());
            String goodsTypeName = commonResult.getData();
            goods.setGoodsTypeName(goodsTypeName);
        }
        goods.setGoodsNumber(String.valueOf(SnowFlake.nextId()));
        if (Objects.nonNull(goodsServiceParam.getDiscountPrice())){
            goods.setDiscountState(1);//开启折扣
        }else {
            goods.setDiscountState(0);
        }
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
    @Transactional
    @LcnTransaction
    public GoodsDto getGoods(Long id) {
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
        Goods goods = goodsMapper.selectOne(new QueryWrapper<Goods>().eq("id", id).eq("type",0));
        if (Objects.isNull(goods)){
            throw new JSYException(-10,"没有找到该商品！");
        }
        if (goods.getState()==1){
            throw new JSYException(-10,"该商品可能存在违规已被大后台禁用！");
        }
        if (goods.getIsPutaway()==0){
            throw new JSYException(-10,"该商品处于下架状态！");
        }

        Long pvNum = statisticsPvNum(loginUser.getId(), goods.getId());
        //添加商品访问量
            goodsMapper.update(null,new UpdateWrapper<Goods>().eq("id",id).set("pv_num",pvNum));
            //添加一条用户的浏览记录
            Browse browse = new Browse();
            browse.setShopId(goods.getShopId());
            browse.setUserId(loginUser.getId());
            browse.setName(goods.getTitle());
            browse.setTextDescription(goods.getTextDescription());
            browse.setRealPrice(goods.getPrice());
            browse.setSellingPrice(goods.getDiscountPrice());
            browse.setType(0);
            browse.setGoodsId(goods.getId());
            browse.setImages(goods.getImages());
            browse.setDiscountState(goods.getDiscountState());
            //browse.setIsVisitingService(goods.getIsVisitingService());
            browseClient.save(browse);

        Goods twoGoods = goodsMapper.selectOne(new QueryWrapper<Goods>().eq("id", id));
        GoodsDto goodsDto = new GoodsDto();
        BeanUtils.copyProperties(twoGoods,goodsDto);
        Long aLong = goodsMapper.sumGoodsSales(twoGoods.getId());
        goodsDto.setSums(Objects.isNull(aLong)?0:aLong);
        return goodsDto;
    }

    public Long statisticsPvNum(Long userId,Long id) {
            //存入key
            redisTep.opsForHyperLogLog().add("pv_num" + id, userId + "-" + id);
            //统计访问量
            Long num = redisTep.opsForHyperLogLog().size("pv_num" + id);
            return num;
    }

    /**
     * 查看一条服务的所有详细信息 B端+C端
     * @param id
     *
     */
    @Override
    @Transactional
    @LcnTransaction
    public GoodsServiceDto
    getGoodsService(Long id) {
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
        Goods goods = goodsMapper.selectOne(new QueryWrapper<Goods>().eq("id", id).eq("type",1));
        if (Objects.isNull(goods)){
            throw new JSYException(-10,"没有找到该服务！");
        }
        if (goods.getState()==1){
            throw new JSYException(-10,"该服务可能存在违规已被大后台禁用！");
        }
        if (goods.getIsPutaway()==0){
            throw new JSYException(-10,"该服务处于下架状态！");
        }
        //添加服务访问量
            Long pvNum = statisticsPvNum(loginUser.getId(), goods.getId());
            goodsMapper.update(null,new UpdateWrapper<Goods>().eq("id",id).set("pv_num",pvNum));
            //添加一条用户的浏览记录
            Browse browse = new Browse();
            browse.setShopId(goods.getShopId());
            browse.setUserId(loginUser.getId());
            browse.setName(goods.getTitle());
            browse.setTextDescription(goods.getTextDescription());
            browse.setRealPrice(goods.getPrice());
            browse.setSellingPrice(goods.getDiscountPrice());
            browse.setType(1);
            browse.setGoodsId(goods.getId());
            browse.setImages(goods.getImages());
            browse.setDiscountState(goods.getDiscountState());
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
     * 查看一条商品或者服务的详细信息
     * @param id
     * @return
     */
    @Override
    @Transactional
    @LcnTransaction
    public Goods getByGoods(Long id) {
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
        Goods goods = goodsMapper.selectOne(new QueryWrapper<Goods>().eq("id", id));/*.eq("type",1));*/
        if (Objects.isNull(goods)){
            throw new JSYException(-10,"没有找到该商品！");
        }
        //添加服务访问量
        Long pvNum = statisticsPvNum(loginUser.getId(), goods.getId());
        if (Objects.nonNull(pvNum)){
            goodsMapper.update(null,new UpdateWrapper<Goods>().eq("id",id).set("pv_num",pvNum));
        }
        //添加一条用户的浏览记录
        Browse browse = new Browse();
        browse.setShopId(goods.getShopId());
        browse.setUserId(loginUser.getId());
        browse.setName(goods.getTitle());
        browse.setTextDescription(goods.getTextDescription());
        browse.setRealPrice(goods.getPrice());
        browse.setSellingPrice(goods.getDiscountPrice());
        browse.setType(goods.getType());
        browse.setGoodsId(goods.getId());
        browse.setImages(goods.getImages());
        browse.setDiscountState(goods.getDiscountState());
        //browse.setIsVisitingService(goods.getIsVisitingService());
        browseClient.save(browse);
        Goods twoGoods = goodsMapper.selectOne(new QueryWrapper<Goods>().eq("id", id));

        Long aLong = goodsMapper.sumServiceSales(twoGoods.getId());
        twoGoods.setSums(Objects.isNull(aLong)?0:aLong);
        return twoGoods;

    }





    /**
     * 查询店铺下面的商品 B端+C端
     * @param goodsPageQuery
     * @return
     */
    @Override
    @Transactional
    @LcnTransaction
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
            if (x.getVirtualState()==1){//开启虚拟销量
                  goods.setSums(x.getVirtualSales());
            }else {//未开启虚拟销量，按订单统计
                Long aLong = goodsMapper.sumGoodsSales(x.getId());
                if (Objects.nonNull(aLong)){
                    goods.setSums(aLong);
                }else {
                    goods.setSums(0L);
                }
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
                if (x.getVirtualState()==1){//开启虚拟销量
                    goods.setSums(x.getVirtualSales());
                }else {//未开启虚拟销量，按订单统计
                    Long aLong = goodsMapper.sumServiceSales(x.getId());
                    if (Objects.nonNull(aLong)){
                        goods.setSums(aLong);
                    }else {
                        goods.setSums(0L);
                    }
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
        Integer type = backstageGoodsQuery.getType();
        String goodsName = backstageGoodsQuery.getGoodsName();
        Long goodsTypeId = backstageGoodsQuery.getGoodsTypeId();
        LocalDateTime startTime = backstageGoodsQuery.getStartTime();
        LocalDateTime endTime = backstageGoodsQuery.getEndTime();
        Page<Goods> page = new Page<>(backstageGoodsQuery.getPage(), backstageGoodsQuery.getRows());
        Page<Goods> goodsPage = goodsMapper.selectPage(page, new QueryWrapper<Goods>()
                .eq("is_official_goods",backstageGoodsQuery.getType())
                .eq("type", 0)
                .like(StringUtils.isNotBlank(goodsName), "title", goodsName)
                .eq(Objects.nonNull(goodsTypeId), "goods_type_id", goodsTypeId)
                .eq("is_official_goods",type)
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
        int update = goodsMapper.update(null, new UpdateWrapper<Goods>().eq("id", id).set("state", 1));
        if (update>=0){
            hotClient.getHotGoods(id);
        }
    }

    /**
     * 大后台显示商家的商品
     * @param id
     */
    @Override
    @Transactional
    public void showGoods(Long id) {
        goodsMapper.update(null,new UpdateWrapper<Goods>().eq("id",id).set("state",0));
    }

    /**
     * 大后台设置商品+服务的虚拟销量
     * @param id
     */
    @Override
    @Transactional
    public void virtualSales(Long id, Long num) {
        if (Objects.nonNull(num)){
            goodsMapper.update(null,new UpdateWrapper<Goods>().eq("id",id).set("virtual_state",1).set("virtual_sales",num));
        }
    }




    /**
     * 上架商品/服务
     * @param id
     */
    @Override
    @Transactional
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
    @Transactional
    public void outaway(Long id) {
        Goods goods = new Goods();
        goods.setIsPutaway(0);
        int update = goodsMapper.update(goods, new QueryWrapper<Goods>().eq("id", id));
        if (update>=0){
            hotClient.getHotGoods(id);//更新热门数据！
            pushGoodsService.outPushGoodsSort(id);//取消推送 ！
        }
    }


    /**
     * 一键上架商品/服务
     */
    @Override
    @Transactional
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
        pageInfo.setSize(iPage.getSize());
        return  pageInfo;
    }

    /**
     * 医疗端：附近的服务2
     */
    @Override
    public List<GoodsServiceDto> NearTheService2(NearTheServiceQuery nearTheServiceQuery) {
        List<GoodsServiceDto> goodsServiceDtos=goodsMapper.NearTheService2(nearTheServiceQuery);
        return goodsServiceDtos;
    }

    /**
     * 商家被禁用，同步禁用商家的商品和服务
     */
    @Override
    @Transactional
    public void disableAll(Long shopId,Integer type) {
        if (type==0){//禁用
            goodsMapper.update(null,new UpdateWrapper<Goods>().eq("shop_id",shopId).set("state",1));
        }
        if (type==1){//取消禁用
            goodsMapper.update(null,new UpdateWrapper<Goods>().eq("shop_id",shopId).set("state",0));
        }
    }


    /**
     * 查询状态 ture 正常 false 不正常
     * type ：0 商品  1:服务  2：套餐  3：商店
     */
    @Override
    public Boolean selectState(Long id, Integer type) {
        if (Objects.isNull(type)){
            throw new JSYException(-1,"type不能为空！");
        }
        if (type==0 || type==1){
            Goods goods = goodsMapper.selectOne(new QueryWrapper<Goods>().eq("id", id).eq("state", 0).eq("is_putaway", 1));
            if (Objects.nonNull(goods)){
                return true;
            }
            return false;
        }
        if (type==2){
            SetMenuDto setMenu = setMenuClient.SetMenuList(id).getData();
            if (setMenu.getState()==1||setMenu.getIsDisable()==0){
                return true;
            }
            return false;
        }
        if (type==3){
            NewShopDto newShop = shopClient.get(id).getData();
            if (newShop.getShielding()==0){
                return true;
            }
            return false;
        }
        return null;
    }

    @Override
    public Integer getGoodsNumber(Long shopId, Integer type) {
        Integer count = goodsMapper.selectCount(new QueryWrapper<Goods>().eq("shop_id", shopId).eq("type", type));
        return count;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        goodsMapper.deleteById(id);
        pushGoodsService.outPushGoodsSort(id);//取消推送
        hotClient.getHotGoods(id);//更新热门数据
    }


}
