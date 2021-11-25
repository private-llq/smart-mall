package com.jsy.controller;
import com.jsy.basic.util.PageInfo;
import com.jsy.dto.SlideshowDto;
import com.jsy.param.SlideshowParam;
import com.jsy.service.ISlideshowService;
import com.jsy.domain.Slideshow;
import com.jsy.query.SlideshowQuery;
import com.jsy.basic.util.PageList;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jsy.basic.util.vo.CommonResult;
import java.util.List;

@RestController
@RequestMapping("/slideshow")
public class SlideshowController {
    @Autowired
    public ISlideshowService slideshowService;

    /**
     * 添加轮播图
     * @param slideshowParam
     * @return
     */
    @PostMapping(value="/addSlideshow")
    public CommonResult addSlideshow(@RequestBody SlideshowParam slideshowParam){
        slideshowService.addSlideshow(slideshowParam);
        return CommonResult.ok();
    }

    /**
     * 修改轮播图
     * @param slideshowParam
     * @return
     */
    @PostMapping(value="/updateSlideshow")
    public CommonResult updateSlideshow(@RequestBody SlideshowParam slideshowParam){
        slideshowService.updateSlideshow(slideshowParam);
        return CommonResult.ok();
    }

    /**
    * 删除轮播图
    * @param id
    * @return
    */
    @DeleteMapping(value="/delSlideshow")
    public CommonResult delSlideshow(@RequestParam("id") Long id){
        slideshowService.delSlideshow(id);
        return CommonResult.ok();
    }



    /**
    * 商城首页轮播图List
    * @return
    */
    @GetMapping(value = "/listSlideshow")
    public CommonResult<List<SlideshowDto>> listSlideshow(){
        List<SlideshowDto> list= slideshowService.listSlideshow();
        return CommonResult.ok(list);
    }


    /**
    * 商城后台轮播图分页
    *
    * @param slideshowQuery 查询对象
    * @return PageInfo 分页对象
    */
    @PostMapping(value = "/pageSlideshow")
    public CommonResult<PageInfo<SlideshowDto>> pageSlideshow(@RequestBody SlideshowQuery slideshowQuery) {
        PageInfo<SlideshowDto> pageInfo= slideshowService.pageSlideshow(slideshowQuery);
        return CommonResult.ok(pageInfo);
    }


}
