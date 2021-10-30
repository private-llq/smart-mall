package com.jsy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsy.domain.ChatForEvaluation;
import com.jsy.dto.ChatForEvaluationDto;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yu
 * @since 2021-03-23
 */
@Component
public interface ChatForEvaluationMapper extends BaseMapper<ChatForEvaluation> {

    @Select({"<script>",
            "select *",
            "from t_chat_for_evaluation ",
            "where evaluation_uuid = #{evaluationUuid}",
            "</script>"})
    List<ChatForEvaluationDto> selectByEuid(@Param("evaluationUuid") String evaluationUuid);
}
