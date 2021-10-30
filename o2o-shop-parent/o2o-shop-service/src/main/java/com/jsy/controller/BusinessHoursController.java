package com.jsy.controller;

import com.jsy.basic.util.vo.CommonResult;
import com.jsy.domain.ShopOwner;
import com.jsy.dto.SelectShopBusinessTimeDto;
import com.jsy.mapper.BusinessHoursMapper;
import com.jsy.parameter.UpdateShopBusinessTimeParam;
import com.jsy.service.BusinessHoursService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/BusinessHours")
@Api(tags = "店铺营业时间")
public class BusinessHoursController {

    @Autowired
    private BusinessHoursService businessHoursService;

    @ApiOperation("添加默认的营业时间")
    @RequestMapping(value = "/defaultBusinessTime",method = RequestMethod.POST)
    public CommonResult<Boolean> defaultBusinessTime(String shopUuid){
        Boolean aBoolean = businessHoursService.defaultBusinessTime(shopUuid);
        return  new CommonResult<Boolean>(200,"添加默认时间成功",aBoolean);
    }

    @ApiOperation("查询店铺的营业时间")
    @RequestMapping(value = "/selectShopBusinessTime",method = RequestMethod.GET)
    public CommonResult<List<SelectShopBusinessTimeDto>> selectShopBusinessTime(String shopUuid) {
        List<SelectShopBusinessTimeDto> selectShopBusinessTimeDtos = businessHoursService.selectShopBusinessTime(shopUuid);
        return  new CommonResult<List<SelectShopBusinessTimeDto>>(200,"查询店铺的营业时间成功",selectShopBusinessTimeDtos);

    }
    @ApiOperation("修改店铺营业时间")
    @RequestMapping(value = "/updateShopBusinessTime",method = RequestMethod.POST)
    public CommonResult<Boolean> updateShopBusinessTime(@RequestBody List<UpdateShopBusinessTimeParam> list){
        Boolean aBoolean = businessHoursService.updateShopBusinessTime(list);
        return  new CommonResult<Boolean>(200,"修改SESSION",aBoolean);
    }






















}
