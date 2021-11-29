package com.jsy.service;

import com.jsy.basic.util.PageInfo;
import com.jsy.domain.BackstageGoods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jsy.dto.BacksageDto;
import com.jsy.dto.BackstageGoodsDto;
import com.jsy.param.BackstageGoodsParam;
import com.jsy.query.BackstageGoodsQuery;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Tian
 * @since 2021-11-29
 */
public interface IBackstageGoodsService extends IService<BackstageGoods> {

    void addBackstageGoods(BackstageGoodsParam backstageGoodsParam);

    void updateBackstageGoods(BackstageGoodsParam backstageGoodsParam);

    PageInfo<BacksageDto>  pageBackstageGoods(BackstageGoodsQuery backstageGoodsQuery);

    /**
     * 分页条件查询 医疗C端页面
     * @param backstageGoodsQuery
     * @return
     */
    PageInfo<BackstageGoodsDto> listBackstageGoods(BackstageGoodsQuery backstageGoodsQuery);
}
