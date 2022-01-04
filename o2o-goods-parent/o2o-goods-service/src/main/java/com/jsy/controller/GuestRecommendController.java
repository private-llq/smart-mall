package com.jsy.controller;

import com.jsy.basic.util.PageInfo;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.domain.GuestRecommend;
import com.jsy.dto.GuestRecommendDto;
import com.jsy.query.GuestRecommendQuery;
import com.jsy.service.IGuestRecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/guestRecommend")
public class GuestRecommendController {
    @Autowired
    public IGuestRecommendService guestRecommendService;

    /**
    * 新增
    * @param guestRecommend  实体类对象
    * @return CommonResult 公共返回类
    */
    @PostMapping("/saveGuestRecommend")
    public CommonResult saveGuestRecommend(@RequestBody GuestRecommend guestRecommend){
        try {
            guestRecommendService.saveGuestRecommend(guestRecommend);
            return CommonResult.ok();
        } catch (Exception ex) {
            return CommonResult.error(-1,"操作失败！");
        }

    }

    /**
     * 修改
     * @param guestRecommend  实体类对象
     * @return CommonResult 公共返回类
     */
    @PostMapping("/updateGuestRecommend")
    public CommonResult updateGuestRecommend(@RequestBody GuestRecommend guestRecommend){
        try {
             guestRecommendService.updateById(guestRecommend);
            return CommonResult.ok();
        } catch (Exception ex) {
            return CommonResult.error(-1,"操作失败！");
        }
    }

    /**
    * 删除
    * @param id
    * @return CommonResult 公共返回类
    */
    @DeleteMapping("/deleteGuestRecommend")
    public CommonResult deleteGuestRecommend(@RequestParam("id") Long id){
        try {
            guestRecommendService.removeById(id);
            return CommonResult.ok();
        } catch (Exception ex) {
            return CommonResult.error(-1,"操作失败！");
        }
    }

    /**
    * 查询
    * @param id
    */
    @GetMapping("getGuestRecommend")
    public CommonResult<GuestRecommend> getGuestRecommend(@RequestParam("id")Long id)
    {
        GuestRecommend getGuestRecommend=guestRecommendService.getById(id);
        return CommonResult.ok(getGuestRecommend);
    }


    /**
    * list列表
    * @return
    */
    @GetMapping("/listGuestRecommend")
    public CommonResult<List<GuestRecommend>> listGuestRecommend(){
        List<GuestRecommend> list=guestRecommendService.list(null);
        return CommonResult.ok(list);
    }


    /**
    * 分页查询数据
    * @param query 查询对象
    * @return PageInfo 分页对象
    */
    @PostMapping("/pageGuestRecommend")
    public CommonResult<PageInfo<GuestRecommendDto>> pageGuestRecommend(@RequestBody GuestRecommendQuery query)
    {
        PageInfo<GuestRecommendDto> pageInfo= guestRecommendService.pageGuestRecommend(query);

        return CommonResult.ok(pageInfo);
    }
}
