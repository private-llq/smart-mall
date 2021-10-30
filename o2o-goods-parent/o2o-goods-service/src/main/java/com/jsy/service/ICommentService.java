package com.jsy.service;

import com.jsy.basic.util.utils.PagerUtils;
import com.jsy.domain.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jsy.dto.CommentDTO;
import com.jsy.query.CommentQuery;
import com.jsy.query.ShopCommentQuery;
import com.jsy.vo.CommentVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lijin
 * @since 2020-12-01
 */
public interface ICommentService extends IService<Comment> {

    int addComment(CommentVO comment);

    int deleteById(String uuid);

    //查看其他用户
    PagerUtils<CommentDTO> findOneAll(CommentQuery query);

    //商家查看所有评论
    PagerUtils<CommentDTO> getList(ShopCommentQuery query);
}
