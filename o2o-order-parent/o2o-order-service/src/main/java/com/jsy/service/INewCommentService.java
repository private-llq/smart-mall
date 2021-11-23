package com.jsy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsy.domain.NewComment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jsy.dto.SelectShopCommentPageDto;
import com.jsy.dto.SelectShopCommentScoreDto;
import com.jsy.query.CreateCommentParam;
import com.jsy.query.SelectShopCommentPageParam;
import com.jsy.utils.MyPage;
import com.jsy.vo.SelectCommentAndReplyVo;

import java.util.List;

/**
 * <p>
 * 评论表 服务类
 * </p>
 *
 * @author arli
 * @since 2021-11-15
 */
public interface INewCommentService extends IService<NewComment> {
    //新增一条评论
    Boolean createComment(CreateCommentParam param);
    //查询店铺评分
    SelectShopCommentScoreDto selectShopCommentScore(Long shopId);

    Page<NewComment> selectShopCommentPage(SelectShopCommentPageParam param);
   //查询评论和商家回复
   MyPage selectCommentAndReply(SelectShopCommentPageParam param);
}
