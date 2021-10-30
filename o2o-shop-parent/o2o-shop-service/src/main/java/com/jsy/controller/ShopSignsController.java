package com.jsy.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jsy.basic.util.exception.JSYException;
import com.jsy.basic.util.utils.UUIDUtils;
import com.jsy.basic.util.utils.ValidatorUtils;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.service.IShopSignsService;
import com.jsy.domain.ShopSigns;
import com.jsy.query.ShopSignsQuery;
import com.jsy.basic.util.PageList;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Objects;
@RestController
@RequestMapping("/shopSigns")
public class ShopSignsController {
    @Autowired
    public IShopSignsService shopSignsService;


    @ApiOperation("添加或修改门店招牌")
    @RequestMapping(value="/save",method= RequestMethod.POST)
    public CommonResult save(@RequestBody ShopSigns shopSigns){
        ValidatorUtils.validateEntity(shopSigns);
        try {
            if(StringUtils.isNotEmpty(shopSigns.getUuid())){
                shopSignsService.update(shopSigns,new UpdateWrapper<ShopSigns>().eq("uuid",shopSigns.getUuid()));
            }else{
                ShopSigns data = this.get(shopSigns.getShopUuid()).getData();
                if (Objects.nonNull(data)){
                    return CommonResult.error(-1,"你已经添加过招牌了！");
                }
                shopSigns.setUuid(UUIDUtils.getUUID());
                shopSignsService.save(shopSigns);
            }
            return CommonResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.ok();
        }
    }


    @ApiOperation("根据uuid删除门店招牌")
    @RequestMapping(value="/{uuid}",method=RequestMethod.DELETE)
    public CommonResult delete(@PathVariable("uuid") Long uuid){
        try {
            shopSignsService.remove(new QueryWrapper<ShopSigns>().eq("uuid",uuid));
            return CommonResult.ok();
        } catch (Exception e) {
        e.printStackTrace();
            return CommonResult.error(-1,"删除失败！");
        }
    }


    @ApiOperation("根据商家uuid 获取招牌信息")
    @RequestMapping(value = "/{shopUuid}",method = RequestMethod.GET)
    public CommonResult<ShopSigns> get(@PathVariable("shopUuid")String shopUuid)
    {
        return CommonResult.ok(shopSignsService.getShopSigns(shopUuid));
    }



    @ApiOperation("返回门店招牌列表信息")
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public CommonResult<List<ShopSigns>> list(@RequestParam("shopUuid") String shopUuid){

        List<ShopSigns> signsList = shopSignsService.list(new QueryWrapper<ShopSigns>().eq("shop_uuid", shopUuid));

        return CommonResult.ok(signsList);
    }



    @ApiOperation("分页并查询条件 返回门店招牌列表信息")
    @RequestMapping(value = "/pagelist",method = RequestMethod.POST)
    public CommonResult<PageList<ShopSigns>> json (@RequestBody ShopSignsQuery query)
    {
        if (StringUtils.isEmpty(query.getShopUuid())){
            throw  new JSYException(-1,"商家ID参数不能为空！");
        }
        Page<ShopSigns> page = new Page<ShopSigns>(query.getPage(),query.getRows());
        page = shopSignsService.page(page,new QueryWrapper<ShopSigns>().eq("shop_uuid",query.getShopUuid()));
        PageList<ShopSigns> shopSignsPageList = new PageList<>(page.getTotal(), page.getRecords());

        return CommonResult.ok(shopSignsPageList);
    }
}
