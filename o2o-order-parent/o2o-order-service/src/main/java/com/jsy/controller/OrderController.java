package com.jsy.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsy.annotation.ControllerAnnotation;
import com.jsy.basic.util.LocalTimeUtil;
import com.jsy.basic.util.PageList;
import com.jsy.basic.util.RedisStateCache;
import com.jsy.basic.util.constant.Global;
import com.jsy.basic.util.exception.JSYError;
import com.jsy.basic.util.exception.JSYException;
import com.jsy.basic.util.utils.CommonExcelHelper;
import com.jsy.basic.util.utils.CurrentUserHolder;
import com.jsy.basic.util.utils.GouldUtil;
import com.jsy.basic.util.utils.UUIDUtils;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.basic.util.vo.UserDto;
import com.jsy.basic.util.vo.UserEntity;
import com.jsy.canstant.OrderStateChange;
import com.jsy.canstant.OrderStateEnum;
import com.jsy.client.CartClient;
import com.jsy.client.ShopInfoClient;
import com.jsy.client.UserRedpackClient;
import com.jsy.domain.Order;
import com.jsy.domain.ShopAssets;
import com.jsy.dto.*;
import com.jsy.query.OrderQuery;
import com.jsy.service.IOrderService;
import com.jsy.service.IShopAssetsService;
import com.jsy.service.IShopEvaluationService;
//import com.jsy.service.IUnionPayOrderRecordService;
import com.jsy.utils.TranslateMap;
import com.jsy.utils.vo.OrderForExecl;
import com.jsy.vo.OrderVo;
import com.jsy.vo.ShopEvaluationVo;
import com.jsy.vo.ShopFinsh;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 错误码code
 * @author yu
 */
@RestController
@RequestMapping("/order")
@Api(tags ="订单服务")
public class OrderController {

    @Autowired
    private IOrderService orderService;
    @Autowired
    private IShopEvaluationService shopEvaluationService;
    @Autowired
    private GouldUtil gouldUtil;
    @Autowired
    private CartClient cartClient;
    @Autowired
    private IShopAssetsService shopAssetsService;
    @Autowired
    private UserRedpackClient userRedpackClient;
    @Autowired
    private RedisStateCache redisStateCache;
    @Autowired
    private ShopInfoClient shopInfoClient;

    //@Autowired
    //public IUnionPayOrderRecordService unionPayOrderRecordService;

    public static final Logger log = LoggerFactory.getLogger(OrderController.class);

    /**
     * 保存和修改公用的
     * @param ordervo  传递的实体
     * @return CommonResult  转换结果
     */
    @ApiOperation(value = "下订单订单接口",httpMethod = "POST",response = CommonResult.class,notes = "根据")
    @ApiParam(required = true,value = "传入订单对象")
    @RequestMapping(value="/saveOrder",method= RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public CommonResult saveOrder(@RequestBody OrderVo ordervo){
        //TODO 多个订单提交
        List<Order> list = new ArrayList<>();

        Order order = orderService.saveOrder(ordervo);
            cartClient.clearCart(ordervo.getShopUuid());
            if (StringUtils.isNotBlank(ordervo.getUserRedpacket())){
                userRedpackClient.useRedpacket(ordervo.getUserRedpacket());
            }
        return CommonResult.ok(order);
    }

    @ApiOperation(value = "接订单")
    @RequestMapping(value = "/acceptOrder",method = RequestMethod.GET)
    public CommonResult acceptOrder(@RequestParam("uuid") String uuid){
        UserEntity userEntity = CurrentUserHolder.getUserEntity();
        int i = orderService.acceptOrder(uuid,userEntity.getUid());
        if (i==Global.INT_0){
            return CommonResult.error(-1,"接单失败");
        }
        return CommonResult.ok();
    }


    @ApiOperation(value = "校验订单")
    @RequestMapping(value = "/checkOrder",method = RequestMethod.POST)
    public CommonResult checkOrder(@RequestBody OrderDto orderDto){
        OrderDto orderDtoOrder = orderService.getByUuid(orderDto.getUuid());
        if (Objects.isNull(orderDtoOrder)){
            return CommonResult.error(-1,"没有此订单");
        }
        orderDto.setCreateTime(null);
        orderDto.setServiceTime(null);
        orderDto.setUpdateTime(null);
        orderDtoOrder.setCreateTime(null);
        orderDtoOrder.setServiceTime(null);
        orderDtoOrder.setUpdateTime(null);
        if (TranslateMap.checkOrder(orderDtoOrder, orderDto)){
            return CommonResult.ok();
        }
        return CommonResult.error(-1,"订单有误");
    }


    @ControllerAnnotation(description = "该订单已支付接口payedOrder")
    @ApiOperation(value = "该订单已支付接口")
    @RequestMapping(value = "/pub/payedOrder/{uuid}",method = RequestMethod.GET)
    public CommonResult payedOrder(@PathVariable("uuid")String uuid){
        //TODO:订单支付成功需要调用社区的支付模块并更改订单状态，并且删除在redis中保存的未支付订单，利用队列同步到数据库
        int i = orderService.payedOrder(uuid);
        if (i==Global.INT_0){
            return CommonResult.error(-1,"请输入正确的订单");
        }
        OrderDto orderDto = orderService.getByUuid(uuid);
        CommonResult<ShopMessageDto> commonResult= shopInfoClient.selectShopMessage(orderDto.getShopUuid());
        ShopMessageDto shopMessageDto = commonResult.getData();

        //TODO:订单支付成功就生成码
        if (Global.Str1.equals(shopMessageDto.getShopSent())){
            String code = UUIDUtils.getServiceCode(orderDto.getOrderNum());
            orderDto.setServiceCode(code);
            orderService.generateCode(orderDto);
            return CommonResult.ok(orderDto.getServiceCode());
        }
        return CommonResult.ok();
    }

    @ApiOperation(value = "根据订单uuid获取服务码")
    @GetMapping("/getServiceCode")
    public CommonResult getServiceCode(@RequestParam("uuid") String uuid){
        OrderDto orderDto = orderService.getByUuid(uuid);
        if (Objects.isNull(orderDto)){
            return CommonResult.error(-1,"获取服务码错误");
        }
        return CommonResult.ok(orderDto);
    }

    @ApiOperation(value = "用户消费服务码")
    @GetMapping("/toUsed")
    public CommonResult toUsed(@RequestParam("serviceCode") String serviceCode,@RequestParam("uuid") String uuid){
        //检查消费码是否被消费
        OrderDto orderDto = orderService.getByUuid(uuid);
        if (Global.Str1.equals(orderDto.getUsed())){
            return CommonResult.error(-1,"消费码已经消费");
        }
        int i = orderService.changeUsed(serviceCode, uuid);
        if (i==Global.INT_0){
            return CommonResult.error(-1,"消费失败");
        }
        return CommonResult.ok();
    }

    @Transactional
    @ApiOperation(value = "用户申请退款 通过订单uuid")
    @RequestMapping(value = "/cancelOrder/{uuid}",method = RequestMethod.GET)
    public CommonResult cancelOrder(@PathVariable("uuid") String uuid){
        //用户申请退款 ,在订单未完成的时候才能申请退款
        orderService.cancelOrder(uuid);
        //TODO：店铺资质,金额
        ShopAssets shopAssets = shopAssetsService.getByOid(uuid);
        shopAssetsService.backByOuid(uuid);
        //TODO：店铺金额流水
        //shopRecordService.deleteByOuid(uuid);

        return CommonResult.ok();
    }

    @ControllerAnnotation(description = "根据订单uuid设置正在配送订单setRunOrder")
    @ApiOperation(value = "根据订单uuid设置正在配送订单")
    @GetMapping("/setRunOrder/{uuid}")
    public CommonResult setRunOrder(@PathVariable("uuid")String uuid){
        int i = orderService.setRunOrder(uuid);
        if (i == Global.INT_0) {
            return CommonResult.error(-1,"设置配送失败");
        }
        return CommonResult.ok();
    }

    @ApiOperation(value = "根据当前用户获取正在配送的订单")
    @GetMapping("/getRunOrder")
    public CommonResult getRunOrder(){
        UserEntity userEntity = CurrentUserHolder.getUserEntity();
        List<OrderInfoDto> list = orderService.getRunOrder(userEntity.getUid());
        return CommonResult.ok(list);
    }

    @ApiOperation(value = "用户消费完成订单接口",httpMethod = "GET",response = CommonResult.class)
    @ApiParam(required = true,value = "传入订单id")
    @RequestMapping(value = "/finishOrder/{uuid}",method = RequestMethod.GET)
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<Boolean> finishOrder(@PathVariable("uuid") String uuid){
        try {
            orderService.finishOrderByUuid(uuid);
        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getMessage());
            return CommonResult.error(-1,"订单提交错误");
        }
        return CommonResult.ok();
    }

    @ApiOperation(value = "用户评价接口",httpMethod = "POST",response = CommonResult.class)
    @ApiParam(required = true,value = "传入ShopEvaluationVo")
    @RequestMapping(value="/userEvaluation",method= RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public CommonResult userEvaluation(@RequestBody ShopEvaluationVo shopEvaluationVo){
        UserEntity userEntity = CurrentUserHolder.getUserEntity();
        OrderDto orderDto = orderService.getByUuid(shopEvaluationVo.getOrderUuid());
        if (orderDto.getEvaluationId()==Global.INT_1){
            return CommonResult.error(JSYError.BAD_REQUEST.getCode(),"您已经评价过了");
        }
        try {
            shopEvaluationVo.setName(userEntity.getNickname());
            //修改订单评价状态
            orderService.updateEid(shopEvaluationVo.getOrderUuid());
            shopEvaluationService.evaluationOrder(shopEvaluationVo);
        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getMessage());
            return CommonResult.error(-1,"用户评价失败");
        }
        return CommonResult.ok();
    }


    /**
    * 删除订单信息---直接删除
    * @param uuid
    * @return CommonResult
    */
    @ApiOperation(value = "订单删除接口",httpMethod = "DELETE",response = CommonResult.class)
    @ApiParam(required = true,value = "根据订单uuid删除")
    @RequestMapping(value="/deleteOrder/{uuid}",method=RequestMethod.DELETE)
    @Transactional(rollbackFor = Exception.class)
    public CommonResult deleteOrder(@PathVariable("uuid") String uuid){
        UserEntity userEntity = CurrentUserHolder.getUserEntity();
        /* 要删除的order */
        OrderDto orderDto = orderService.getByUuid(uuid);
        try {
            orderService.deleteById(uuid);
            return CommonResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getMessage());
            throw new JSYException(JSYError.INTERNAL.getCode(),"删除失败，请稍后再试");
        }
    }

    @ApiOperation(value = "店铺同意完成退款订单")
    @RequestMapping(value = "/finishCancelOrder/{uuid}",method = RequestMethod.GET)
    public CommonResult finishCancelOrder(@PathVariable("uuid") String uuid){
        orderService.finishCancelOrder(uuid);
        return CommonResult.ok();
    }

    @ApiOperation(value = "店铺不同意完成退款订单")
    @RequestMapping(value = "/noCancelOrder/{uuid}",method = RequestMethod.GET)
    public CommonResult noCancelOrder(@PathVariable("uuid") String uuid){
        int i = orderService.noCancelOrder(uuid);
        if (i==Global.INT_0){
            return CommonResult.error(-1,"操作失败");
        }
        return CommonResult.ok();
    }

    @ApiOperation(value = "根据uuid将订单改为待删除状态",httpMethod = "GET",response = CommonResult.class)
    @ApiParam(value = "根据uuid将订单改为待删除状态")
    @RequestMapping(value = "/changeStatus/{uuid}",method=RequestMethod.GET)
    public CommonResult changeStatus(@PathVariable("uuid")String uuid){
        try {
            orderService.changeStatus(uuid);
        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getMessage());
            throw new JSYException(JSYError.INTERNAL.getCode(),"删除失败，请稍后再试");
        }
        return CommonResult.ok();
    }

    @ApiOperation(value = "删除所有数据库待删除订单,并删除该订单商品",response = CommonResult.class)
    @ApiParam(required = true,value = "删除数据库待删除订单")
    @RequestMapping(value = "/deleteByStatus",method = RequestMethod.DELETE)
    public CommonResult deleteByStatus(){
        try {
            orderService.deleteByStatus();
        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getMessage());
            throw new JSYException(JSYError.INTERNAL.getCode(),"请联系管理员");
        }
        return CommonResult.ok();
    }


    @ApiOperation(value = "查询某个订单",httpMethod = "GET",response = Order.class)
    @ApiParam(required = true,value = "根据订单号查询")
    @RequestMapping(value = "/getOrderByNum/{orderNum}",method = RequestMethod.GET)
    public CommonResult getOrderByNum(@PathVariable("orderNum")String orderNum) {
        OrderDto orderDto = orderService.selectByOrderNum(orderNum);
        return CommonResult.ok(orderDto);
    }

    @ApiOperation(value = "根据条件查询的分页接口",httpMethod = "POST",response = CommonResult.class)
    @ApiParam(name = "OrderQuery",value = "传入一个OrdeQruery对象，可进行条件查询")
    @RequestMapping(value = "/pageOrder",method = RequestMethod.POST)
    public CommonResult<PageList<Order>> pageOrder(@RequestBody OrderQuery query) {

        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(!StringUtils.isEmpty(query.getUserUuid()),"user_uuid",query.getUserUuid())
                .eq(!StringUtils.isEmpty(query.getShopUuid()),"shop_uuid",query.getShopUuid())
                //.eq(query.getOrderNum()>0? true:false,"order_num",query.getOrderNum())
                //.le("state_id",5)
                .le(StringUtils.isNotBlank(query.getPayState()),"pay_state",query.getPayState())
                .ge(query.getCreateTime1()!=null,"create_time",query.getCreateTime1())
                .le(query.getCreateTime2()!=null,"create_time",query.getCreateTime2());
        //生成分页规则
        Page<Order> orderpage = new Page<Order>(query.getPage(),query.getRows());
        Page<Order> page = null;
        try {
            page = orderService.page(orderpage,queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getMessage());
            throw new JSYException(-1,"分页条件错误，请刷新重试");
        }
        return CommonResult.ok(new PageList<Order>(page.getTotal(),page.getRecords()));
    }
    @ApiOperation(value = "我的订单分页查询",httpMethod = "POST",response = CommonResult.class)
    @ApiParam(name = "OrderQuery",value = "传入一个OrdeQruery对象，可进行条件查询")
    @PostMapping("/pageForUser")
    public CommonResult pageForUser(@RequestBody OrderQuery orderQuery){
        UserEntity userEntity = CurrentUserHolder.getUserEntity();
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tr.user_uuid",userEntity.getUid())
                .le("tr.state_id",Global.INT_4)
                .le("tr.pay_state",Global.INT_4)
                .ge(orderQuery.getCreateTime1()!=null,"tr.create_time",orderQuery.getCreateTime1())
                .le(orderQuery.getCreateTime2()!=null,"tr.create_time",orderQuery.getCreateTime2());
        List<Order> list =new ArrayList<>();
        IPage<OrderInfoDto> iPage;
                Page<OrderInfoDto> page = new Page<>(orderQuery.getPage(),orderQuery.getRows());
        try{

            iPage = orderService.pageUserState(page,queryWrapper);
        }catch (Exception e) {
            e.printStackTrace();
            log.info(e.getMessage());
            throw new JSYException(-1,"分页条件错误，请刷新重试");
        }
        List<OrderInfoDto> records = iPage.getRecords();
        List<OrderInfoDto> orders = new ArrayList<>();

        for (OrderInfoDto orderInfoDto :records){
            Integer integer = OrderStateChange.OrderStateChangeInt(orderInfoDto.getPayState(), orderInfoDto.getUsed(), orderInfoDto.getEvaluationId().toString());
            String state = OrderStateEnum.OrderStateFromEnum.getCode(integer);
            orderInfoDto.setAppState(state);
            orderInfoDto.setAppStateNum(integer);
            orders.add(orderInfoDto);
        }
        return CommonResult.ok(new PageList<OrderInfoDto>(iPage.getTotal(),orders));
    }
   /**
     * 根据订单uuid查询订单的商品
     * @param uuid
     * @return
     */
    @ApiOperation(value = "根据订单uuid查询订单的商品",httpMethod = "PUT",response = CommonResult.class)
    @ApiParam(name = "id",value = "传入订单uuid查询所属商品")
    @RequestMapping(value = "/goodsOrder/{uuid}",method = RequestMethod.PUT)
    public CommonResult goodsOrder(@PathVariable("uuid") String uuid){
        List<OrderCommodityDto> list = orderService.findGoods(uuid);
        return CommonResult.ok(list);
    }

    /**
     * 根据用户id查询用户未评价订单
     * @param
     * @return
     */
    @ApiOperation(value = "根据用户uuid查询用户未评价订单",httpMethod = "GET",response = CommonResult.class)
    @ApiParam(name = "id",value = "根据用户uuid查询用户未评价订单")
    @RequestMapping(value = "/waitOrder",method = RequestMethod.GET)
    public CommonResult waitOrder(){
        UserEntity userEntity = CurrentUserHolder.getUserEntity();
        List<OrderDto> list = orderService.selectWait(userEntity.getUid());
        return CommonResult.ok(list);
    }
    
    /**  
     * @return com.jsy.basic.util.vo.CommonResult
     * @Author 国民探花
     * @Description
     * @Date 2021-04-26 15:25
     * @Param [] 
     **/
    @ApiOperation(value = "根据用户uuid查询待付款订单",httpMethod = "GET")
    @RequestMapping(value = "/waitPayed",method = RequestMethod.GET)
    public CommonResult waitPayed(){
        UserEntity userEntity = CurrentUserHolder.getUserEntity();
        List<OrderDto> list = orderService.waitPayed(userEntity.getUid());
        return CommonResult.ok(list);
    }
    @ApiOperation(value = "根据用户uuid查询待使用订单",httpMethod = "GET")
    @RequestMapping(value = "/waitUsed",method = RequestMethod.GET)
    public CommonResult waitUsed(){
        UserEntity userEntity = CurrentUserHolder.getUserEntity();
        List<OrderDto> list  = orderService.waitUsed(userEntity.getUid());
        return CommonResult.ok(list);
    }
    @ApiOperation(value = "根据用户uuid获取已完成订单",httpMethod = "GET")
    @RequestMapping(value = "/ownOrder",method = RequestMethod.GET)
    public CommonResult ownOrder(){
        UserEntity userEntity = CurrentUserHolder.getUserEntity();
        List<OrderDto> list = orderService.ownOrder(userEntity.getUid());
        return CommonResult.ok(list);
    }

    @ApiOperation(value = "待评价")
    @PostMapping("/pageUserState")
    public CommonResult pageUserState(@RequestBody OrderQuery orderQuery){
        UserEntity userEntity = CurrentUserHolder.getUserEntity();
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        if(Global.Str0.equals(orderQuery.getPayState())){
            queryWrapper.eq(StringUtils.isNotBlank(orderQuery.getPayState()),"tr.pay_state",orderQuery.getPayState());
        }else {
            queryWrapper.le(StringUtils.isNotBlank(orderQuery.getPayState()),"tr.pay_state",orderQuery.getPayState())
            .ge(StringUtils.isNotBlank(orderQuery.getPayState()),"tr.pay_state",Global.INT_2);
        }
            queryWrapper
                .eq(orderQuery.getStateId()>Global.INT_0,"tr.state_id",orderQuery.getStateId())
                .eq(Objects.nonNull(orderQuery.getEvaluationId()),"tr.evaluation_id",orderQuery.getEvaluationId())
                .eq("tr.user_uuid",userEntity.getUid())
                .ge(orderQuery.getCreateTime1()!=null,"tr.create_time",orderQuery.getCreateTime1())
                .le(orderQuery.getCreateTime2()!=null,"tr.create_time",orderQuery.getCreateTime2());
        Page<Order> page = new Page<>(orderQuery.getPage(), orderQuery.getRows());
        IPage<OrderInfoDto> iPage = orderService.pageUserState(page,queryWrapper);
        List<OrderInfoDto> records = iPage.getRecords();
        List<OrderInfoDto> orders = new ArrayList<>(records.size());

        Integer orderState = orderQuery.getOrderState();
        for (OrderInfoDto orderInfoDto :records){
            //获取订单属性代表的int类型
            Integer integer = OrderStateChange.OrderStateChangeInt(orderInfoDto.getPayState(), orderInfoDto.getUsed(), orderInfoDto.getEvaluationId().toString());
            if (OrderStateChange.selection(orderState,integer)){
            //根据订单属性的int类型获取属性名（便于维护）
            String state = OrderStateEnum.OrderStateFromEnum.getCode(integer);
            orderInfoDto.setAppStateNum(integer);
            orderInfoDto.setAppState(state);
            orders.add(orderInfoDto);
            }
        }
        return CommonResult.ok(new PageList<OrderInfoDto>(iPage.getTotal(),orders));
    }

    @ApiOperation(value = "订单统计今日，昨日，统计七日（不包括今日），三十日（不包括今日）")
    @PostMapping("/getStatistics")
    public CommonResult getStatistics(@RequestBody OrderQuery query){
        UserEntity userEntity = CurrentUserHolder.getUserEntity();
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        if(query.getDayType()!=Global.INT_0){
            queryWrapper.ge("create_time",LocalTimeUtil.getStart(query.getDayType()))
                    .le("create_time",LocalTimeUtil.getEnd(Global.INT_1));
        }else if (query.getDayType()==Global.INT_0){
            queryWrapper.ge("create_time",LocalTimeUtil.getStart(query.getDayType()))
                    .le("create_time",LocalTimeUtil.getEnd(Global.INT_0));
        }
            queryWrapper.eq("state_id",Global.INT_3);
        Map<String,String> turnOver = orderService.getTurnover(queryWrapper);
        int i = orderService.selectBackCount(userEntity.getUid());
        turnOver.put("return",String.valueOf(i));

        return CommonResult.ok(turnOver);
    }

    /**
     * 根据用户id查询用户退款中订单
     * @param
     * @return
     */
    @ApiOperation(value = "根据用户uuid查询用户退款中订单",httpMethod = "GET",response = CommonResult.class)
    @ApiParam(name = "id",value = "根据用户uuid查询用户退款中订单")
    @RequestMapping(value = "/backOrder",method = RequestMethod.GET)
    public CommonResult backOrder(){
        UserEntity userEntity = CurrentUserHolder.getUserEntity();
        List<OrderDto> list =orderService.selectBack(userEntity.getUid());
        return CommonResult.ok(list);
    }

    /**
     * 根据用户id查询历史完成的订单
     * @param
     * @return
     */
    @ApiOperation(value = "根据用户uuid查询历史完成的订单",httpMethod = "GET",response = CommonResult.class)
    @ApiParam(name = "uuid",value = "根据用户uuid查询用户历史完成订单")
    @RequestMapping(value = "/historyOrder",method = RequestMethod.GET)
    public CommonResult historyOrder(){
        UserEntity userEntity = CurrentUserHolder.getUserEntity();
        List<OrderDto> list = orderService.getHistory(userEntity.getUid());
        return CommonResult.ok(list);
    }

    /**
     * 根据店家id查询订单详细信息，包括商品详细信息dto
     * @param uuid
     * @return
     */
    @ApiOperation(value = "根据店家uuid查询订单详细信息，包括商品详细信息",httpMethod = "GET",response = CommonResult.class)
    @ApiParam(name = "店铺id",value = "根据店家uuid查询订单详细信息")
    @RequestMapping(value = "/pageBill/{uuid}",method = RequestMethod.GET)
    public CommonResult pageBill(@PathVariable("uuid") String uuid){
        List<OrderDto> orderDtos = orderService.getBillBySid(uuid);
        return CommonResult.ok(orderDtos);
    }

    /**
     * 根据时间算已支付的订单总金额
     * @param query
     * @return
     */
    @ApiOperation(value = "获取区间内总金额",httpMethod = "POST",response = CommonResult.class)
    @ApiParam(name = "OrderQuery",value = "获取区间内总金额")
    @RequestMapping(value = "/money",method = RequestMethod.POST)
    public CommonResult mouthBill(@RequestBody OrderQuery query){

        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .ge(query.getCreateTime1()!=null,"create_time",query.getCreateTime1())
                .le(query.getCreateTime2()!=null,"create_time",query.getCreateTime2())
                .eq(query.getStateId()>Global.INT_0,"state_id",Global.INT_3);
        //获取已完成的订单金额
        BigDecimal money = orderService.getMoney(queryWrapper);
        try {
        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getMessage());
            throw new JSYException(JSYError.INTERNAL.getCode(),"请联系管理员");
        }
        return CommonResult.ok(money);
    }

    /**
     * 测试通过
     * 店家定位到订单用户位置附近50米可以点击订单完成成功
     * @param shopFinsh
     * @return
     */
    @ApiOperation(value = "店家确定订单完成",httpMethod = "POST",response = CommonResult.class)
    @ApiParam(name = "shopFinsh",value = "店家确定订单完成")
    @RequestMapping(value = "/shopFinish",method = RequestMethod.POST)
    public CommonResult shopFinish(@RequestBody ShopFinsh shopFinsh){

        UserDto userDto = CurrentUserHolder.getCurrentUser();
        if (userDto.getUuid().equals(shopFinsh.getUserUuid())) {
            return CommonResult.error(-1,"店家不匹配");
        }
        OrderDto orderDto = null;
        String lonLat = null;
        String lonLat1 = null;
        try {
            //根据订单uuid获取订单上面的用户地址
            orderDto = orderService.getByUuid(shopFinsh.getOrderUuid());
            lonLat = gouldUtil.getLonLat(orderDto.getAddress());
            lonLat1 = gouldUtil.getLonLat(shopFinsh.getEnd());
        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getMessage());
            throw new JSYException(JSYError.INTERNAL.getCode(),"系统繁忙，请稍后再试");
        }
        if (Objects.isNull(orderDto)){
            return CommonResult.error(JSYError.NOT_FOUND.getCode(),"订单未找到");
        }
        if (StringUtils.isEmpty(lonLat)){
            return CommonResult.error(JSYError.NOT_FOUND.getCode(),"地图繁忙");
        }
        if (StringUtils.isEmpty(lonLat1)){
            return CommonResult.error(JSYError.NOT_FOUND.getCode(),"地图繁忙");
        }
        if (Global.Str_1.equals(lonLat)){
            return CommonResult.error(JSYError.NOT_FOUND.getCode(),"请输入正确地址");
        }
        if (Global.Str_1.equals(lonLat1)){
            return CommonResult.error(JSYError.NOT_FOUND.getCode(),"请输入正确地址");
        }
        /* one 根据高德api算经纬度的距离 */
        //long distance = gouldUtil.getDistanceByAddress(lonLat, lonLat1);
        /* two 根据Math算经纬度的距离 */
        double distance = gouldUtil.getDistance(lonLat,lonLat1);
        /* 距离小于五十可以成功 */
        if (distance > Global.LONG_50){
            return CommonResult.error(JSYError.UNAUTHORIZED.getCode(),"请到达之后再完成订单");
        }
        orderService.finishOrderByUuid(orderDto.getUuid());
        //成功进账
        shopAssetsService.updateAssets(1,orderDto.getOrderPrice(),orderDto.getShopUuid());
        return CommonResult.ok();
    }

    /**                                  uuid                                   **/
    /**
     * 根据uuid获取用户的所有订单
     * @return
     */
     @ApiOperation(value = "根据订单uuid获取订单",httpMethod = "GET",response = CommonResult.class)
     @ApiParam(name = "uuid",value = "根据订单uuid获取订单")
     @RequestMapping(value = "/getOrder/{uuid}",method = RequestMethod.GET)
     public CommonResult getOrder(@PathVariable("uuid") String uuid){
         OrderDto orderDto = orderService.getByUuid(uuid);
         return CommonResult.ok(orderDto);
     }

    /**
     *根据uuid完成订单
     * @param uuid
     * @return
     */
     @ApiOperation(value = "根据uuid完成订单",httpMethod = "GET",response = CommonResult.class)
     @ApiParam(name = "uuid",value = "根据uuid完成订单")
     @RequestMapping(value = "/finishOrderByUuid/{uuid}",method = RequestMethod.GET)
     public CommonResult finishOrderByUuid(@PathVariable("uuid")String uuid){
         OrderDto orderDto = orderService.getByUuid(uuid);
         if(Objects.isNull(orderDto)){
             return CommonResult.error(JSYError.NOT_FOUND.getCode(),"订单没找到");
         }
         orderService.finishOrderByUuid(uuid);
         ShopAssets shopAssets = shopAssetsService.getByUUid(orderDto.getShopUuid());
         shopAssets.setProfits(shopAssets.getProfits().add(orderDto.getOrderPrice()));
         shopAssetsService.update(shopAssets,new QueryWrapper<ShopAssets>().eq("uuid",shopAssets.getUuid()));
//         UnionPayOrderRecord unionPayOrderRecord = new UnionPayOrderRecord();
//         /**
//          * {
//          *     "subject": "测试商品描述",
//          *     "orderAmt": 10,
//          *     "outTradeNo":12313131,
//          *     "walletId":2061914600241540101,
//          *     "merName":"asdf",
//          *     "tradeName": 2
//          * }
//          */
//         unionPayOrderRecord.setSubject(orderDto.getOrderNum());
//         unionPayOrderRecord.setOrderAmt(orderDto.getOrderPrice().multiply(new BigDecimal(100)));
//         unionPayOrderRecord.setTransOrderNo(orderDto.getUuid());
//         unionPayOrderRecord.setWalletId(shopAssets.getWalletId());
//         unionPayOrderRecord.setMerName(UnionPayConfig.MER_NAME);
//         unionPayOrderRecord.setTradeName(2);
//         unionPayOrderRecordService.generateOrder(unionPayOrderRecord);
         return CommonResult.ok();
     }

    /**
     * 根据uuid更改为待删除订单
     * @param uuid
     * @return
     */
    @ApiOperation(value = "根据uuid更改为待删除订单",httpMethod = "GET",response = CommonResult.class)
    @ApiParam(name ="uuid id",value = "根据uuid更改为待删除订单")
    @RequestMapping(value = "/deleteOrderByUuid/{uuid}",method = RequestMethod.GET)
     public CommonResult deleteOrderByUuid(@PathVariable("uuid")String uuid){
         orderService.deleteOrderByUuid(uuid);
         return CommonResult.ok();
     }

    /**
     * 根据用户id查询用户退款中订单
     * @param
     * @return
     */
    @ApiOperation(value = "根据用户id查询用户退款中订单",httpMethod = "GET",response = CommonResult.class)
    @ApiParam(name = "id",value = "根据用户id查询用户退款中订单")
    @RequestMapping(value = "/backOrderByUuid",method = RequestMethod.GET)
    public CommonResult backOrderByUuid(){
        UserEntity userEntity = CurrentUserHolder.getUserEntity();
        List<OrderDto> list = orderService.backOrderByUuid(userEntity.getUid());
        return CommonResult.ok(list);
    }

    /**
     * 根据店铺id查询退款中订单
     * @param uuid
     * @return
     */
    @ApiOperation(value = "根据店铺uuid查询订单",httpMethod = "GET",response = CommonResult.class)
    @ApiParam(value = "根据店铺uuid查询订单")
    @RequestMapping(value = "/backOrderBySUuid/{uuid}",method = RequestMethod.GET)
    public CommonResult backOrderBySUuid(@PathVariable("uuid") String uuid){
        List<OrderDto> list = orderService.backOrderBySUuid(uuid);
        return CommonResult.ok(list);
    }

    /**
     *  "money": 10,
     *  "count": 1,
     *  "shopUuid": "1eef1c6c6cef46ffa76e3b1141cd02df"
     * @param query
     * @return
     */
    @ApiOperation(value = "在订单中查询店铺uuid和在店铺使用平台红包的数量金额shopUuid,count，money",httpMethod = "POST",response = CommonResult.class)
    @ApiParam(name = "汇总",value = "在订单中查询店铺uuid和在店铺使用平台红包的数量shopUuid,count，money")
    @RequestMapping(value = "/getRedpacket",method = RequestMethod.POST)
    public CommonResult getRedpacket(@RequestBody OrderQuery query){
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .ge(query.getCreateTime1()!=null,"create_time",query.getCreateTime1())
                .le(query.getCreateTime2()!=null,"create_time",query.getCreateTime2())
                .eq("state_id",Global.INT_3);
        List<JSONObject> list = orderService.getRedpacket(queryWrapper);
        return CommonResult.ok(list);
    }

    /**
     * @return ResponseEntity
     * @Author 国民探花
     * @Description
     * @Date 2021-04-26 10:23
     * @Param [query]
     **/
    @ApiOperation(value = "分页条件查询  下载")
    @RequestMapping(value = "/downloadPage",method = RequestMethod.POST)
    public ResponseEntity downloadPage(@RequestBody OrderQuery query){


        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(!StringUtils.isEmpty(query.getUserUuid()),"user_uuid",query.getUserUuid())
                .eq(!StringUtils.isEmpty(query.getShopUuid()),"shop_uuid",query.getShopUuid())
                //.eq(query.getOrderNum()>0? true:false,"order_num",query.getOrderNum())
                .le("state_id",Global.INT_5)
                .ge(query.getCreateTime1()!=null,"create_time",query.getCreateTime1())
                .le(query.getCreateTime2()!=null,"create_time",query.getCreateTime2());
        //生成分页规则
        Page<Order> orderpage = new Page<Order>(query.getPage(),query.getRows());
        Page<Order> page = orderService.page(orderpage, queryWrapper);
        if (page.getRecords().isEmpty()){
            return ResponseEntity.ok("没有订单可以导出");
        }
        List<Order> records = page.getRecords();
        List<OrderForExecl> orderForExecls = new ArrayList<>(records.size());
        for (Order order:records){
            OrderForExecl orderForExecl = new OrderForExecl();
            BeanUtils.copyProperties(order,orderForExecl);
            orderForExecls.add(orderForExecl);
        }
        //前端解析
        String execlTitle = "OrderExecl" +"_"+ System.currentTimeMillis();
        try {
            ResponseEntity export = CommonExcelHelper.export(orderForExecls, TranslateMap.translateOrder(OrderForExecl.class), execlTitle);
            return export;
        } catch (IOException e){
            log.error(e.getMessage());
            return ResponseEntity.ok("下载失败");
        }
    }


    @ApiOperation("e店宝账单明细")
    @GetMapping("/billDetails")
    public CommonResult billDetails(){
    List<Order> list = orderService.billDetails();
    //计算收入
    BigDecimal positiveMoney = new BigDecimal(0),
            negativeMoney = new BigDecimal(0);
    //计算退款
    //BigDecimal negativeMoney = new BigDecimal(0);
    for (Order o :list){
        BigDecimal orderPrice = o.getOrderPrice();
        if (orderPrice.compareTo(new BigDecimal(0))>-1){
            positiveMoney = positiveMoney.add(orderPrice);
        }else {
            negativeMoney = negativeMoney.add(orderPrice);
        }
    }
    return CommonResult.ok(new OrderBill(positiveMoney,negativeMoney,list));
    }


    /**
     * *******************************************************************************************************************************************************
     */
    @ApiOperation("统计新客详情")
    @RequestMapping(value = "pub/statisticsNewUser",method = RequestMethod.POST)
    public CommonResult statisticsNewUser(@RequestParam String shopUuid){
        NewUserDto newUserDto = orderService.statisticsNewUser(shopUuid);
        return CommonResult.ok(newUserDto);
    }


    /**
     * 统计满减活动详情信息
     * fandOne接口返回活动id 在传入该接口
     */
    @ApiOperation("统计满减活动详情信息")
    @RequestMapping(value = "/pub/statisticalActivities",method = RequestMethod.GET)
    public CommonResult<ActivityDTO> statisticalActivities(@RequestParam("shopUuid") String shopUuid) {
        ActivityDTO activity = orderService.statisticalActivities(shopUuid);
        return CommonResult.ok(activity);
    }
}



