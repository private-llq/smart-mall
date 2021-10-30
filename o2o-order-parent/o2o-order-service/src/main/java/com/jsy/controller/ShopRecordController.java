package com.jsy.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsy.basic.util.AjaxResult;
import com.jsy.basic.util.PageList;
import com.jsy.basic.util.constant.Global;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.domain.ShopRecord;
import com.jsy.query.ShopRecordQuery;
import com.jsy.service.IShopRecordService;
import com.jsy.vo.ShopRecordVo;
import com.jsy.vo.WithdrawVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shopRecord")
@Api("店铺流水记录服务")
public class ShopRecordController {
    @Autowired
    public IShopRecordService shopRecordService;

    /**
     * 生成店铺订单记录流水
     * @param shopRecordVo
     * @return
     */
    @ApiOperation(value = "生成店铺订单记录流水",httpMethod = "POST",response = CommonResult.class)
    @ApiParam(name = "ShopRecordVo",value = "生成店铺订单记录流水")
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public CommonResult shopRecord(ShopRecordVo shopRecordVo){
        shopRecordService.save(shopRecordVo);
        return CommonResult.ok();
    }

    @ApiOperation("账户提现")
    @PostMapping("/withdraw")
    public CommonResult withdraw(@RequestBody WithdrawVO withdrawVO) {
        shopRecordService.withdraw(withdrawVO);
        return CommonResult.ok();
    }

    @ApiOperation("根据assets_uuid查询流水")
    @GetMapping("/recordList")
    public CommonResult recordList() {
        List<ShopRecord> shopRecords = shopRecordService.recordList();
        return CommonResult.ok(shopRecords);
    }

    /**
    * 删除对象信息
    * @param id
    * @return
    */
    @RequestMapping(value="/{id}",method=RequestMethod.DELETE)
    public AjaxResult delete(@PathVariable("id") Long id){
        try {
            shopRecordService.removeById(id);
            return AjaxResult.me();
        } catch (Exception e) {
        e.printStackTrace();
            return AjaxResult.me().setMessage("删除对象失败！"+e.getMessage());
        }
    }

    //获取
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public ShopRecord get(@PathVariable("id")Long id) {
        return shopRecordService.getById(id);
    }

    /**
    * 查看所有的信息
    * @return
    */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public List<ShopRecord> list(){
        return shopRecordService.list(null);
    }


    /**
    * 分页查询数据
    *
    * @param query 查询对象
    * @return PageList 分页对象
    */
    @RequestMapping(value = "/pageList",method = RequestMethod.POST)
    public CommonResult list(@RequestBody ShopRecordQuery query){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq(StringUtils.isNotEmpty(query.getAssetsUuid()),"assets_uuid",query.getAssetsUuid())
                .eq(StringUtils.isNotEmpty(query.getAccountNumber()),"account_number",query.getAccountNumber())
                .ge(query.getCreateTime1()!=null,"create_time",query.getCreateTime1())
                .le(query.getCreateTime2()!=null,"create_time",query.getCreateTime2())
                .eq("state_id", Global.INT_1);
        Page<ShopRecord> page = new Page<ShopRecord>(query.getPage(),query.getRows());
        IPage<ShopRecord> iPage = shopRecordService.pageShopRecord(page,queryWrapper);
        page = shopRecordService.page(page,queryWrapper);

        return CommonResult.ok(new PageList<ShopRecord>(page.getTotal(),page.getRecords()));
    }
}
