package com.jsy.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsy.basic.util.PageList;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.domain.BusinessFeedappend;
import com.jsy.query.BusinessFeedappendQuery;
import com.jsy.service.IBusinessFeedappendService;
import com.jsy.vo.BusinessFeedbackAppendVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/userFeedappend")
public class BusinessFeedappendController {
    @Autowired
    public IBusinessFeedappendService userFeedappendService;


    @RequestMapping(value="/append",method = RequestMethod.POST)
    public CommonResult<Boolean> append(@RequestBody BusinessFeedbackAppendVo businessFeedbackAppendVo){
        Boolean aBoolean =  userFeedappendService.append(businessFeedbackAppendVo);
        return new CommonResult<>(200,"提交成功",aBoolean);
    }

    /**
     * 返回list列表
     * @return
     */
    @GetMapping(value = "/list")
    public CommonResult<List<BusinessFeedappend>> list(){
        List<BusinessFeedappend> businessFeedappends = userFeedappendService.list(null);
        return new CommonResult(200,"操作成功", businessFeedappends);
    }


//    /**
//     * 根据意见反馈的订单id查询反馈进度
//     * @param orderUuid
//     * @return
//     */
//    @ApiOperation("商家根据意见反馈的订单id查询反馈进度")
//    @GetMapping(value = "/getByOrderUuid/{order_uuid}/{type}")
//    public CommonResult getOrderUUid(@PathVariable("order_uuid")String orderUuid)
//    {
//        return CommonResult.ok(userFeedappendService.getByOrderUuid(orderUuid));
//    }

    /**
    * 保存和修改公用的
    * @param businessFeedappend  传递的实体
    * @return Ajaxresult转换结果
    */
    @PostMapping(value="/save")
    public CommonResult save(@RequestBody BusinessFeedappend businessFeedappend){
        try {
            if(businessFeedappend.getId()!=null){
                userFeedappendService.updateById(businessFeedappend);
            }else{
                userFeedappendService.save(businessFeedappend);
            }
            return CommonResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.error(-1,"操作失败！");
        }
    }

    /**
    * 删除对象信息
    * @param id
    * @return
    */
    @DeleteMapping(value="/{id}")
    public CommonResult delete(@PathVariable("id") Long id){
        try {
            userFeedappendService.removeById(id);
            return CommonResult.ok();
        } catch (Exception e) {
        e.printStackTrace();
            return  CommonResult.error(-1,"删除失败！");
        }
    }

    /**
    * 根据id查询一条
    * @param id
    */
    @GetMapping(value = "/{id}")
    public BusinessFeedappend get(@PathVariable("id")Long id)
    {
        return userFeedappendService.getById(id);
    }





    /**
    * 分页查询数据
    *
    * @param query 查询对象
    * @return PageList 分页对象
    */
    @PostMapping(value = "/pagelist")
    public PageList<BusinessFeedappend> json(@RequestBody BusinessFeedappendQuery query)
    {
        Page<BusinessFeedappend> page = new Page<BusinessFeedappend>(query.getPage(),query.getRows());
        page = userFeedappendService.page(page);
        return new PageList<BusinessFeedappend>(page.getTotal(),page.getRecords());
    }
}
