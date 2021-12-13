package com.jsy.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jsy.basic.util.PageInfo;
import com.jsy.basic.util.utils.BeansCopyUtils;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.domain.Goods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jsy.domain.Tree;
import com.jsy.dto.*;
import com.jsy.parameter.GoodsParam;
import com.jsy.parameter.GoodsServiceParam;
import com.jsy.query.*;

import java.util.List;

/**
 * <p>
 *  商品表 服务类
 * </p>
 *
 * @author lijin
 * @since 2021-11-09
 */
public interface IGoodsService extends IService<Goods> {


    /**
     * 添加 商品
     */
    void saveGoods(GoodsParam goodsParam);

    /**
     * 添加 服务
     */
    void saveService(GoodsServiceParam goodsServiceParam);


    /**
     * 查看一条商品的所有详细信息 B端+C端
     * @param id
     *
     */
    GoodsDto getGoods(Long id);

    /**
     * 查看一条服务的所有详细信息 B端+C端
     * @param id
     *
     */
    GoodsServiceDto getGoodsService(Long id);

    /**
     * 查询店铺下面的商品+服务 B端
     * @param goodsPageQuery   0:普通商品 1：服务类商品
     * @return
     */
    PageInfo<GoodsDto> getGoodsAll(GoodsPageQuery goodsPageQuery);


    /**
     * 查询店铺下面的服务 B端+C端
     * @param goodsPageQuery
     * @return
     */
    PageInfo<GoodsServiceDto> getGoodsServiceAll(GoodsPageQuery goodsPageQuery);


    /**
     * 上架商品/服务
     *
     * @param id
     */
    void putaway(Long id);

    /**
     * 下架商品/服务
     *
     * @param id
     */
    void outaway(Long id);

    /**
     * 一键上架商品/服务
     */
    void putawayAll(List idList);

    /**
     * 添加服务查询分类
     *
     * @param shopId
     * @return
     */
    List<Tree> selectServiceType(Long shopId);


    /**
     * 最新上架 商品、服务、套餐
     *
     * @param shopId
     * @return
     */
    Goods latelyGoods(Long shopId);


    /**
     * 批量查询 商品
     */

    List<GoodsDto> batchGoods(List<Long> goodsList);

    /**
     * 批量查询 服务
     */
    List<GoodsServiceDto> batchGoodsService(List<Long> goodsServiceList);

    /**
     * 大后台查询商品列表
     * @param backstageGoodsQuery
     * @return
     */
    PageInfo<BackstageGoodsDto> backstageGetGoodsAll(BackstageGoodsQuery backstageGoodsQuery);

    /**
     * 大后台查询服务列表
     * @param backstageServiceQuery
     * @return
     */
    PageInfo<BackstageServiceDto> backstageGetServiceAll(BackstageServiceQuery backstageServiceQuery);

    /**
     * 大后台屏蔽商家的商品
     * @param id
     */
    void shieldGoods(Long id);

    /**
     * 大后台显示商家的商品
     * @param id
     */
    void showGoods(Long id);


    /**
     * 医疗端：附近的服务
     */
    PageInfo<GoodsServiceDto> NearTheService(NearTheServiceQuery nearTheServiceQuery);


    /**
     * 大后台设置商品+服务的虚拟销量
     * @param id
     */
    void virtualSales(Long id, Long num);

    /**
     * 查看一条商品或者服务的详细信息
     * @param id
     * @return
     */
    Goods getByGoods(Long id);

    /**
     * 医疗端：附近的服务2
     */
    List<GoodsServiceDto> NearTheService2(NearTheServiceQuery nearTheServiceQuery);
}
