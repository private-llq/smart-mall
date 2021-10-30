package com.jsy.client;

import com.jsy.FeignConfiguration;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.domain.UserRedpacket;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(value = "SERVICE-USER",configuration = FeignConfiguration.class)
public interface UserRedpackClient {

    //获取
    @RequestMapping(value = "/userRedpacket/{uuid}",method = RequestMethod.GET)
    @ApiOperation(value = "1.02 通过红包id获取用户红包信息", httpMethod = "POST")
    CommonResult<UserRedpacket> get(@PathVariable("uuid")String uuid);

    @ApiOperation(value = "1.02 用户使用红包", httpMethod = "GET")
    @RequestMapping(value = "/userRedpacket/use/{uuid}",method = RequestMethod.GET)
    CommonResult useRedpacket(@PathVariable("uuid")String uuid);

    @ApiOperation(value = "新增抢到的红包", httpMethod = "POST")
    @RequestMapping(value="/userRedpacket/insterRedPacket",method= RequestMethod.POST)
    public CommonResult<Boolean> insterRedPacket(@RequestBody UserRedpacket userRedpacket);

    @ApiOperation(value = "根据用户和活动uuid查询最近一次领取的红包", httpMethod = "GET")
    @RequestMapping(value="/userRedpacket/selectUserRedpacket/{userActiveUuid}",method= RequestMethod.GET)
    public CommonResult<UserRedpacket> selectUserRedpacket(@PathVariable("userActiveUuid") String userActiveUuid);

    @ApiOperation(value = "根据用户和活动uuid查询已领红包数据集合", httpMethod = "GET")
    @RequestMapping(value="/userRedpacket/selectUserRedpacketAll/{activity}",method= RequestMethod.GET)
    public CommonResult<List<UserRedpacket>> selectUserRedpacketAll(@PathVariable("activity")String activity);
}
