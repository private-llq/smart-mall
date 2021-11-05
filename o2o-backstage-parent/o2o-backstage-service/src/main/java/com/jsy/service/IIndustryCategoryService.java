package com.jsy.service;

import com.jsy.domain.IndustryCategory;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 大后台 行业服务分类 服务类
 * </p>
 *
 * @author Tian
 * @since 2021-11-04
 */
public interface IIndustryCategoryService extends IService<IndustryCategory> {
    //查询行业服务分类层级结构
    List<IndustryCategory> selectCategory();

//    //新增服务行业分类
//    boolean addCategory(IndustryCategoryVo categoryVo);

    //删除当前的节点 以及她的子节点
    void deleteById(Long id);
}
