package com.jsy.service;

import com.jsy.domain.NewReply;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jsy.query.ReplyCommentParam;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author arli
 * @since 2021-11-22
 */
public interface INewReplyService extends IService<NewReply> {
    //商家回复评论
    Boolean replyComment(ReplyCommentParam param);
}
