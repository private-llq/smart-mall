package com.jsy.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jsy.domain.Suggestions;
import com.jsy.mapper.SuggestionsMapper;
import com.jsy.service.ISuggestionsService;
import com.jsy.vo.SuggestionsVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yu
 * @since 2020-12-11
 */
@Service
public class SuggestionsServiceImpl extends ServiceImpl<SuggestionsMapper, Suggestions> implements ISuggestionsService {

    @Autowired
    private SuggestionsMapper suggestionsMapper;

    @Override
    public void save(SuggestionsVo suggestionsvo) {
        Suggestions suggestions = new Suggestions();
        BeanUtils.copyProperties(suggestionsvo,suggestions);
        suggestions.setCreateTime(LocalDateTime.now());
        suggestions.setFinishTime(null);
        suggestions.setIsdeal(0);
        suggestions.setDealmanUuid(null);
        suggestionsMapper.insert(suggestions);
    }
}
