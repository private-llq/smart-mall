package com.jsy.controller;

import com.jsy.basic.util.vo.CommonResult;
import com.jsy.service.IUserSearchHistoryService;
import com.zhsj.baseweb.support.ContextHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/userSearchHistory")
@Slf4j
@Api(tags="新-商家")
public class UserSearchHistoryController {
    @Resource
    private IUserSearchHistoryService searchHistoryService;

    @PostMapping("/addSearchHistoryByUserId")
    @ApiOperation("新增搜索记录")
    public CommonResult<Boolean> addSearchHistoryByUserId(@RequestParam("searchkey") String searchkey){
        Long userId = ContextHolder.getContext().getLoginUser().getId();
        Boolean b = searchHistoryService.addSearchHistoryByUserId(userId,searchkey);
        if (b){
            return CommonResult.ok(b);
        }else {
            return CommonResult.ok(b);
        }
    }

    @PostMapping("/delSearchHistoryByUserId")
    @ApiOperation("删除个人历史数据")
    public CommonResult delSearchHistoryByUserId(@RequestParam("searchkey") String searchkey) {
        Long userId = ContextHolder.getContext().getLoginUser().getId();
        Long b = searchHistoryService.delSearchHistoryByUserId(userId,searchkey);
        return CommonResult.ok();
    }

    @PostMapping("/getSearchHistoryByUserId")
    @ApiOperation("获取个人历史数据列表")
    public CommonResult<List<String>> getSearchHistoryByUserId() {
        Long userId = ContextHolder.getContext().getLoginUser().getId();
        List<String> list = searchHistoryService.getSearchHistoryByUserId(userId);
        return CommonResult.ok(list);
    }

    @PostMapping("/incrementScoreByUserId")
    @ApiOperation("新增一条热词搜索记录，将用户输入的热词存储下来")
    public CommonResult<Boolean> incrementScoreByUserId(@RequestParam("searchkey") String searchkey) {
        Long userId = ContextHolder.getContext().getLoginUser().getId();
        Boolean b = searchHistoryService.incrementScoreByUserId(searchkey);
        if (b){
            return CommonResult.ok(b);
        }else {
            return CommonResult.ok(b);
        }
    }

    @PostMapping("/getHotList")
    @ApiOperation("根据searchkey搜索其相关最热的前十名 (如果searchkey为null空，则返回redis存储的前十最热词条)")
    public CommonResult<List<String>> getHotList(@RequestParam("searchkey") String searchkey) {
        Long userId = ContextHolder.getContext().getLoginUser().getId();
        List<String> list = searchHistoryService.getHotList(searchkey);
        return CommonResult.ok(list);
    }

    @PostMapping("/incrementScore")
    @ApiOperation("次点击给相关词searchkey热度 +1")
    public CommonResult<Boolean> incrementScore(@RequestParam("searchkey") String searchkey) {
        Long userId = ContextHolder.getContext().getLoginUser().getId();
        Boolean b = searchHistoryService.incrementScore(searchkey);
        if (b){
            return CommonResult.ok(b);
        }else {
            return CommonResult.ok(b);
        }
    }

}
