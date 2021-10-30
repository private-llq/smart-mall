package com.jsy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jsy.domain.BusinessHours;
import com.jsy.dto.SelectShopBusinessTimeDto;
import com.jsy.parameter.UpdateShopBusinessTimeParam;

import java.util.List;

public interface BusinessHoursService extends IService<BusinessHours> {
    //添加默认的营业时间
    public Boolean  defaultBusinessTime(String shopUuid);
    //根据uuid 查询店铺的营业时间
    public List<SelectShopBusinessTimeDto> selectShopBusinessTime(String shopUuid);
    //修改店铺的营业时间
    public Boolean updateShopBusinessTime(List<UpdateShopBusinessTimeParam> list);

}
