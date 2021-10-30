package com.jsy.client;
import com.jsy.FeignConfiguration;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.domain.ShopApplySeckill;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@FeignClient(value = "SERVICE-SHOP",configuration = FeignConfiguration.class)
public interface ShopAppSeckillClient {

    //获取
    @ApiOperation("根据id获取审核通过秒杀活动信息")
    @RequestMapping(value = "/shopApplySeckill/pub/selectKillInfo/{shopUuid}/{goodsUuid}",method = RequestMethod.POST)
    CommonResult<ShopApplySeckill> selectKillInfo(@PathVariable("shopUuid") String shopUuid, @PathVariable("goodsUuid") String goodsUuid);


    //修改库存  库存-购买数量
    @ApiOperation("根据id获取秒杀活动信息")
    @RequestMapping(value = "/shopApplySeckill/pub/updateStock/{shopUuid}/{goodsUuid}/{num}",method = RequestMethod.POST)
    CommonResult updateStock(@PathVariable("shopUuid") String shopUuid,
                                               @PathVariable("goodsUuid") String goodsUuid,
                                               @PathVariable("num") Integer num);


}
