package com.jsy.controller;
import com.jsy.query.ReplyCommentParam;
import com.jsy.service.INewReplyService;
import com.jsy.domain.NewReply;
import com.jsy.query.NewReplyQuery;
import com.jsy.basic.util.PageList;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jsy.basic.util.vo.CommonResult;
import java.util.List;

@RestController
@RequestMapping("/newReply")
public class NewReplyController {
    @Autowired
    public INewReplyService newReplyService;

    /**
    * 保存和修改公用的
    * @param newReply  传递的实体
    * @return Ajaxresult转换结果
    */
    @ApiOperation("商家回复评论")
    @RequestMapping (value="/replyComment",method = RequestMethod.POST)
    public CommonResult<Boolean> replyComment(@RequestBody ReplyCommentParam param){
        Boolean    b= newReplyService.replyComment(param);
        if(b){
            return new CommonResult<>(200, "操作成功",b);
        }
        return new CommonResult<>(500, "操作失败",b);
    }


}
