package com.jsy.client.impl;

import com.jsy.basic.util.vo.CommonResult;
import com.jsy.client.GoodsTypeClient;
import com.jsy.dto.GoodsTypeDto;
import org.springframework.web.bind.annotation.GetMapping;

public class GoodsTypeClientImpl implements GoodsTypeClient {
    @Override
    public CommonResult<GoodsTypeDto> get(Long id) {
        return null;
    }

    @GetMapping("/industryCategory/getGoodsTypeId")
    @Override
    public CommonResult<String>  getGoodsTypeId(Long id) {
        return null;
    }


}
