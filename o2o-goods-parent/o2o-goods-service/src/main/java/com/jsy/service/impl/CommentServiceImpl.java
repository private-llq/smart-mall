package com.jsy.service.impl;

import cn.hutool.Hutool;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsy.basic.util.exception.JSYException;
import com.jsy.basic.util.utils.CurrentUserHolder;
import com.jsy.basic.util.utils.PagerUtils;
import com.jsy.basic.util.utils.UUIDUtils;
import com.jsy.basic.util.vo.UserDto;
import com.jsy.client.ShopClient;
import com.jsy.domain.Comment;
import com.jsy.dto.CommentDTO;
import com.jsy.mapper.CommentMapper;
import com.jsy.query.CommentQuery;
import com.jsy.query.ShopCommentQuery;
import com.jsy.service.ICommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jsy.vo.CommentVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lijin
 * @since 2020-12-01
 */
@Service
@Slf4j
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Resource
    private ShopClient shopClient;

    @Override
    @Transactional
    public int addComment(CommentVO comment) {
        Comment userComment = new Comment();
        BeanUtils.copyProperties(comment,userComment);
        userComment.setUuid(UUIDUtils.getUUID()                                                                            );

        UserDto dto = CurrentUserHolder.getCurrentUser();
        //设置用户昵称
        userComment.setUserNickName(dto.getName());
        //携带头像
        userComment.setUserUuid(dto.getUuid());
//        userComment.setUserHead(dto.getHeadSculpture());
        int insert = commentMapper.insert(userComment);

        //修改店铺评价
        shopClient.updateShopStar(comment.getShopUuid(),comment.getOverallEvaluation().doubleValue());

        return insert;
    }

    @Override
    public int deleteById(String uuid) {
        UserDto user = CurrentUserHolder.getCurrentUser();
        Comment comment = commentMapper.selectOne(new QueryWrapper<Comment>().eq("uuid",uuid));
        if (user.getUuid() != comment.getUserUuid()) {
            throw new JSYException(-1,"不合法操作");
        }

        int i = commentMapper.delete(new QueryWrapper<Comment>().eq("uuid",uuid));

        return i;
    }

    @Override
    public PagerUtils<CommentDTO> findOneAll(CommentQuery query) {
        log.info("查看评论入参:{}",query.getUuid());
        UserDto dto = CurrentUserHolder.getCurrentUser();
        if (Objects.isNull(dto)) {
            throw new JSYException(-1,"请登录后查看");
        }
        //uuid为空查看自己，uuid有查看其他人的
        if (StringUtils.isEmpty(query.getUuid())) {
            query.setUuid(dto.getUuid());
        }
        IPage<CommentDTO> all = commentMapper.selectAll(new Page(query.getPage(), query.getRows()), query.getUuid());
        PagerUtils<CommentDTO> pagerUtils = new PagerUtils<>();
        pagerUtils.setTotal((int)all.getTotal());
        pagerUtils.setRecords(all.getRecords());
        return pagerUtils;
    }

    @Override
    public PagerUtils<CommentDTO> getList(ShopCommentQuery query) {

        UserDto dto = CurrentUserHolder.getCurrentUser();
        if (Objects.isNull(dto)) {
            throw new JSYException(-1,"登录后查看");
        }
        query.setShopUuid(dto.getRelationUuid());

        Page<CommentDTO> page = new Page<>(query.getPage(), query.getRows());
        IPage<CommentDTO> shop = commentMapper.selectCommentByShop(page, query);

        PagerUtils<CommentDTO> pagerUtils = new PagerUtils<>();
        pagerUtils.setTotal((int)shop.getTotal());
        pagerUtils.setRecords(shop.getRecords());

        return pagerUtils;

    }
}
