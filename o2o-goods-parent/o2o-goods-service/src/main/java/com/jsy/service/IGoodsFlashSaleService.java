package com.jsy.service;

import com.jsy.basic.util.vo.CommonResult;
import com.jsy.vo.RecordVo;

public interface IGoodsFlashSaleService {

    //开始抢单
    String RushPurchase(String goodsUuid, String shopUuid);

}
