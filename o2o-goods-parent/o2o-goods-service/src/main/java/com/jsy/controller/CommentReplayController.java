package com.jsy.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsy.basic.util.PageList;
import com.jsy.basic.util.exception.JSYError;
import com.jsy.basic.util.utils.CurrentUserHolder;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.basic.util.vo.UserEntity;
import com.jsy.domain.CommentReplay;
import com.jsy.query.CommentReplayQuery;
import com.jsy.service.ICommentReplayService;
import com.jsy.vo.CommentReplayVo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/commentReplay")
@Api("商城会员")
public class CommentReplayController {

    @Autowired
    public ICommentReplayService commentReplayService;

    /**
    * 保存和修改公用的
    * @param commentReplayVo  传递的实体
    * @return
    */
    @RequestMapping(value="/save",method= RequestMethod.POST)
    public CommonResult save(@RequestBody CommentReplayVo commentReplayVo){

        UserEntity userEntity = CurrentUserHolder.getUserEntity();
        commentReplayVo.setUuid(userEntity.getUid());
        int i = commentReplayService.save(commentReplayVo);
        if (i==0){
            return CommonResult.error(JSYError.INTERNAL.getCode(),"购买会员失败");
        }
        return CommonResult.ok();
    }

    /**
    * 删除对象信息
    * @param uuid
    * @return
    */
    @RequestMapping(value="/delete/{uuid}",method=RequestMethod.DELETE)
    public CommonResult delete(@PathVariable("uuid") String uuid){
        int i = commentReplayService.delete(uuid);
        if (i==0){
            return CommonResult.error(JSYError.INTERNAL.getCode(),"删除失败");
        }
        return CommonResult.ok();
    }

    //获取
    @RequestMapping(value = "/get/{uuid}",method = RequestMethod.GET)
    public CommonResult get(@PathVariable("uuid")String uuid) {
        return CommonResult.ok(commentReplayService.selectOne(uuid));
    }

    /**
        * 分页查询数据
        *
        * @param query 查询对象
        * @return PageList 分页对象
        */
    @RequestMapping(value = "/pagelist",method = RequestMethod.POST)
    public CommonResult json(@RequestBody CommentReplayQuery query){
        QueryWrapper<CommentReplay> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(query.getCreateTime1()!=null,"create_time",query.getCreateTime1())
                .eq(query.getCreateTime2()!=null,"create_time",query.getCreateTime2());
        Page<CommentReplay> page = new Page<CommentReplay>(query.getPage(),query.getRows());
        page = commentReplayService.page(page,queryWrapper);
        return CommonResult.ok(new PageList<CommentReplay>(page.getTotal(),page.getRecords()));
    }
}
