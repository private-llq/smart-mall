package com.jsy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jsy.basic.util.utils.UUIDUtils;
import com.jsy.domain.ChatForEvaluation;
import com.jsy.dto.ChatForEvaluationDto;
import com.jsy.mapper.ChatForEvaluationMapper;
import com.jsy.service.IChatForEvaluationService;
import com.jsy.vo.ChatForEvaluationVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yu
 * @since 2021-03-23
 */
@Service
public class ChatForEvaluationServiceImpl extends ServiceImpl<ChatForEvaluationMapper, ChatForEvaluation> implements IChatForEvaluationService {

    @Autowired
    private ChatForEvaluationMapper chatForEvaluationMapper;

    @Override
    public int save(ChatForEvaluationVo chatForEvaluationVo) {
        ChatForEvaluation chatForEvaluation = new ChatForEvaluation();
        BeanUtils.copyProperties(chatForEvaluationVo,chatForEvaluation);
        chatForEvaluation.setUuid(UUIDUtils.getUUID());
        return this.save(chatForEvaluation)?1:0;
    }

    @Override
    public int chatWith(ChatForEvaluationVo chatForEvaluationVo) {
        ChatForEvaluation chatForEvaluation = new ChatForEvaluation();
        BeanUtils.copyProperties(chatForEvaluationVo,chatForEvaluation);
        chatForEvaluation.setUuid(UUIDUtils.getUUID());
        return this.save(chatForEvaluation)?1:0;
    }
    /**
     * @return java.util.List<com.jsy.dto.ChatForEvaluationDto>
     * @Author 国民探花
     * @Description
     * @Date 2021-04-23 9:39
     * @Param [evaluationUuid]
     **/
    @Override
    public List<ChatForEvaluationDto> selectChatList(String evaluationUuid) {
        //ChatForEvaluation chatForEvaluation = this.getOne(new QueryWrapper<ChatForEvaluation>().eq("evaluation_uuid", evaluationUuid), false);

        List<ChatForEvaluationDto> list = chatForEvaluationMapper.selectByEuid(evaluationUuid);
        Map<String,List<ChatForEvaluationDto>> map = new HashMap<>();

        for (ChatForEvaluationDto chatForEvaluationDto :list){
            //评论及回复只有二级
            String uid = chatForEvaluationDto.getUuid();
            String parentId = chatForEvaluationDto.getParentId();
            //给子级的list添加数据
            if (StringUtils.isNotBlank(parentId)) {
                List<ChatForEvaluationDto> mapList = map.get(parentId);
                if (!map.containsKey(parentId)){
                    mapList = new ArrayList<>();
                }
                System.out.println(parentId);
                System.out.println(map.containsKey(parentId));
                System.out.println(chatForEvaluationDto);
                System.out.println(mapList);
                map.put(parentId,mapList);
            }
        }

        list.forEach(
                (e->e.setList(map.get(e.getId()))
                ));

        return list;
    }
}
