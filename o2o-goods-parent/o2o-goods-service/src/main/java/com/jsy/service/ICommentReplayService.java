package com.jsy.service;

import com.jsy.domain.CommentReplay;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jsy.dto.CommentReplayDTO;
import com.jsy.vo.CommentReplayVo;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author lijin
 * @since 2021-03-16
 */
public interface ICommentReplayService extends IService<CommentReplay> {

    int save(CommentReplayVo commentReplayVo);

    int delete(String uuid);

    CommentReplayDTO selectOne(String uuid);
}
