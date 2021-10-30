package com.jsy.controller;

import com.jsy.basic.util.utils.PagerUtils;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.dto.CommentDTO;
import com.jsy.query.CommentQuery;
import com.jsy.query.ShopCommentQuery;
import com.jsy.service.ICommentService;
import com.jsy.vo.CommentVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
@Api(tags = "商品评论controller")
public class CommentController {
    @Autowired
    public ICommentService commentService;
     /**  
      * @return
      * @Author lxr
      * @Description 新增评论
      * @Date 2020/12/1 11:18 
      * @Param
      **/
    @ApiOperation("新增商品评论")
    @RequestMapping(value="/save",method= RequestMethod.POST)
    public CommonResult<Boolean> save(@RequestBody CommentVO comment){
        commentService.addComment(comment);
        return CommonResult.ok();
    }
    /**
    * 删除个人评论
    * @param uuid
    * @return
    */
    @ApiOperation("根据评论id 删除评论")
    @RequestMapping(value="/{uuid}",method=RequestMethod.DELETE)
    public CommonResult<Boolean> delete(@PathVariable("uuid") String uuid){
        commentService.deleteById(uuid);
        return CommonResult.ok();
    }
    //获取
    @ApiOperation("[登录用户]分页展示评论")
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    public CommonResult<PagerUtils<CommentDTO>> getList(@ApiParam("分页接收对象")@RequestBody CommentQuery query) {
        return CommonResult.ok(commentService.findOneAll(query));
    }
    //查询店铺的评论
    @ApiOperation("[商家]查看店铺的评论")
    @PostMapping("/shop/list")
    public CommonResult<PagerUtils<CommentDTO>> getList(@RequestBody ShopCommentQuery query) {
        return CommonResult.ok(commentService.getList(query));
    }
}
