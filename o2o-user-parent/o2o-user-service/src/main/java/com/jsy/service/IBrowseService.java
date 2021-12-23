package com.jsy.service;

import com.jsy.domain.Browse;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yu
 * @since 2021-11-22
 */
public interface IBrowseService extends IService<Browse> {
//删除浏览记录
    void del(Long id);
//批量删除
    void delList(List<Long> stringList);
}
