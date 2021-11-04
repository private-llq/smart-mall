package com.jsy.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jsy.basic.util.RedisStateCache;
import com.jsy.dto.BusinessFeedbackDto;
import com.jsy.service.IBusinessFeedbackService;
import com.jsy.domain.BusinessFeedback;
import com.jsy.query.BusinessFeedbackQuery;
import com.jsy.basic.util.PageList;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.jsy.vo.BusinessFeedbackAppendVo;
import com.jsy.vo.BusinessFeedtypeVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jsy.basic.util.vo.CommonResult;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/userFeedback")
public class BusinessFeedbackController {
    @Autowired
    public IBusinessFeedbackService userFeedbackService;

    @Autowired
    private RedisStateCache redisStateCache;


    /**
     * @Description:
     * @author: Tian
     * @since:
     * @Param: businessFeedtypeVo
     * @return: CommonResult
     */
  @ApiOperation("店铺新增意见反馈和投诉建议")
    @RequestMapping(value="/save",method = RequestMethod.POST)
    public CommonResult<Boolean> save(@RequestBody BusinessFeedtypeVo businessFeedtypeVo){


      if (!userFeedbackService.selectCount(businessFeedtypeVo.getInfo_uuid())){
          return CommonResult.error(-1,"今日份反馈意见已经提交三次，请明天在提交");
      }
      if (!userFeedbackService.selectState(businessFeedtypeVo.getInfo_uuid())){
          return CommonResult.error(-1,"未处理的意见已有3条，请等待处理后提交");

      }
      if (!userFeedbackService.selectCreateTime(businessFeedtypeVo.getInfo_uuid())){
          return CommonResult.error(-1,"距离上次还未过10分钟，请稍后再试");
      }
      Boolean aBoolean = userFeedbackService.addUserFeedback(businessFeedtypeVo);
      return new CommonResult<>(200,"提交成功",aBoolean);
    }

//    @RequestMapping(value="/append",method = RequestMethod.POST)
//    public CommonResult<Boolean> append(@RequestBody BusinessFeedbackAppendVo businessFeedbackAppendVo){
//        Boolean aBoolean =  userFeedbackService.append(businessFeedbackAppendVo);
//        return new CommonResult<>(200,"提交成功",aBoolean);
//    }



    /**
    * 删除对象信息
    * @param id
    * @return
    */
    @DeleteMapping(value="/{id}")
    public CommonResult delete(@PathVariable("id") Long id){
        try {
            userFeedbackService.removeById(id);
            return CommonResult.ok();
        } catch (Exception e) {
        e.printStackTrace();
            return  CommonResult.error(-1,"删除失败！");
        }
    }


    /**
     * 根据意见反馈的订单id查询反馈进度
     * @param orderUuid
     * @return
     */
    @ApiOperation("商家根据意见反馈的订单id查询反馈进度")
    @GetMapping(value = "/getByOrderUuid/{order_uuid}")
    public CommonResult getOrderUUid(@PathVariable("order_uuid")String orderUuid)
    {
        return CommonResult.ok(userFeedbackService.getByOrderUuid(orderUuid));
    }

    /**
    * 根据id查询一条
    * @param id
    */
    @GetMapping(value = "/{id}")
    public BusinessFeedback get(@PathVariable("id")Long id)
    {
        return userFeedbackService.getById(id);
    }


    /**
    * 返回list列表
    * @return
    */
    @GetMapping(value = "/list")
    public List<BusinessFeedback> list(){

        return userFeedbackService.list(null);
    }



    /**
    * 分页查询数据
    *
    * @param query 查询对象
    * @return PageList 分页对象
    */
    @PostMapping(value = "/pagelist")
    public PageList<BusinessFeedback> json(@RequestBody BusinessFeedbackQuery query)
    {
        Page<BusinessFeedback> page = new Page<BusinessFeedback>(query.getPage(),query.getRows());
        page = userFeedbackService.page(page);
        return new PageList<BusinessFeedback>(page.getTotal(),page.getRecords());
    }




}
