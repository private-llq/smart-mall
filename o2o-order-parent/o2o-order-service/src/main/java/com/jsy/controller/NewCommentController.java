package com.jsy.controller;

import com.jsy.dto.SelectShopCommentPageDto;
import com.jsy.dto.SelectShopCommentScoreDto;
import com.jsy.query.CreateCommentParam;
import com.jsy.query.SelectShopCommentPageParam;
import com.jsy.service.INewCommentService;
import com.jsy.domain.NewComment;
import com.jsy.query.NewCommentQuery;
import com.jsy.basic.util.PageList;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsy.utils.MyPage;
import com.jsy.vo.SelectCommentAndReplyVo;
import com.zhsj.baseweb.annotation.LoginIgnore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jsy.basic.util.vo.CommonResult;

import java.util.List;

@RestController
@RequestMapping("/newComment")
public class NewCommentController {
    @Autowired
    public INewCommentService newCommentService;


    /**************************************arli**********************************/
    //新增一条评论
    @RequestMapping(value = "/createComment",method =RequestMethod.POST)
    public CommonResult<Boolean> createComment(@RequestBody CreateCommentParam param) {
        Boolean b = newCommentService.createComment(param);
        return CommonResult.ok(b);
    }

    //查询店铺的评分
    @RequestMapping(value = "/selectShopCommentScore",method =RequestMethod.GET)
    public CommonResult<SelectShopCommentScoreDto> selectShopCommentScore(@RequestParam("shopId") Long shopId) {
        SelectShopCommentScoreDto value= newCommentService.selectShopCommentScore(shopId);
        return CommonResult.ok(value);
    }


   /* //分页查询店铺的评论
    @LoginIgnore
    @RequestMapping(value = "/selectShopCommentPage",method =RequestMethod.POST)
    public CommonResult<Page<NewComment>> selectShopCommentPage(@RequestBody SelectShopCommentPageParam param) {
       Page<NewComment>   values=newCommentService.selectShopCommentPage(param);
        return CommonResult.ok(values);
    }*/

    //分页查询店铺的评论and回复
   /* @LoginIgnore*/
    @RequestMapping(value = "/selectCommentAndReply",method =RequestMethod.POST)
    public CommonResult<MyPage> selectCommentAndReply(@RequestBody SelectShopCommentPageParam param) {
        MyPage values=newCommentService.selectCommentAndReply(param);
        return CommonResult.ok(values);
    }






}
