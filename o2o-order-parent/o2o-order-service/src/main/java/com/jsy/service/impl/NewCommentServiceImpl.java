package com.jsy.service.impl;

import com.jsy.domain.NewComment;
import com.jsy.mapper.NewCommentMapper;
import com.jsy.service.INewCommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 评论表 服务实现类
 * </p>
 *
 * @author arli
 * @since 2021-11-15
 */
@Service
public class NewCommentServiceImpl extends ServiceImpl<NewCommentMapper, NewComment> implements INewCommentService {

}
