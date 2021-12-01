package com.jsy.service;

import com.jsy.basic.util.PageInfo;
import com.jsy.domain.SetMenu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jsy.dto.SetMenuDto;
import com.jsy.dto.SetMenuGoodsDto;
import com.jsy.parameter.SetMenuParam;
import com.jsy.query.SetMenuQuery;

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
    void addSetMenu(SetMenuParam setMenu);
//根据id查询套餐和套餐详情
  SetMenuDto getSetMenulist(Long shopId,Long id);
//查询所有套餐上下架套餐
PageInfo<SetMenuDto> getList(SetMenuQuery setMenuQuery);
//通过套餐id查询套餐详情
Map<String,List<SetMenuGoodsDto>> getMenuId(Long setMenuId);
//查询商家所有套餐
    PageInfo<SetMenuDto> listAll(SetMenuQuery setMenuQuery);
//修改上下架
    void setState(Long id,Integer state);
//修改套餐
    void updateSetMenu(SetMenuParam setMenu);
//根据id批量查询
    List<SetMenuDto> batchIds(List<Long> ids);
}
