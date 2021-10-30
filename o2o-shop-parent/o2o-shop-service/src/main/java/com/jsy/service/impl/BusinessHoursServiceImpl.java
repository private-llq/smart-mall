package com.jsy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jsy.basic.util.utils.UUIDUtils;
import com.jsy.domain.BusinessHours;
import com.jsy.dto.SelectShopBusinessTimeDto;
import com.jsy.mapper.BusinessHoursMapper;
import com.jsy.parameter.UpdateShopBusinessTimeParam;
import com.jsy.service.BusinessHoursService;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

@Service
public class BusinessHoursServiceImpl extends ServiceImpl<BusinessHoursMapper, BusinessHours> implements BusinessHoursService {

    @Resource
    private BusinessHoursMapper businessHoursMapper;

    //添加默认的营业时间
    @Override
    @Transactional
    public Boolean defaultBusinessTime(String shopUuid) {
        for (int i = 1; i <=7; i++) {
            BusinessHours businessHours = new BusinessHours();
            businessHours.setUuid(UUIDUtils.getUUID());
            businessHours.setWeek(i);
            businessHours.setShopUuid(shopUuid);
            businessHours.setStartTime( Time.valueOf("09:00:00"));
            businessHours.setEndTime( Time.valueOf("22:00:00"));
            businessHours.setStatus(1);
            businessHoursMapper.insert(businessHours);
        }
        return true;
    }

    //查询店铺的营业时间
    @Override
    public List<SelectShopBusinessTimeDto> selectShopBusinessTime(String shopUuid) {
        List<BusinessHours> bus = businessHoursMapper.selectList(new QueryWrapper<BusinessHours>().eq("shop_uuid", shopUuid));

        List<SelectShopBusinessTimeDto> selectShopBusinessTimeDtos=new ArrayList<>();
        for (BusinessHours businessHours : bus) {
            SelectShopBusinessTimeDto shopBusinessTimeDto =new SelectShopBusinessTimeDto();
            BeanUtils.copyProperties(businessHours,shopBusinessTimeDto);
            selectShopBusinessTimeDtos.add(shopBusinessTimeDto);
        }
        return selectShopBusinessTimeDtos;
    }
    //修改店铺的营业时间
    @Override
    @Transactional
    public Boolean updateShopBusinessTime(List<UpdateShopBusinessTimeParam> list) {
        for (UpdateShopBusinessTimeParam up : list) {
            BusinessHours businessHours=new BusinessHours();
            BeanUtils.copyProperties(up,businessHours);
            businessHours.setStartTime(Time.valueOf(up.getStartTime()));
            businessHours.setEndTime(Time.valueOf(up.getEndTime()));
            int uuid = businessHoursMapper.update(businessHours, new UpdateWrapper<BusinessHours>().eq("uuid", up.getUuid()));
        }
        return true;
    }

}
