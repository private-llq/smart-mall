package com.jsy.controller;

import com.jsy.basic.util.utils.PagerUtils;
import com.jsy.dto.SelectUserOrderDTO;
import com.jsy.query.CompletionPayParam;
import com.jsy.query.CreationOrderParam;
import com.jsy.query.SelectUserOrderParam;
import com.jsy.service.INewOrderService;
import com.jsy.domain.NewOrder;
import com.jsy.query.NewOrderQuery;
import com.jsy.basic.util.PageList;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhsj.baseweb.annotation.Permit;
import com.zhsj.baseweb.support.ContextHolder;
import com.zhsj.baseweb.support.LoginUser;
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
    public CommonResult<List<SelectUserOrderDTO>> selectUserOrder(@RequestBody SelectUserOrderParam param) {

        //Long id1 = ContextHolder.getContext().getLoginUser().getId();//获取用户id

        Long id = 0L;//写死
        List<SelectUserOrderDTO> list = newOrderService.selectUserOrder(id, param, 0);

        PagerUtils pagerUtils = new PagerUtils<SelectUserOrderDTO>();
        PagerUtils pagerUtils1 = pagerUtils.queryPage(param.getPage(), param.getSize(), list);
        return new CommonResult<>(200, "查询成功", pagerUtils1.getRecords());
    }


    @ApiOperation("商家根据转态查询订单")
    @RequestMapping(value = "/selectShopOrder", method = RequestMethod.POST)
    public CommonResult<List<SelectUserOrderDTO>> selectShopOrder(@RequestBody SelectUserOrderParam param) {
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();//获取用户id
        Long id = 0L;/*loginUser.getId();*/
        List<SelectUserOrderDTO> list = newOrderService.selectUserOrder(id, param, 1);
        return new CommonResult<>(200, "查询成功", list);
    }

    @ApiOperation("用户删除订单")
    @RequestMapping(value = "/deletedUserOrder", method = RequestMethod.POST)
    public CommonResult<Boolean> deletedUserOrder(Long OrderId) {
        boolean b = newOrderService.deletedUserOrder(OrderId);
        return new CommonResult<>(200, "删除成功", b);
    }

    @ApiOperation("商家同意预约订单")
    @RequestMapping(value = "/consentOrder", method = RequestMethod.POST)
    public CommonResult<Boolean> consentOrder(Long OrderId) {
        Boolean b = newOrderService.consentOrder(OrderId);
        return new CommonResult<>(200, "商家接受预约", b);
    }





   // @Permit("")
    @ApiOperation("测试支付")
    @RequestMapping(value = "/testPay", method = RequestMethod.POST)
    public CommonResult<Boolean> consentOrder(@RequestBody CompletionPayParam param) {
        Boolean b = newOrderService.completionPay(param);
        return new CommonResult<>(200, "支付完成", b);
    }


}
