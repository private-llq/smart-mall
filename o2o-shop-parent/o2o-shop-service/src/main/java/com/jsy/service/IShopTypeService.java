package com.jsy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jsy.domain.ShopType;
import com.jsy.vo.ShopTypeVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lijin
 * @since 2020-11-12
 */
public interface IShopTypeService extends IService<ShopType> {
   //根据门店类型uuid查询所属类型层级系
    public String selectShopType(String uuid);


    /**************/
    List<ShopType> findByParentId(Long parentId);

    List<ShopTypeVo> getList();

}
