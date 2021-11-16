package com.jsy.service;

import com.jsy.basic.util.PageInfo;
import com.jsy.domain.Goods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jsy.domain.Tree;
import com.jsy.parameter.GoodsParam;
import com.jsy.parameter.GoodsServiceParam;
import com.jsy.query.GoodsPageQuery;

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
     * 查看一条商品/服务的所有信息
     * @param id
     */
    Goods getGoodsService(Long id);

    /**
     * 查询店铺下面的所有商品+服务
     */

    PageInfo<Goods> getGoodsAll(GoodsPageQuery goodsPageQuery);


    /**
     * 上架商品/服务
     * @param id
     */
    void putaway(Long id);

    /**
     * 下架商品/服务
     * @param id
     */
    void outaway(Long id);

    /**
     * 一键上架商品/服务
     */
    void putawayAll(List idList);

    /**
     * 添加服务查询分类
     * @param shopId
     * @return
     */
    List<Tree> selectServiceType(Long shopId);
}
