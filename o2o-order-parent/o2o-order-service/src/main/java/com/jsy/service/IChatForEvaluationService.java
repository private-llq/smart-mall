package com.jsy.service;

import com.jsy.domain.ChatForEvaluation;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jsy.dto.ChatForEvaluationDto;
import com.jsy.vo.ChatForEvaluationVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yu
 * @since 2021-03-23
 */
public interface IChatForEvaluationService extends IService<ChatForEvaluation> {

    int save(ChatForEvaluationVo chatForEvaluationVo);

    int chatWith(ChatForEvaluationVo chatForEvaluationVo);

    List<ChatForEvaluationDto> selectChatList(String evaluationUuid);
}
