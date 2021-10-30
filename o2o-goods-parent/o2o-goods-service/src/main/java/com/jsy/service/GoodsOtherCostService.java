package com.jsy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jsy.domain.GoodsOtherCost;
import com.jsy.dto.SelectGoodsOtherCostByGoodsUuidDto;
import com.jsy.parameter.SelectGoodsOtherCostByGoodsUuidParam;

import java.util.List;


public interface GoodsOtherCostService extends IService<GoodsOtherCost> {


    //根据商品的uuid查询其他收费项目
    public List<SelectGoodsOtherCostByGoodsUuidDto> selectGoodsOtherCostByGoodsUuid(List<SelectGoodsOtherCostByGoodsUuidParam> params);




}
