package com.jsy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jsy.basic.util.exception.JSYException;
import com.jsy.domain.NewRefund;
import com.jsy.domain.NewReply;
import com.jsy.mapper.NewRefundMapper;
import com.jsy.mapper.NewReplyMapper;
import com.jsy.query.ReplyCommentParam;
import com.jsy.service.INewReplyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author arli
 * @since 2021-11-22
 */
@Service
public class NewReplyServiceImpl extends ServiceImpl<NewReplyMapper, NewReply> implements INewReplyService {
   @Resource
   private NewReplyMapper newReplyMapper;

    //商家回复评论
    @Override
    public Boolean replyComment(ReplyCommentParam param) {
        QueryWrapper<NewReply> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("comment_id", param.getCommentId());
        List<NewReply> newReplies = newReplyMapper.selectList(queryWrapper);
        if (newReplies.size()>0) {
            throw  new JSYException(500,"已经回复过");
        }

        NewReply entity = new NewReply();
        entity.setReply(param.getReply());
        entity.setCommentId(param.getCommentId());
        int insert = newReplyMapper.insert(entity);
        if (insert>0){
            return true;
        }
        return false;
    }

}
