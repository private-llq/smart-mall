package com.jsy.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jsy.basic.util.exception.JSYException;
import com.jsy.basic.util.utils.UUIDUtils;
import com.jsy.domain.BusinessFeedappend;
import com.jsy.mapper.BusinessFeedappendMapper;
import com.jsy.mapper.BusinessFeedbackMapper;
import com.jsy.service.IBusinessFeedappendService;
import com.jsy.vo.BusinessFeedbackAppendVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yu
 * @since 2021-05-20
 */
@Service
public class BusinessFeedappendServiceImpl extends ServiceImpl<BusinessFeedappendMapper, BusinessFeedappend> implements IBusinessFeedappendService {
    @Resource
    private BusinessFeedappendMapper businessFeedappendMapper;

    @Resource
    private  BusinessFeedbackMapper businessFeedbackMapper;

    @Override
    public Boolean append(BusinessFeedbackAppendVo businessFeedbackAppendVo) {
        BusinessFeedappend bu = businessFeedappendMapper.selectOne(new QueryWrapper<BusinessFeedappend>().eq("order_uuid", businessFeedbackAppendVo.getOrderUuid()));
        if (Objects.nonNull(bu)){
            throw  new JSYException(-1,"已追加，不能再次追加");
        }

         BusinessFeedappend businessFeedappend = new BusinessFeedappend();
         BeanUtil.copyProperties(businessFeedbackAppendVo,businessFeedappend);

        String[] picUrls = businessFeedbackAppendVo.getPicUrls();

        if(Objects.isNull(picUrls)){
            businessFeedappend.setHasPicture(0);
        }else {
            businessFeedappend.setHasPicture(1);
            if((picUrls.length)>3){
                throw  new JSYException(-1,"最多只能发布3张图片");
            }
        }

        String str = StringUtils.join(picUrls, ",");
        businessFeedappend.setPicUrls(str);
        businessFeedappend.setUuid(UUIDUtils.getUUID());
        businessFeedappend.setContentAppend(businessFeedbackAppendVo.getContentAppend());
        businessFeedappend.setOrderUuid(businessFeedbackAppendVo.getOrderUuid());
        businessFeedappendMapper.insert(businessFeedappend);
        businessFeedbackMapper.updateByOrderId(businessFeedbackAppendVo.getOrderUuid());
        return true;
    }
}
