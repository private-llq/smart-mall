package com.jsy.controller;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.druid.support.json.JSONUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jsy.basic.util.MyPageUtils;
import com.jsy.basic.util.PageInfo;
import com.jsy.basic.util.utils.BeansCopyUtils;
import com.jsy.dto.BrowseDto;
import com.jsy.service.IBrowseService;
import com.jsy.domain.Browse;
import com.jsy.query.BrowseQuery;
import com.jsy.basic.util.PageList;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jsy.basic.util.vo.CommonResult;
import java.util.List;

@RestController
@RequestMapping("/browse")
public class BrowseController {
    @Autowired
    public IBrowseService browseService;

    /**
    * 保存和修改公用的
    * @param browse  传递的实体
    * @return Ajaxresult转换结果
    */
    @PostMapping(value="/save")
    public CommonResult save(@RequestBody Browse browse){
        try {
            if(browse.getId()!=null){
                browseService.updateById(browse);
            }else{
                browseService.save(browse);
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
    public CommonResult delete(@RequestParam("id") Long id){
        try {
            browseService.removeById(id);
            return CommonResult.ok();
        } catch (Exception e) {
        e.printStackTrace();
            return  CommonResult.error(-1,"删除失败！");
        }
    }

    /**
    * 分页返回list列表
    * @return
    */
    @GetMapping(value = "/list")
    public CommonResult<PageInfo<BrowseDto>> list(@RequestBody BrowseQuery browseQuery){
        List<Browse> list = browseService.list(new QueryWrapper<Browse>().eq("user_id",browseQuery.getUserId()));
        List<BrowseDto> dtoList = BeansCopyUtils.copyListProperties(list, BrowseDto::new);
        PageInfo<BrowseDto> browsePageInfo = MyPageUtils.pageMap(browseQuery.getPage(), browseQuery.getRows(), dtoList);
        Object parse = JSONUtils.parse("");

        return CommonResult.ok(browsePageInfo);

    }
}
