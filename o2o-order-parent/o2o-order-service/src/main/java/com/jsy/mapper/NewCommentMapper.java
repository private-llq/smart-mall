package com.jsy.mapper;

import com.jsy.domain.NewComment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsy.vo.SelectCommentAndReplyVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * <p>
 * 评论表 Mapper 接口
 * </p>
 *
 * @author arli
 * @since 2021-11-15
 */
public interface NewCommentMapper extends BaseMapper<NewComment> {


    public List<SelectCommentAndReplyVo> selectCommentAndReply(@Param("current")Integer current,
                                                               @Param("amount")Integer amount,
                                                               @Param("shopId")Long shopId,
                                                               @Param("nots") Integer nots                           );


   public Integer selectCommentAndReplyTotal(@Param("shopId")Long shopId,
                                             @Param("nots") Integer nots);


    List<SelectCommentAndReplyVo> shopSelectCommentAndReply(
            @Param("current")Integer index,
            @Param("amount")Integer end,
            @Param("shopId")Long shopId,
            @Param("isReply")int isReply);

    Integer shopSelectCommentAndReplyTotal(
            @Param("shopId")Long shopId,
            @Param("isReply")int isReply);
}
