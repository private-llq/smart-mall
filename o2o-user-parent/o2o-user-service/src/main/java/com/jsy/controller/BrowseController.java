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
import com.zhsj.baseweb.support.ContextHolder;
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
    * @param
    * @return
    */
    @DeleteMapping(value="/del")
    public CommonResult delete(){
        try {
            Long id = ContextHolder.getContext().getLoginUser().getId();
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
    @PostMapping(value = "/list")
    public CommonResult<PageInfo<BrowseDto>> list(@RequestBody BrowseQuery browseQuery){
        Long id = ContextHolder.getContext().getLoginUser().getId();
        List<Browse> list = browseService.list(new QueryWrapper<Browse>().select("distinct goods_id").eq("user_id",id));

        List<BrowseDto> dtoList = BeansCopyUtils.copyListProperties(list, BrowseDto::new);
        PageInfo<BrowseDto> browsePageInfo = MyPageUtils.pageMap(browseQuery.getPage(), browseQuery.getRows(), dtoList);
        return CommonResult.ok(browsePageInfo);

    }
}
