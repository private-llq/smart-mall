package com.jsy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jsy.basic.util.PageList;
import com.jsy.domain.Activity;
import com.jsy.dto.ActivityDTO;
import com.jsy.query.ActivityQuery;
import com.jsy.vo.ActivityVo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lijin
 * @since 2020-11-19
 */
public interface IActivityService extends IService<Activity> {


    PageList<Activity> selectByConditon(ActivityQuery<Activity> query);


    Activity findOne(String uuid,BigDecimal money);

    List<Activity> runActivities(String shopUuid);

    void saveList(ActivityVo activityVo);

    List<Activity> newestActivities(String shopUuid);
}
