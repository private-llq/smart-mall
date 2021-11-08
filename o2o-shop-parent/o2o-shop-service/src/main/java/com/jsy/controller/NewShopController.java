package com.jsy.controller;
import com.jsy.basic.util.utils.ValidatorUtils;
import com.jsy.parameter.NewShopParam;
import com.jsy.service.INewShopService;
import com.jsy.domain.NewShop;
import com.jsy.query.NewShopQuery;
import com.jsy.basic.util.PageList;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhsj.basecommon.vo.R;
import com.zhsj.baseweb.annotation.LoginIgnore;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jsy.basic.util.vo.CommonResult;
import java.util.List;

@RestController
@RequestMapping("/newShop")
@Slf4j
@Api(tags="新-商家")
public class NewShopController {
    @Autowired
    public INewShopService newShopService;

    /**
     * 保存和修改公用的
     * @param shopPacketParam  传递的实体
     * @return Ajaxresult转换结果
     */
    @ApiOperation("创建店铺")
    @PostMapping(value="/addNewShop")
    public CommonResult addNewShop(@RequestBody NewShopParam shopPacketParam){
        log.info("入参：{}",shopPacketParam);
         ValidatorUtils.validateEntity(shopPacketParam,NewShopParam.newShopValidatedGroup.class);
        try {
              newShopService.addNewShop(shopPacketParam);
            return CommonResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return  CommonResult.error(-1,"创建成功！");
        }
    }

    @LoginIgnore
    @PostMapping("/test")
    public R<Void> test(){
        return R.ok();
    }

    /**
    * 保存和修改公用的
    * @param newShop  传递的实体
    * @return Ajaxresult转换结果
    */
    @PostMapping(value="/save")
    public CommonResult save(@RequestBody NewShop newShop){
        try {
            if(newShop.getId()!=null){
                newShopService.updateById(newShop);
            }else{
                newShopService.save(newShop);
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
            newShopService.removeById(id);
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
    public NewShop get(@PathVariable("id")Long id)
    {
        return newShopService.getById(id);
    }


    /**
    * 返回list列表
    * @return
    */
//    @LoginIgnore
    @GetMapping(value = "/list")
    public List<NewShop> list(){

        return newShopService.list(null);
    }


    /**
    * 分页查询数据
    *
    * @param query 查询对象
    * @return PageList 分页对象
    */
    @PostMapping(value = "/pagelist")
    public PageList<NewShop> json(@RequestBody NewShopQuery query)
    {
        Page<NewShop> page = new Page<NewShop>(query.getPage(),query.getRows());
        page = newShopService.page(page);
        return new PageList<NewShop>(page.getTotal(),page.getRecords());
    }
}
