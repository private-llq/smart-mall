package com.jsy.controller;
import com.jsy.basic.util.PageInfo;
import com.jsy.dto.NewShopHotDto;
import com.jsy.query.NewShopQuery;
import com.jsy.service.IHotGoodsService;
import com.jsy.domain.HotGoods;
import com.jsy.query.HotGoodsQuery;
import com.jsy.basic.util.PageList;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jsy.basic.util.vo.CommonResult;
import java.util.List;

@RestController
@RequestMapping("/hotGoods")
public class HotGoodsController {
    @Autowired
    public IHotGoodsService hotGoodsService;

    /**
    * 删除对象信息
    * @param id
    * @return
    */
    @DeleteMapping(value="/{id}")
    public CommonResult delete(@PathVariable("id") Long id){
        try {
            hotGoodsService.removeById(id);
            return CommonResult.ok();
        } catch (Exception e) {
        e.printStackTrace();
            return  CommonResult.error(-1,"删除失败！");
        }
    }
    @ApiOperation("热门推荐")
    @RequestMapping(value = "/getHot",method = RequestMethod.POST)
    public CommonResult<PageInfo<HotGoods>> getHot(@RequestBody NewShopQuery newShopQuery){
        PageInfo<HotGoods> hotDtoPageInfo = hotGoodsService.getHot(newShopQuery);
        if (hotDtoPageInfo!=null){
            return CommonResult.ok(hotDtoPageInfo);
        }
        else {
            return new  CommonResult(-1,"失败",null);
        }
    }

}
