package com.jsy.controller;
import com.jsy.basic.util.PageInfo;
import com.jsy.dto.BacksageDto;
import com.jsy.dto.BackstageGoodsDto;
import com.jsy.param.BackstageGoodsParam;
import com.jsy.service.IBackstageGoodsService;
import com.jsy.domain.BackstageGoods;
import com.jsy.query.BackstageGoodsQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jsy.basic.util.vo.CommonResult;

@RestController
@RequestMapping("/backstageGoods")
public class BackstageGoodsController {
    @Autowired
    public IBackstageGoodsService backstageGoodsService;


    /**
     * 添加
     * @param backstageGoodsParam
     * @return
     */
    @PostMapping(value="/addBackstageGoods")
    public CommonResult addBackstageGoods(@RequestBody BackstageGoodsParam backstageGoodsParam){
        backstageGoodsService.addBackstageGoods(backstageGoodsParam);
        return CommonResult.ok();
    }

    /**
     * 修改
     * @param backstageGoodsParam
     * @return
     */
    @PostMapping(value="/updateBackstageGoods")
    public CommonResult updateBackstageGoods(@RequestBody BackstageGoodsParam backstageGoodsParam){
        backstageGoodsService.updateBackstageGoods(backstageGoodsParam);
        return CommonResult.ok();
    }


    /**
     * 删除
     * @param id
     * @return
     */
    @GetMapping(value="/deleteBackstageGoods")
    public CommonResult deleteBackstageGoods(@RequestParam("id") Long id){
        backstageGoodsService.removeById(id);
        return CommonResult.ok();
    }

    /**
     * 获取一条记录
     * @param id
     * @return
     */
    @GetMapping(value="/getBackstageGoods")
    public CommonResult<BackstageGoods> getBackstageGoods(@RequestParam("id") Long id){
        BackstageGoods goods = backstageGoodsService.getById(id);
        return CommonResult.ok(goods);
    }


    /**
     * 分页条件查询 管理页面
     * @param backstageGoodsQuery
     * @return
     */
    @PostMapping(value = "/pageBackstageGoods")
    public CommonResult<PageInfo<BacksageDto>> pageBackstageGoods(@RequestBody BackstageGoodsQuery backstageGoodsQuery)
    {
        PageInfo<BacksageDto>  pageInfo=backstageGoodsService.pageBackstageGoods(backstageGoodsQuery);
        return  CommonResult.ok(pageInfo);
    }


    /**
     * 分页条件查询 医疗C端页面
     * @param backstageGoodsQuery
     * @return
     */
    @PostMapping(value = "/listBackstageGoods")
    public CommonResult<PageInfo<BackstageGoodsDto>> listBackstageGoods(@RequestBody BackstageGoodsQuery backstageGoodsQuery)
    {
        PageInfo<BackstageGoodsDto>  pageInfo=backstageGoodsService.listBackstageGoods(backstageGoodsQuery);
        return  CommonResult.ok(pageInfo);
    }
}
