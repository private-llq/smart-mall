package com.jsy.controller;

import com.jsy.basic.util.vo.CommonResult;
import com.jsy.domain.ShopRedpacket;
import com.jsy.dto.SelectShopRedpacketByUserDto;
import com.jsy.dto.SelectShopRedpacketDto;
import com.jsy.dto.UserGetRedPacketDto;
import com.jsy.parameter.ShopRedPacketParam;
import com.jsy.query.ShopRedpacketQuery;
import com.jsy.service.IShopRedpacketService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/shopRedpacket")
@Api(tags = "店铺红包模块")
public class ShopRedpacketController {
    @Autowired
    public IShopRedpacketService shopRedpacketService;

    @RequestMapping(value="/creationRedPack",method= RequestMethod.POST)
    @ApiOperation(value = "创建店铺红包", httpMethod = "POST")
    public CommonResult<Boolean> creationRedPack(@RequestBody ShopRedPacketParam shopRedPacketParam){
        boolean b = shopRedpacketService.creationRedPack(shopRedPacketParam);
        if(b){
            return new CommonResult<Boolean>(200,"创建成功",b);
        }
        return new CommonResult<Boolean>(-1,"失败",b);
    }


    @RequestMapping(value="/selectShopRedpacket",method= RequestMethod.GET)
    @ApiOperation(value = "查询店铺红包信息", httpMethod = "GET")
    public CommonResult<SelectShopRedpacketDto> selectShopRedpacket(String shopUuid) {
        SelectShopRedpacketDto shopRedpacketDto = shopRedpacketService.selectShopRedpacket(shopUuid);
        return new CommonResult<SelectShopRedpacketDto>(200,"查询成功",shopRedpacketDto);
    }


    @RequestMapping(value="/SelectShopRedpacketByUser",method= RequestMethod.GET)
    @ApiOperation(value = "{用户}查看店铺红包信息", httpMethod = "GET")
    public CommonResult<SelectShopRedpacketByUserDto> SelectShopRedpacketByUser(String shopUuid) {
        SelectShopRedpacketByUserDto Dto = shopRedpacketService.SelectShopRedpacketByUser(shopUuid);
        return new CommonResult<SelectShopRedpacketByUserDto>(200,"查询成功",Dto);
    }

    @RequestMapping(value="/UserGetRedPacket",method= RequestMethod.GET)
    @ApiOperation(value = "{用户}领取红包", httpMethod = "GET")
    public CommonResult<UserGetRedPacketDto> UserGetRedPacket(String redPacketUuid) {
        UserGetRedPacketDto dto = shopRedpacketService.UserGetRedPacket(redPacketUuid);
        return new CommonResult<>(200,"领取成功",dto);

    }







    /***************************************/
    /**
    * 新增及修改店铺红包
    * @param shopRedpacket  传递的实体
    * @return Ajaxresult转换结果
    */
    @RequestMapping(value="/save",method= RequestMethod.POST)
    @ApiOperation(value = "1.01 新增商铺红包", httpMethod = "POST")
    public CommonResult save(@RequestBody ShopRedpacket shopRedpacket){
        shopRedpacketService.addAndUpdate(shopRedpacket);
        return CommonResult.ok();
    }

    //修改红包状态
    @RequestMapping(value = "/{uuid}/{status}",method = RequestMethod.GET)
    @ApiOperation(value = "1.02 修改红包状态（红包发布及失效功能）", httpMethod = "GET")
    public CommonResult get(@PathVariable("uuid")String uuid,@PathVariable("status")Integer status)
    {
        shopRedpacketService.changStatus(uuid,status);
        return CommonResult.ok();
    }

    //获取
    @RequestMapping(value = "/{uuid}",method = RequestMethod.GET)
    @ApiOperation(value = "1.03 通过id获取店铺红包信息", httpMethod = "POST")
    public CommonResult get(@PathVariable("uuid")String uuid)
    {
        return CommonResult.ok(shopRedpacketService.getByUuid(uuid));
    }

    //领取红包
    @RequestMapping(value = "/grant/{uuid}",method = RequestMethod.GET)
    @ApiOperation(value = "1.04 用户领取红包时店铺红包判断是否可领取及删减红包数量", httpMethod = "GET")
    public CommonResult grant(@PathVariable("uuid")String uuid)
    {
        return CommonResult.ok(shopRedpacketService.grant(uuid));
    }
    /**
    * 分页查询数据
    *
    * @param query 查询对象
    * @return PageList 分页对象
    */
    @RequestMapping(value = "/pagelist",method = RequestMethod.POST)
    @ApiOperation(value = "1.05 分页查询本店店铺红包可以以类型查询", httpMethod = "POST")
    public CommonResult page(@RequestBody ShopRedpacketQuery query)
    {
        return CommonResult.ok(shopRedpacketService.queryByPage(query));
    }

    //获取
    @PostMapping("/getMapByUuid")
    @ApiOperation(value = "获取红包信息通过uuids", httpMethod = "POST")
    public Map<String,ShopRedpacket> getShopMapByUuid(@RequestBody List<String> uuids)
    {
        return shopRedpacketService.getMapByUuid(uuids);
    }
}
