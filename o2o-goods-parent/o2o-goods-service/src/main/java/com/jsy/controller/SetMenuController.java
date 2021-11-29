package com.jsy.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jsy.basic.util.PageInfo;
import com.jsy.domain.Goods;
import com.jsy.dto.SetMenuDto;
import com.jsy.dto.SetMenuGoodsDto;
import com.jsy.parameter.SetMenuParam;
import com.jsy.service.ISetMenuService;
import com.jsy.domain.SetMenu;
import com.jsy.query.SetMenuQuery;
import com.jsy.basic.util.PageList;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhsj.baseweb.annotation.LoginIgnore;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jsy.basic.util.vo.CommonResult;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@RestController
@RequestMapping("/setMenu")
@Api(tags = "商家套餐")
public class SetMenuController {
    @Autowired
    public ISetMenuService setMenuService;

    /**
    * 保存和修改公用的
    * @param setMenu  传递的实体
    * @return Ajaxresult转换结果
    */
    @PostMapping(value="/save")
    public CommonResult save(@RequestBody SetMenuParam setMenu){
        try {
            if(setMenu.getId()!=null){
                System.out.println("打印");
                setMenuService.updateSetMenu(setMenu);
            }else{
                setMenuService.addSetMenu(setMenu);
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
            setMenuService.removeById(id);
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
    public SetMenu get(@PathVariable("id")Long id)
    {
        return setMenuService.getById(id);
    }


    /**
    * 根据套餐id套餐详情
    * @return
    */
    @ApiOperation("根据套餐id套餐详情")
    @GetMapping(value = "/getMenuId")
    public Map<String,List<SetMenuGoodsDto>> getMenuId(@RequestParam("setMenuId")Long setMenuId){
        return setMenuService.getMenuId(setMenuId);
    }
    /**
     * 据id查询套餐和套餐详情
     * @return
     */
    @GetMapping(value = "/SetMenuList")
    public SetMenuDto SetMenuList(@RequestParam("shopId") Long shopId,Long id){
        return setMenuService.getSetMenulist(shopId,id);
    }

    /**
     * 查询商家 上架或下架套餐
     * @return
     */
    @ApiOperation("查询商家 上架或下架套餐")
    @GetMapping(value = "/list")
    public List<SetMenuDto> list(@RequestParam("shopId") Long shopId, @RequestParam("state") Integer state){
//        Long shopId= 1L;
        return setMenuService.getList(shopId,state);
    }

    /**
     * 查询所有套餐列表
     * @return
     */
    @ApiOperation("查询商家所有套餐")
    @GetMapping(value = "/listAll")
    public PageInfo<SetMenuDto> listAll(@RequestBody SetMenuQuery setMenuQuery){
        PageInfo<SetMenuDto> dtoList = setMenuService.listAll(setMenuQuery);

        return dtoList;
    }

    /**
     * 修改上下架套餐
     * @return
     */
    @ApiOperation("修改上下架套餐")
    @PostMapping(value = "/setState")
    public CommonResult setState(@RequestParam("id") Long id, @RequestParam("state") Integer state){
        try {
            setMenuService.setState(id,state);
            return CommonResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return  CommonResult.error(-1,"删除失败！");
        }
    }


    @ApiOperation("查询商家最新发布的套餐")
    @GetMapping("/getShopIdMenus")
    public CommonResult<SetMenu> getShopIdMenus(@RequestParam("shopId") Long shopId)
    {
        List<SetMenu> one = setMenuService.list(new QueryWrapper<SetMenu>().eq("shop_id", shopId).orderByDesc("create_time"));
        if (one.size()>0){
            SetMenu setMenu = one.get(0);
            return CommonResult.ok(setMenu);
        }else {
            return CommonResult.ok(null);
        }
    }

    @ApiOperation("根据id批量查询")
    @PostMapping("/batchIds")
    public CommonResult<List<SetMenuDto>> batchIds(@RequestBody List<Long> ids)
    {
        List<SetMenuDto> dtoList = setMenuService.batchIds(ids);
        return CommonResult.ok(dtoList);

    }

}
