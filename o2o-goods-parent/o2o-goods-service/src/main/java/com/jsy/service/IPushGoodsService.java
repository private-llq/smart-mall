package com.jsy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jsy.basic.util.PageInfo;
import com.jsy.domain.PushGoods;
import com.jsy.parameter.PushGoodsParam;
import com.jsy.query.PushGoodsQuery;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lijin
 * @since 2021-12-06
 */
public interface IPushGoodsService extends IService<PushGoods> {

    /**
     * 医疗端：推送产品（智能手环、手表）
     */
    void pushGoods(PushGoodsParam pushGoodsParam);

    /**
     * 医疗端：查询推送的产品（智能手环、手表）
     */
    PageInfo<PushGoods> pageListPushGoods(PushGoodsQuery pushGoodsQuery);

    /**
     * 取消推送
     * @param goodsId
     */
    void outPushGoodsSort(Long goodsId);

}
