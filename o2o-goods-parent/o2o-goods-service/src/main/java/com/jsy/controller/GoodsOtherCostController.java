package com.jsy.controller;

import com.jsy.basic.util.vo.CommonResult;
import com.jsy.dto.SelectGoodsOtherCostByGoodsUuidDto;
import com.jsy.parameter.SelectGoodsOtherCostByGoodsUuidParam;
import com.jsy.service.GoodsOtherCostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/GoodsOtherCost")
@Api(tags = "商品其他收费模块")
public class GoodsOtherCostController {

    @Resource
    private GoodsOtherCostService goodsOtherCostService;

    @ApiOperation("根据商品的uuid查询其他收费项目")
    @RequestMapping(value = "/selectGoodsOtherCostByGoodsUuid", method = RequestMethod.POST)
    public CommonResult<List<SelectGoodsOtherCostByGoodsUuidDto>> selectGoodsOtherCostByGoodsUuid( @RequestBody List<SelectGoodsOtherCostByGoodsUuidParam> goodUuids){
        List<SelectGoodsOtherCostByGoodsUuidDto> list = goodsOtherCostService.selectGoodsOtherCostByGoodsUuid(goodUuids);
        return CommonResult.ok(list);

    }

}
