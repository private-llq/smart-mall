package com.jsy.service;

import com.jsy.domain.SetMenu;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lijin
 * @since 2021-11-10
 */
public interface ISetMenuService extends IService<SetMenu> {
//创建套餐
    void addSetMenu(SetMenu setMenu);
}
