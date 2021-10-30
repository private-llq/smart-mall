package com.jsy.client;


import com.jsy.FeignConfiguration;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.domain.CommonRedpacket;
import com.jsy.domain.NewUser;
import com.jsy.domain.ShopInfo;
import com.jsy.domain.ShopRedpacket;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(value = "SERVICE-SHOP",configuration = FeignConfiguration.class)
public interface ShopClient {

    @RequestMapping(value = "/shopRedpacket/{uuid}",method = RequestMethod.GET)
    CommonResult get(@PathVariable("uuid")String uuid);

    @RequestMapping(value = "/shopRedpacket/grant/{uuid}",method = RequestMethod.GET)
    CommonResult grant(@PathVariable("uuid")String uuid);



    //获取
    @RequestMapping(value = "/commonRedpacket/{uuid}",method = RequestMethod.GET)
    CommonResult getCommonRedpacket(@PathVariable("uuid")String uuid);

    @RequestMapping(value = "/commonRedpacket/grant/{uuid}",method = RequestMethod.GET)
    CommonResult grantCommonRedpacket(@PathVariable("uuid")String uuid);

    //获取
    @PostMapping("/commonRedpacket/getMapByUuid")
    @ApiOperation(value = "获取红包信息通过uuids", httpMethod = "POST")
    Map<String, CommonRedpacket> getMapByUuid(@RequestBody List<String> uuids);

    @PostMapping("/shopRedpacket/getMapByUuid")
    @ApiOperation(value = "获取红包信息通过uuids", httpMethod = "POST")
    Map<String, ShopRedpacket> getShopMapByUuid(@RequestBody List<String> uuids);

    @PostMapping("/shopInfo/getMapByUuid")
    @ApiOperation(value = "获取店铺信息通过uuids", httpMethod = "POST")
     Map<String, ShopInfo> getShopInfoMapByUuid(@RequestBody List<String> uuids);

    @ApiModelProperty("修改店铺星级")
    @GetMapping("/shopInfo/pub/updateShopStar/{shopUuid}/{star}")
    CommonResult<Boolean> updateShopStar(@PathVariable("shopUuid") String shopUuid,@PathVariable("star") double star);

    /**
     * 判断该用户是否是首单（新客）
     */
    @GetMapping("/newUser/pub/isNewUser")
    CommonResult<String> isNewUser(@RequestParam("shopUuid") String shopUuid, @RequestParam("userUuid") String userUuid);

    /**
     * 进行中的新客立减活动
     * @param
     */
    @GetMapping("/newUser/pub/getNewUser")
    CommonResult<NewUser> getNewUser(@RequestParam("shopUuid") String shopUuid);

    /**
     *最近一次的新客立减活动  1 : 进行中 2 已撤销 3 已过期 4未开始
     * @param
     */
    @ApiOperation("最近一次的新客立减活动 ")
    @GetMapping("/newUser/pub/newestNewUser")
    CommonResult<NewUser> newestNewUser(@RequestParam("shopUuid") String shopUuid);

}
