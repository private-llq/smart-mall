package com.jsy.client;

import com.jsy.FeignConfiguration;
import com.jsy.basic.util.PageList;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.client.impl.OrderClientImpl;
import com.jsy.domain.Order;
import com.jsy.query.OrderQuery;
import com.jsy.vo.OrderVo;
import com.jsy.vo.ShopEvaluationVo;
import com.jsy.vo.ShopFinsh;
import com.jsy.vo.ShopRecordVo;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "SERVICE-ORDER",fallback = OrderClientImpl.class,configuration = FeignConfiguration.class)
public interface OrderClient {

    @ApiOperation(value = "下订单订单接口",httpMethod = "POST",response = CommonResult.class,notes = "根据")
    @ApiParam(required = true,value = "传入订单对象")
    @RequestMapping(value="/pub/order/saveOrder",method= RequestMethod.POST)
    public CommonResult<Boolean> saveOrder(@RequestBody OrderVo ordervo);

    /**
     * 用户确定消费了订单
     * @param id
     * @return CommonResult
     */
    @ApiOperation(value = "用户消费完成订单接口",httpMethod = "GET",response = CommonResult.class)
    @ApiParam(required = true,value = "传入订单id")
    @RequestMapping(value = "/pub/order/finishOrder/{id}",method = RequestMethod.GET)
    public CommonResult<Boolean> finishOrder(@PathVariable("id") Long id);

    @ApiOperation(value = "用户评价接口",httpMethod = "POST",response = CommonResult.class)
    @ApiParam(required = true,value = "传入ShopEvaluationVo")
    @RequestMapping(value="/userEvaluation",method= RequestMethod.POST)
    public CommonResult<Boolean> userEvaluation(@RequestBody ShopEvaluationVo shopEvaluationVo);

    /**
     * 删除订单信息
     * @param uuid
     * @return CommonResult
     */
    @ApiOperation(value = "订单删除接口",httpMethod = "DELETE",response = CommonResult.class)
    @ApiParam(required = true,value = "根据订单uuid删除")
    @RequestMapping(value="/deleteOrder/{uuid}",method=RequestMethod.DELETE)
    public CommonResult<Boolean> deleteOrder(@PathVariable("uuid") String uuid);

    /**
     * 根据id将订单改为待删除状态
     * @param uuid
     * @return
     */
    @ApiOperation(value = "根据uuid将订单改为待删除状态",httpMethod = "GET",response = CommonResult.class)
    @ApiParam(required = true,value = "根据uuid将订单改为待删除状态")
    @RequestMapping(value = "/changeStatus/{uuid}",method=RequestMethod.GET)
    public CommonResult changeStatus(@PathVariable("uuid")String uuid);


    /**
     * 删除所有数据库待删除订单,并删除该订单商品
     * @return
     */
    @ApiOperation(value = "删除所有数据库待删除订单,并删除该订单商品",response = CommonResult.class)
    @ApiParam(required = true,value = "删除数据库待删除订单")
    @RequestMapping(value = "/deleteByStatus",method = RequestMethod.DELETE)
    public CommonResult deleteByStatus();

    /**
     * 查询某个订单
     * @param id
     * @return
     */
    @ApiOperation(value = "查询某个订单",httpMethod = "GET")
    @ApiParam(required = true,value = "根据订单id查询")
    @RequestMapping(value = "/getOrder/{id}",method = RequestMethod.GET)
    public CommonResult getOrder(@PathVariable("id")Long id);


    @ApiOperation(value = "根据条件查询的分页接口",httpMethod = "POST",response = CommonResult.class)
    @ApiParam(name = "OrderQuery",value = "传入一个OrdeQruery对象，可进行条件查询")
    @RequestMapping(value = "/order/pageOrder",method = RequestMethod.POST)
    public CommonResult<PageList<Order>> pageOrder(@RequestBody OrderQuery query);



    /**
     * 根据订单uuid查询订单的商品
     * @param uuid
     * @return
     */
    @ApiOperation(value = "根据订单uuid查询订单的商品",httpMethod = "PUT",response = CommonResult.class)
    @ApiParam(name = "id",value = "传入订单uuid查询所属商品")
    @RequestMapping(value = "/GoodsOrder/{uuid}",method = RequestMethod.PUT)
    public CommonResult goodsOrder(@PathVariable("uuid") String uuid);

    /**
     * 根据用户id查询用户未评价订单
     * @param uuid
     * @return
     */
    @ApiOperation(value = "根据用户uuid查询用户未评价订单",httpMethod = "GET",response = CommonResult.class)
    @ApiParam(name = "id",value = "根据用户uuid查询用户未评价订单")
    @RequestMapping(value = "/waitOrder/{uuid}",method = RequestMethod.GET)
    public CommonResult waitOrder(@PathVariable("uuid") String uuid);

    /**
     * 根据用户id查询用户退款中订单
     * @param uuid
     * @return
     */
    @ApiOperation(value = "根据用户uuid查询用户退款中订单",httpMethod = "GET",response = CommonResult.class)
    @ApiParam(name = "id",value = "根据用户uuid查询用户退款中订单")
    @RequestMapping(value = "/backOrder/{uuid}",method = RequestMethod.GET)
    public CommonResult backOrder(@PathVariable("uuid")String uuid);

    /**
     * 根据用户id查询历史完成的订单
     * @param uuid
     * @return
     */
    @ApiOperation(value = "根据用户uuid查询历史完成的订单",httpMethod = "GET",response = CommonResult.class)
    @ApiParam(name = "uuid",value = "根据用户uuid查询用户历史完成订单")
    @RequestMapping(value = "/historyOrder/{uuid}",method = RequestMethod.GET)
    public CommonResult historyOrder(@PathVariable("uuid")String uuid);


    /**
     * 根据店家id查询订单详细信息，包括商品详细信息dto
     * @param uuid
     * @return
     */
    @ApiOperation(value = "根据店家uuid查询订单详细信息，包括商品详细信息",httpMethod = "GET",response = CommonResult.class)
    @ApiParam(name = "店铺id",value = "根据店家uuid查询订单详细信息")
    @RequestMapping(value = "/pageBill/{uuid}",method = RequestMethod.GET)
    public CommonResult pageBill(@PathVariable("uuid") String uuid);

    /**
     * 根据时间算已支付的订单总金额
     * @param query
     * @return
     */
    @ApiOperation(value = "获取区间内总金额",httpMethod = "POST",response = CommonResult.class)
    @ApiParam(name = "OrderQuery",value = "获取区间内总金额")
    @RequestMapping(value = "/money",method = RequestMethod.POST)
    public CommonResult mouthBill(@RequestBody OrderQuery query);

    /**
     * 测试通过
     * 店家定位到订单用户位置附近50米可以点击订单完成成功
     * @param shopFinsh
     * @return
     */
    @ApiOperation(value = "店家确定订单完成",httpMethod = "POST",response = CommonResult.class)
    @ApiParam(name = "shopFinsh",value = "店家确定订单完成")
    @RequestMapping(value = "/shopFinish",method = RequestMethod.POST)
    public CommonResult shopFinish(@RequestBody ShopFinsh shopFinsh);

    /**
     * 根据uuid获取用户的所有订单
     * @return
     */
    @ApiOperation(value = "根据用户uuid获取订单",httpMethod = "GET",response = CommonResult.class)
    @ApiParam(name = "uuid",value = "根据用户uuid获取订单")
    @RequestMapping(value = "/getOrderByUuid/{uuid}",method = RequestMethod.GET)
    public CommonResult getOrderByUuid(@PathVariable("uuid") String uuid);

    /**
     *
     * @param uuid
     * @return
     */
    @ApiOperation(value = "根据uuid完成订单",httpMethod = "GET",response = CommonResult.class)
    @ApiParam(name = "uuid",value = "根据uuid完成订单")
    @RequestMapping(value = "finishOrderByUuid/{uuid}",method = RequestMethod.GET)
    public CommonResult finishOrderByUuid(@PathVariable("uuid")String uuid);

    @ApiOperation(value = "根据uuid更改为待删除订单",httpMethod = "GET",response = CommonResult.class)
    @ApiParam(name ="uuid id",value = "根据uuid更改为待删除订单")
    @RequestMapping(value = "/deleteOrderByUuid/{uuid}",method = RequestMethod.GET)
    public CommonResult deleteOrderByUuid(@PathVariable("uuid")String uuid);


    @ApiOperation(value = "根据用户id查询用户退款中订单",httpMethod = "GET",response = CommonResult.class)
    @ApiParam(name = "id",value = "根据用户id查询用户退款中订单")
    @RequestMapping(value = "/backOrderByUuid/{uuid}",method = RequestMethod.GET)
    public CommonResult backOrderByUuid(@PathVariable("uuid") String uuid);

    /**
     *  "money": 10,
     *  "count": 1,
     *  "shopUuid": "1eef1c6c6cef46ffa76e3b1141cd02df"
     * @param query
     * @return
     */
    @ApiOperation(value = "在订单中查询店铺uuid和在店铺使用平台红包的数量金额shopUuid,count，money",httpMethod = "POST",response = CommonResult.class)
    @ApiParam(name = "",value = "在订单中查询店铺uuid和在店铺使用平台红包的数量shopUuid,count，money")
    @RequestMapping(value = "/getRedpacket",method = RequestMethod.POST)
    public CommonResult getRedpacket(@RequestBody OrderQuery query);

    /**
     * 生成店铺订单记录流水
     * @param shopRecordVo
     * @return
     */
    @ApiOperation(value = "生成店铺订单记录流水",httpMethod = "POST",response = CommonResult.class)
    @ApiParam(name = "ShopRecordVo",value = "生成店铺订单记录流水")
    @RequestMapping(value = "/shopRecord",method = RequestMethod.POST)
    public CommonResult shopRecord(ShopRecordVo shopRecordVo);
}
