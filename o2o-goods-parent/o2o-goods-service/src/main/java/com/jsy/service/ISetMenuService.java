package com.jsy.service;

import com.jsy.domain.SetMenu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jsy.dto.SetMenuDto;
import com.jsy.dto.SetMenuGoodsDto;

import java.util.List;
import java.util.Map;

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
//查询套餐
    List<SetMenuDto> getSetMenulist(Long shopId);
//查询所有套餐列表
    List<SetMenuDto> getList(Long shopId,Integer state);
//通过套餐id查询套餐
Map<String,List<SetMenuGoodsDto>> getMenuId(Long setMenuId);
}
