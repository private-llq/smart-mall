package com.jsy.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jsy.dto.FeedbackDto;
import com.jsy.param.FeedbackParam;
import com.jsy.service.IFeedbackService;
import com.jsy.domain.Feedback;
import com.jsy.query.FeedbackQuery;
import com.jsy.basic.util.PageList;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jsy.basic.util.vo.CommonResult;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {
    @Autowired
    public IFeedbackService feedbackService;

    /**
    * 保存和修改公用的
    * @param param  传递的实体
    * @return Ajaxresult转换结果
    */
    @PostMapping(value="/save")
    public CommonResult save(@RequestBody FeedbackParam param){
        try {
            Feedback feedback = new Feedback();
            BeanUtils.copyProperties(param,feedback);
            if(param.getId()!=null){
                feedbackService.updateById(feedback);
            }else{
                feedbackService.save(feedback);
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
            feedbackService.removeById(id);
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
    public FeedbackDto get(@PathVariable("id")Long id)
    {
        Feedback feedback = feedbackService.getById(id);
        FeedbackDto feedbackDto = new FeedbackDto();
        BeanUtils.copyProperties(feedback,feedbackDto);

        return feedbackDto;
    }


    /**
    * 返回list列表
    * @return
    */
    @GetMapping(value = "/list")
    public List<FeedbackDto> list(){
        List<Feedback> list = feedbackService.list(null);
        List<FeedbackDto> dtoList = new ArrayList<>();
        for (Feedback feedback : list) {
            FeedbackDto feedbackDto = new FeedbackDto();
            BeanUtils.copyProperties(feedback,feedbackDto);
            dtoList.add(feedbackDto);
        }
        return dtoList;
    }

    /**
     * 返回list列表
     * @return
     */
    @GetMapping(value = "/getList")
    public List<FeedbackDto> getList(Long shopId){
        List<Feedback> list = feedbackService.list(new QueryWrapper<Feedback>().eq("shop_id",shopId));
        List<FeedbackDto> dtoList = new ArrayList<>();
        for (Feedback feedback : list) {
            FeedbackDto feedbackDto = new FeedbackDto();
            BeanUtils.copyProperties(feedback,feedbackDto);
            dtoList.add(feedbackDto);
        }
        return dtoList;
    }

    /**
    * 分页查询数据
    *
    * @param query 查询对象
    * @return PageList 分页对象
    */
    @PostMapping(value = "/pagelist")
    public PageList<Feedback> json(@RequestBody FeedbackQuery query)
    {
        Page<Feedback> page = new Page<Feedback>(query.getPage(),query.getRows());
        page = feedbackService.page(page);
        return new PageList<Feedback>(page.getTotal(),page.getRecords());
    }
}
