package com.jsy.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsy.domain.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsy.dto.CommentDTO;
import com.jsy.query.CommentQuery;
import com.jsy.query.ShopCommentQuery;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lijin
 * @since 2020-12-01
 */
@Repository
public interface CommentMapper extends BaseMapper<Comment> {

    IPage<CommentDTO> selectAll(Page page, @Param("userUuid")String uuid);

    IPage<CommentDTO> selectCommentByShop(Page<CommentDTO> page, @Param("query") ShopCommentQuery query);
}
