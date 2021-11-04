package com.jsy.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jsy.basic.util.exception.JSYException;
import com.jsy.basic.util.utils.CurrentUserHolder;
import com.jsy.basic.util.utils.UUIDUtils;
import com.jsy.basic.util.vo.UserDto;
import com.jsy.domain.BusinessFeedback;
import com.jsy.domain.BusinessFeedtype;
import com.jsy.domain.BusinessFeedappend;
import com.jsy.dto.BusinessFeedbackDto;
import com.jsy.mapper.BusinessFeedappendMapper;
import com.jsy.mapper.BusinessFeedbackMapper;
import com.jsy.mapper.BusinessFeedtypeMapper;
import com.jsy.service.IBusinessFeedbackService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.jsy.vo.BusinessFeedtypeVo;
import org.apache.commons.lang.StringUtils;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yu
 * @since 2021-05-20
 */
@Service
public class BusinessFeedbackServiceImpl extends ServiceImpl<BusinessFeedbackMapper, BusinessFeedback> implements IBusinessFeedbackService {

    @Resource
    private BusinessFeedbackMapper businessFeedbackMapper;

    @Resource
    private BusinessFeedtypeMapper businessFeedtypeMapper;

    @Resource
    private BusinessFeedappendMapper businessFeedappendMapper;

    //
    @Override
    public Boolean addUserFeedback(BusinessFeedtypeVo businessFeedtypeVo) {

        //获取token信息
        UserDto dto= CurrentUserHolder.getCurrentUser();
        BusinessFeedback businessFeedback = new BusinessFeedback();
        BeanUtil.copyProperties(dto,businessFeedback);
        businessFeedback.setUserName(dto.getName());

        BeanUtil.copyProperties(businessFeedtypeVo,businessFeedback);
        businessFeedback.setOrderUuid(UUIDUtils.getUUID());
        businessFeedback.setMessageUuid(UUIDUtils.getUUID());
        businessFeedback.setState(0);//默认0  未处理

        List<String> picUrls = businessFeedtypeVo.getPicUrls();
        if(Objects.isNull(picUrls)){
            businessFeedback.setHasPicture(0);
        }else {
            businessFeedback.setHasPicture(1);
            if(picUrls.size()>3){
                throw  new JSYException(-1,"最多只能发布3张图片");
            }
        }
        if (Objects.isNull(businessFeedtypeVo.getFeedType())){
            businessFeedback.setFeedType(6);
        }
        String str = StringUtils.join(picUrls, ",");
        businessFeedback.setPicUrls(str);
        businessFeedback.setAppendType(0);//默认为未追加
        businessFeedbackMapper.insert(businessFeedback);

        return  true;

    }

    @Override
    public BusinessFeedbackDto getByOrderUuid(String order_uuid) {
        BusinessFeedbackDto businessFeedbackDto = new BusinessFeedbackDto();
        BusinessFeedback businessFeedback = businessFeedbackMapper.selectOne(new QueryWrapper<BusinessFeedback>().eq("order_uuid", order_uuid));
        BusinessFeedtype businessFeedtype = businessFeedtypeMapper.selectOne(new QueryWrapper<BusinessFeedtype>().eq("type_id", businessFeedback.getFeedType()));
        BeanUtil.copyProperties(businessFeedback,businessFeedbackDto);

        if (businessFeedback.getAppendType()==0) {
            String picUrls = businessFeedback.getPicUrls();
            List list = Arrays.asList(picUrls.split(","));
            businessFeedbackDto.setPicUrls(list);
            businessFeedbackDto.setFeed(businessFeedtype.getFeedType());
        }

      if (businessFeedback.getAppendType()==1){
          BusinessFeedappend businessFeedappend = businessFeedappendMapper.selectOne(new QueryWrapper<BusinessFeedappend>().eq("order_uuid", order_uuid));
          BeanUtil.copyProperties(businessFeedappend,businessFeedbackDto);
          if (Objects.nonNull(businessFeedappend.getPicUrls())) {
              String urls = businessFeedappend.getPicUrls();
              List list = Arrays.asList(urls.split(","));
              businessFeedbackDto.setPicUrls(list);
          }
              businessFeedbackDto.setFeed(businessFeedtype.getFeedType());
              businessFeedbackDto.setCreateTime(businessFeedback.getCreateTime());
              businessFeedbackDto.setUpdateTime(businessFeedappend.getAppendTime());
              businessFeedbackDto.setContentAppend(businessFeedappend.getContentAppend());

      }


        //System.out.println(businessFeedbackDto);
        return  businessFeedbackDto;
    }

    @Override
    public Boolean selectCount(String info_uuid) {
        List<BusinessFeedback> count = businessFeedbackMapper.selectList(new QueryWrapper<BusinessFeedback>()
                .eq("info_uuid", info_uuid)
                .ge("create_time", LocalDate.now())
                .le("create_time",LocalDate.now().plusDays(1)));
       if (count != null && count.size()<3){
           return true;
       }
        return  false;
    }

    @Override
    public Boolean selectState(String info_uuid) {
        List<BusinessFeedback> state = businessFeedbackMapper.selectList(new QueryWrapper<BusinessFeedback>()
                .eq("info_uuid", info_uuid)
                .eq("state",0));

        if(state != null && state.size()<3){
                return true;
        }
        return false;
    }

    @Override
    public Boolean selectCreateTime(String info_uuid) {
        List<BusinessFeedback> list = businessFeedbackMapper.selectList(new QueryWrapper<BusinessFeedback>().eq("info_uuid", info_uuid));
        if (list.size()>0){
            Optional<BusinessFeedback> max = list.stream().max(Comparator.comparing(x -> x.getCreateTime().toEpochSecond(ZoneOffset.of("+8"))));
            long createTime = max.get().getCreateTime().toEpochSecond(ZoneOffset.of("+8"));
            long l = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));

            if(l-createTime<=60*9){
                throw new  JSYException(-1,"10分钟以内只能添加一次，请稍后提交");
            }
        }
        return true;
    }

    }

