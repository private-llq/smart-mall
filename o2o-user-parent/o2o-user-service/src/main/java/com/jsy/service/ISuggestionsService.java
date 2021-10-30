package com.jsy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jsy.domain.Suggestions;
import com.jsy.vo.SuggestionsVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yu
 * @since 2020-12-11
 */
public interface ISuggestionsService extends IService<Suggestions> {

    void save(SuggestionsVo suggestionsvo);
}
