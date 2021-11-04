package com.jsy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jsy.domain.BusinessFeedappend;
import com.jsy.vo.BusinessFeedbackAppendVo;


public interface IBusinessFeedappendService extends IService<BusinessFeedappend> {
    //追加
    Boolean append(BusinessFeedbackAppendVo businessFeedbackAppendVo);


    //查看追加的进度
    //Object getByUuid(String orderUuid,Integer type);
}
