package com.jsy.controller;
import cn.hutool.core.util.ObjectUtil;
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

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
        Browse serviceOne = browseService.getOne(new QueryWrapper<Browse>().eq("goods_id", browse.getGoodsId()));
        try {
            if(ObjectUtil.isNotNull(serviceOne)){
                browseService.updateById(serviceOne);
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
            browseService.del(id);
            return CommonResult.ok();
        } catch (Exception e) {
        e.printStackTrace();
            return  CommonResult.error(-1,"删除失败！");
        }
    }
    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> concurrentHashMap = new ConcurrentHashMap<>();
        return t -> concurrentHashMap.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    /**
    * 分页返回list列表
    * @return
    */
    @PostMapping(value = "/list")
    public CommonResult<PageInfo<BrowseDto>> list(@RequestBody BrowseQuery browseQuery){
        Long id = ContextHolder.getContext().getLoginUser().getId();
        List<Browse> list = browseService.list(new QueryWrapper<Browse>().eq("user_id",id).orderByDesc("create_time"));
        List<BrowseDto> dtoList = BeansCopyUtils.copyListProperties(list, BrowseDto::new);
        List<BrowseDto> collect = dtoList.stream().filter(distinctByKey(BrowseDto::getGoodsId)).collect(Collectors.toList());
        PageInfo<BrowseDto> browsePageInfo = MyPageUtils.pageMap(browseQuery.getPage(), browseQuery.getRows(), collect);
        return CommonResult.ok(browsePageInfo);

    }

     /**
      * @author Tian
      * @since 2021/12/20-14:38
      * @description 批量删除
      **/

     @PostMapping(value = "/delList")
     public CommonResult delList(@RequestBody List<Long> stringList){
         browseService.delList(stringList);
         return CommonResult.ok();

     }
}
