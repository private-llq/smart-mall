package com.jsy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jsy.domain.CommentReplay;
import com.jsy.dto.CommentReplayDTO;
import com.jsy.mapper.CommentReplayMapper;
import com.jsy.service.ICommentReplayService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jsy.vo.CommentReplayVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author lijin
 * @since 2021-03-16
 */
@Service
public class CommentReplayServiceImpl extends ServiceImpl<CommentReplayMapper, CommentReplay> implements ICommentReplayService {


    @Override
    public int save(CommentReplayVo commentReplayVo) {
        CommentReplay commentReplay = new CommentReplay();
        BeanUtils.copyProperties(commentReplayVo,commentReplay);
        return this.save(commentReplay)? 1:0;
    }


    @Override
    public int delete(String uuid) {
        boolean b = this.remove(new QueryWrapper<CommentReplay>().eq("uuid", uuid));
        return b? 1:0;
    }

    @Override
    public CommentReplayDTO selectOne(String uuid) {
        CommentReplay commentReplay = this.getOne(new QueryWrapper<CommentReplay>().eq("uuid", uuid));
        CommentReplayDTO commentReplayDTO = new CommentReplayDTO();
        BeanUtils.copyProperties(commentReplay,commentReplayDTO);
        return commentReplayDTO;
    }
}
