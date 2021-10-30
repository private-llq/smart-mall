package com.jsy.controller;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.domain.UserRedpacket;
import com.jsy.dto.SelectUserAllRedpacketDto;
import com.jsy.dto.SelectUserNoUserRedPacketDto;
import com.jsy.dto.UserRedpacketDTO;
import com.jsy.query.UserRedpacketQuery;
import com.jsy.service.IUserRedpacketService;
import com.jsy.vo.ReceiveRedpacketVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/userRedpacket")
@Api(tags = "用户红包模块")
public class UserRedpacketController {
    @Autowired
    public IUserRedpacketService userRedpacketService;


    @ApiOperation(value = "新增抢到的红包", httpMethod = "POST")
    @RequestMapping(value="/insterRedPacket",method= RequestMethod.POST)
    public CommonResult<Boolean> insterRedPacket(@RequestBody UserRedpacket userRedpacket) {
        boolean b = userRedpacketService.insterRedPacket(userRedpacket);
       return new CommonResult<Boolean>(200,"新增成功",b);
    }

    @ApiOperation(value = "查询用户在店铺中没有使用的红包", httpMethod = "GET")
    @RequestMapping(value="/selectUserNoUserRedPacket",method= RequestMethod.GET)
    public CommonResult<List<SelectUserNoUserRedPacketDto>> selectUserNoUserRedPacket(String shopUuid) {
        List<SelectUserNoUserRedPacketDto> dtoList = userRedpacketService.selectUserNoUserRedPacket(shopUuid);
        return new CommonResult<>(200,"查询成功",dtoList);
    }

    @ApiOperation(value = "根据用户和活动uuid查询最近一次领取的红包", httpMethod = "GET")
    @RequestMapping(value="/selectUserRedpacket/{userActiveUuid}",method= RequestMethod.GET)
    public CommonResult<UserRedpacket> selectUserRedpacket(@PathVariable("userActiveUuid") String userActiveUuid){
            UserRedpacket dto = userRedpacketService.selectUserRedpacket(userActiveUuid);
        return new CommonResult<>(200,"查询成功",dto);
    }

    @ApiOperation(value = "查询所有用户的红包", httpMethod = "GET")
    @RequestMapping(value="/selectUserAllRedpacket",method= RequestMethod.GET)
    public CommonResult<List<SelectUserAllRedpacketDto>> selectUserAllRedpacket(){
        List<SelectUserAllRedpacketDto> userRedpackets = userRedpacketService.selectUserAllRedpacket();
        return new CommonResult<>(0,"查询成功",userRedpackets);
    }



    @ApiOperation(value = "根据用户和活动uuid查询已领红包数据集合", httpMethod = "GET")
    @RequestMapping(value="/selectUserRedpacketAll/{activity}",method= RequestMethod.GET)
    public CommonResult<List<UserRedpacket>> selectUserRedpacketAll(@PathVariable("activity")String activity){
        List<UserRedpacket> userRedpackets = userRedpacketService.selectUserRedpacketAll(activity);
        return new CommonResult<>(200,"查询成功",userRedpackets);
    }



    /*******************************************************************************/

    /**
    * 领取红包
    * @param receiveRedpacketVo  传递的实体
    * @return
    */
    @ApiOperation(value = "1.01 用户领取红包模块", httpMethod = "POST")
    @RequestMapping(value="/save",method= RequestMethod.POST)
    public CommonResult save(@RequestBody ReceiveRedpacketVo receiveRedpacketVo){
        userRedpacketService.receiveRedpacket(receiveRedpacketVo);
        return CommonResult.ok();
    }

    //获取
    @RequestMapping(value = "/{uuid}",method = RequestMethod.GET)
    @ApiOperation(value = "1.02 通过红包id获取用户红包信息", httpMethod = "GET")
    public CommonResult<UserRedpacket> get(@PathVariable("uuid")String uuid){
        return CommonResult.ok(userRedpacketService.getByUuid(uuid));
    }

    //获取
    @RequestMapping(value = "/use/{uuid}",method = RequestMethod.GET)
    @ApiOperation(value = "1.02 用户使用红包", httpMethod = "GET")
    public CommonResult useRedpacket(@PathVariable("uuid")String uuid) {
        userRedpacketService.useRedpacket(uuid);
        return CommonResult.ok();
    }

    /**
    * 分页查询数据
    *
    * @param query 查询对象
    * @return PageList 分页对象
    */
    @RequestMapping(value = "/pagelist",method = RequestMethod.POST)
    @ApiOperation(value = "1.03 分页查询该用户所有红包", httpMethod = "POST")
    public CommonResult json(@RequestBody UserRedpacketQuery query){
        return CommonResult.ok(userRedpacketService.queryByPage(query));
    }

    @GetMapping("/queryByShop/{shopUuid}")
    @ApiOperation(value = "1.04 查看该用户在该店铺可使用的红包", httpMethod = "GET")
    public CommonResult queryByShop(@PathVariable String shopUuid){
        return CommonResult.ok(userRedpacketService.queryByShop(shopUuid));
    }
}
