package com.jsy.service;

import com.jsy.domain.BusinessHelp;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yu
 * @since 2021-05-26
 */
public interface IBusinessHelpService extends IService<BusinessHelp> {

    //查询帮助中心的分类
   public List<BusinessHelp> selectType();


    List<BusinessHelp> selectName(String name);
}
