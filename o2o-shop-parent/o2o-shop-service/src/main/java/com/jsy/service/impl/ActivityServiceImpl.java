package com.jsy.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jsy.basic.util.PageList;
import com.jsy.basic.util.exception.JSYException;
import com.jsy.basic.util.utils.CurrentUserHolder;
import com.jsy.basic.util.utils.UUIDUtils;
import com.jsy.basic.util.vo.UserDto;
import com.jsy.controller.ActivityController;
import com.jsy.domain.Activity;
import com.jsy.mapper.ActivityMapper;
import com.jsy.query.ActivityQuery;
import com.jsy.service.IActivityService;
import com.jsy.vo.ActivityVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lijin
 * @since 2020-11-19
 */
@Service
@Slf4j
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity> implements IActivityService {

    @Resource
    private ActivityMapper activityMapper;

    @Autowired
    private ActivityController activityController;


    @Override
    public PageList<Activity> selectByConditon(ActivityQuery<Activity> query) {
        log.info("店铺活动查询入参:{}", query);
        //设置搜索条件
        QueryWrapper<Activity> queryWrapper = new QueryWrapper<>();
        if (query.getQuery() != null) {
            if (StringUtils.isNotEmpty(query.getQuery().getName())) {
                queryWrapper.like("name", query.getQuery().getName());
            }
            if (query.getQuery().getBeginTime() != null) {
                queryWrapper.ge("begin_time", query.getQuery().getBeginTime());
            }
            if (query.getQuery().getEndTime() != null) {
                queryWrapper.le("end_time", query.getQuery().getEndTime());
            }
            if (query.getQuery().getDeleted() != null) {
                queryWrapper.eq("deleted", query.getQuery().getDeleted());
            }
            queryWrapper.eq("shop_uuid", query.getQuery().getShopUuid());
        } else {
            throw new JSYException(-1, "数据错误请重试");
        }
        //分页
        Page<Activity> page = new Page<>(query.getPage(), query.getRows());
        Page<Activity> activityPage = activityMapper.selectPage(page, queryWrapper);
        return new PageList<Activity>(activityPage.getTotal(), activityPage.getRecords());
    }

    @Override
    public Activity findOne(String uuid, BigDecimal money) {
        LocalDateTime time = LocalDateTime.now();
        Activity one = activityMapper.findOne(uuid, money, time);
        return one;
    }

    @Override
    public List<Activity> runActivities(String shopUuid) {
        long localTime = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
        List<Activity> activities = activityMapper.selectList(new QueryWrapper<Activity>().eq("shop_uuid", shopUuid));
        List<Activity> collect = activities.stream().filter(x -> {
            if (localTime >= x.getBeginTime().toEpochSecond(ZoneOffset.of("+8"))
                    && localTime <= x.getEndTime().toEpochSecond(ZoneOffset.of("+8"))
                    && x.getDeleted()!= 0) {
                return true;
            }
            return false;
        }).collect(Collectors.toList());
        return collect;
    }

    //开始时间和结束时间单独传
    @Override
    public void saveList(ActivityVo activityVo) {

        if (Objects.isNull(activityVo.getBeginTime()) || Objects.isNull(activityVo.getEndTime())){
            throw new JSYException(-1,"活动的开始的结束时间为必填");
        }
        if (Objects.isNull(activityVo.getShopUuid())){
            throw new JSYException(-1,"商家ID参数异常");
        }
        UserDto dto = CurrentUserHolder.getCurrentUser();
        LocalDateTime now = LocalDateTime.now();

        // 如果之前有活动 自动撤销
        activityController.revokeActivities(activityVo.getShopUuid());

        for (Activity activity : activityVo.getActivityList()) {
            if (activityVo.getBeginTime().isBefore(LocalDateTime.now()) || activityVo.getEndTime().isBefore(LocalDateTime.now())) {
                throw new JSYException(-1, "输入的活动时间错误");
            }
            activity.setBeginTime(activityVo.getBeginTime());
            activity.setEndTime(activityVo.getEndTime());
            activity.setDeleted(1);
            activity.setUserUuid(dto.getUuid());
            activity.setUuid(UUIDUtils.getUUID());
            activity.setShopUuid(activityVo.getShopUuid());
            activity.setCreatTime(now);
            activity.setName("满"+activity.getErverySum()+"减"+activity.getReduceNum());
            activityMapper.insert(activity);
        }
    }

    @Override
    public List<Activity> newestActivities(String shopUuid) {
        long localTime = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
        List<Activity> activities = activityMapper.selectList(new QueryWrapper<Activity>().eq("shop_uuid",shopUuid));

        Optional<Long> max = activities.stream().map(x -> x.getCreatTime().toEpochSecond(ZoneOffset.of("+8"))).max(Long::compareTo);
        Long maxTime;
        if (max.isPresent()){//存在
             maxTime = max.get();
        }else {
            throw new JSYException(-1,"未找到最近活动，请添加活动");
        }

        List<Activity> collect = activities.stream().filter(x -> {
            if (x.getCreatTime().toEpochSecond(ZoneOffset.of("+8")) ==maxTime) {
                return true;
            }
            return false;
        }).map(x -> {
            if (localTime >= x.getBeginTime().toEpochSecond(ZoneOffset.of("+8"))
                    && localTime <= x.getEndTime().toEpochSecond(ZoneOffset.of("+8"))
                    && x.getDeleted() != 0) {
                x.setState(1);//进行中
            }
            if (x.getDeleted() == 0) {
                x.setState(2);//已撤销
            }
            if (localTime > x.getEndTime().toEpochSecond(ZoneOffset.of("+8"))) {
                x.setState(3);//已过期
            }
            if (localTime<x.getBeginTime().toEpochSecond(ZoneOffset.of("+8"))){
                x.setState(4);//未开始
            }
            return x;
        }).collect(Collectors.toList());
        return collect;
    }

}
