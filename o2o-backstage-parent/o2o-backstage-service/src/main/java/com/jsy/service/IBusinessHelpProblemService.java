package com.jsy.service;

import com.jsy.domain.BusinessHelpProblem;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jsy.dto.BusinessHelpProblemDto;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yu
 * @since 2021-05-27
 */
public interface IBusinessHelpProblemService extends IService<BusinessHelpProblem> {
    //点击是否帮助
    void add(BusinessHelpProblemDto businessHelpProblemDto);
}
