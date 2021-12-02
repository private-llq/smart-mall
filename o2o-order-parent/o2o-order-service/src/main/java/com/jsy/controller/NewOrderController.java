package com.jsy.controller;

import com.jsy.basic.util.utils.PagerUtils;
import com.jsy.dto.SelectShopOrderDto;
import com.jsy.dto.SelectUserOrderDto;
import com.jsy.query.*;
import com.jsy.service.INewOrderService;
import com.jsy.domain.NewOrder;
import com.jsy.basic.util.PageList;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhsj.baseweb.support.ContextHolder;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jsy.basic.util.vo.CommonResult;
import java.util.List;

@RestController
@RequestMapping("/newOrder")
public class NewOrderController {
    @Autowired
    public INewOrderService newOrderService;


    /**
     * 保存和修改公用的
     *
     * @param newOrder 传递的实体
     * @return Ajaxresult转换结果
     */
    @PostMapping(value = "/save")
    public CommonResult save(@RequestBody NewOrder newOrder) {
        try {
            if (newOrder.getId() != null) {
                newOrderService.updateById(newOrder);
            } else {
                newOrderService.save(newOrder);
            }
            return CommonResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.error(-1, "操作失败！");
        }
    }

    /**
     * 删除对象信息
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}")
    public CommonResult delete(@PathVariable("id") Long id) {
        try {
            newOrderService.removeById(id);
            return CommonResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.error(-1, "删除失败！");
        }
    }

    /**
     * 根据id查询一条
     *
     * @param id
     */
    @GetMapping(value = "/{id}")
    public NewOrder get(@PathVariable("id") Long id) {
        return newOrderService.getById(id);
    }


    /**
     * 返回list列表
     *
     * @return
     */
    @GetMapping(value = "/list")
    public List<NewOrder> list() {

        return newOrderService.list(null);
    }


    /**
     * 分页查询数据
     *
     * @param query 查询对象
     * @return PageList 分页对象
     */
    @PostMapping(value = "/pagelist")
    public PageList<NewOrder> json(@RequestBody NewOrderQuery query) {
        Page<NewOrder> page = new Page<NewOrder>(query.getPage(), query.getRows());
        page = newOrderService.page(page);
        return new PageList<NewOrder>(page.getTotal(), page.getRecords());

    }

    /**********************************************arli***************************************/

    @ApiOperation("新增订单")
    @RequestMapping(value = "/creationOrder", method = RequestMethod.POST)
    public CommonResult<Boolean> creationOrder(@RequestBody CreationOrderParam creationOrderParam) {
        Boolean b = newOrderService.creationOrder(creationOrderParam);
        return new CommonResult<Boolean>(200, "新增订单成功", b);
    }



    @ApiOperation("用户根据转态查询订单")
    @RequestMapping(value = "/selectUserOrder", method = RequestMethod.POST)
    public CommonResult<PagerUtils> selectUserOrder(@RequestBody SelectUserOrderParam param) {
        Long id= ContextHolder.getContext().getLoginUser().getId();//获取用户id
        List<SelectUserOrderDto> list = newOrderService.selectUserOrder(id, param);
        PagerUtils pagerUtils = new PagerUtils<SelectUserOrderDto>();
        PagerUtils pagerUtils1 = pagerUtils.queryPage(param.getPage(), param.getSize(), list);
        return new CommonResult<>(200, "查询成功", pagerUtils1);
    }


    @ApiOperation("商家根据转态查询订单")
    @RequestMapping(value = "/selectShopOrder", method = RequestMethod.POST)
    public CommonResult<List<SelectShopOrderDto>> selectShopOrder(@RequestBody SelectShopOrderParam param) {
        List<SelectShopOrderDto> list = newOrderService.selectShopOrder(param);
        return new CommonResult<>(200, "查询成功", list);
    }





    @ApiOperation("用户删除订单")
    @RequestMapping(value = "/deletedUserOrder", method = RequestMethod.POST)
    public CommonResult<Boolean> deletedUserOrder(Long OrderId) {
        boolean b = newOrderService.deletedUserOrder(OrderId);
        return new CommonResult<>(200, "删除成功", b);
    }


    @ApiOperation("商家根据验证码查询订单")
    @RequestMapping(value = "/shopConsentOrder", method = RequestMethod.POST)
    public CommonResult<SelectShopOrderDto> shopConsentOrder(@RequestBody ShopConsentOrderParam param) {
        SelectShopOrderDto value=newOrderService.shopConsentOrder(param);
        return CommonResult.ok(value);
    }



    @ApiOperation("商家同意预约订单")
    @RequestMapping(value = "/consentOrder", method = RequestMethod.POST)
    public CommonResult<Boolean> consentOrder(@RequestBody ConsentOrderParam param) {
        Boolean b = newOrderService.consentOrder(param.getShopId(), param.getOrderId());
        if (b) {
            return new CommonResult<>(200, "商家接受预约", b);
        }
        return new CommonResult<>(500, "未成功", b);
    }






    @ApiOperation("测试支付回调")
    @RequestMapping(value = "/testPay", method = RequestMethod.POST)
    public CommonResult<Boolean> testPay(@RequestBody CompletionPayParam param) {
        Boolean b = newOrderService.completionPay(param);
        return new CommonResult<>(200, "支付完成", b);
    }


    @ApiOperation("商家验卷接口")
    @RequestMapping(value = "/acceptanceCheck", method = RequestMethod.POST)
    public CommonResult<Boolean> acceptanceCheck(@RequestBody AcceptanceCheckParam param){
        boolean b = newOrderService.acceptanceCheck(param.getShopId(), param.getCode());
        if (b){
            return new CommonResult<>(200, "验证成功", b);
        }
        return new CommonResult<>(500, "验证失败", b);
    }




}
