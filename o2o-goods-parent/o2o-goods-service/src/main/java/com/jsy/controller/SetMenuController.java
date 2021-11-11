package com.jsy.controller;
import com.jsy.dto.SetMenuDto;
import com.jsy.dto.SetMenuGoodsDto;
import com.jsy.service.ISetMenuService;
import com.jsy.domain.SetMenu;
import com.jsy.query.SetMenuQuery;
import com.jsy.basic.util.PageList;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jsy.basic.util.vo.CommonResult;
import java.util.List;
import java.util.Map;

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
    public CommonResult save(@RequestBody SetMenu setMenu){
        try {
            if(setMenu.getId()!=null){
                setMenuService.updateById(setMenu);
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
    * 返回list列表
    * @return
    */
    @GetMapping(value = "/getMenuId")
    public Map<String,List<SetMenuGoodsDto>> getMenuId(@RequestParam("setMenuId")Long setMenuId){
        return setMenuService.getMenuId(setMenuId);
    }
    /**
     * 返回list列表
     * @return
     */
    @GetMapping(value = "/SetMenuList")
    public List<SetMenuDto> SetMenuList(){
                Long shopId= 1L;
        return setMenuService.getSetMenulist(shopId);
    }

    /**
     * 返回list列表
     * @return
     */
    @GetMapping(value = "/list")
    public List<SetMenuDto> list(@RequestParam("shopId") Long shopId, @RequestParam("state") Integer state){
//        Long shopId= 1L;
        return setMenuService.getList(shopId,state);
    }




    /**
    * 分页查询数据
    *
    * @param query 查询对象
    * @return PageList 分页对象
    */
    @PostMapping(value = "/pagelist")
    public PageList<SetMenu> json(@RequestBody SetMenuQuery query)
    {
        Page<SetMenu> page = new Page<SetMenu>(query.getPage(),query.getRows());
        page = setMenuService.page(page);
        return new PageList<SetMenu>(page.getTotal(),page.getRecords());
    }
}
