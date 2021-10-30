package com.jsy.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jsy.basic.util.PageList;
import com.jsy.basic.util.exception.JSYException;
import com.jsy.basic.util.utils.UUIDUtils;
import com.jsy.domain.PlatformActivity;
import com.jsy.mapper.PlatformActivityMapper;
import com.jsy.query.PlatformActivityQuery;
import com.jsy.service.IPlatformActivityService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.time.LocalDateTime;
/**
 * <p>
 *  服务实现类
 * </p>
 * @author lijin
 * @since 2020-11-20
 */
@Service
@Slf4j
public class PlatformActivityServiceImpl extends ServiceImpl<PlatformActivityMapper, PlatformActivity> implements IPlatformActivityService {

    @Resource
    private PlatformActivityMapper platformActivityMapper;

    @Override
    public Integer delete(String uuid) {
        PlatformActivity activity = new PlatformActivity();
        activity.setDeleted(0);
        return platformActivityMapper.update(activity,new QueryWrapper<PlatformActivity>().eq("uuid",uuid));
    }

    @Override
    public PageList<PlatformActivity> selectByConditon(PlatformActivityQuery<PlatformActivity> activity) {
        log.info("入参:{}",activity);
        //设置搜索条件
        QueryWrapper<PlatformActivity> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(activity.getQuery().getName())) {
            queryWrapper.like("name",activity.getQuery().getName());
        }
        if (activity.getQuery().getBeginTime()!=null) {
            queryWrapper.ge("begin_time",activity.getQuery().getBeginTime());
        }
        if (activity.getQuery().getEndTime() != null) {
            queryWrapper.le("end_time",activity.getQuery().getEndTime());
        }
        if (activity.getQuery().getDeleted() != null) {
            queryWrapper.eq("deleted",activity.getQuery().getDeleted());
        }
        queryWrapper.eq("manager_uuid",activity.getQuery().getManagerUuid());
        //分页
        Page<PlatformActivity> page = new Page<>(activity.getPage(),activity.getRows());
        Page<PlatformActivity> selectPage = platformActivityMapper.selectPage(page, queryWrapper);

        return new PageList<PlatformActivity>(selectPage.getTotal(),selectPage.getRecords());
    }

    @Override
    public Integer saveAndUpdate(PlatformActivity platformActivity) {
        log.info("传入的时间:{}" , platformActivity.getBeginTime());
        if (platformActivity.getId() != null) {
            //进行修改
            return platformActivityMapper.update(platformActivity,new QueryWrapper<PlatformActivity>().eq("uuid",platformActivity.getUuid()));
        }
        //新增
        if (platformActivity.getBeginTime().isBefore(LocalDateTime.now()) || platformActivity.getEndTime().isBefore(LocalDateTime.now())) {
            throw new JSYException(-1,"输入的活动时间错误");
        }
        platformActivity.setUuid(UUIDUtils.getUUID());
        return platformActivityMapper.insert(platformActivity);
    }

    @Override
    public PlatformActivity getByUuid(String uuid) {
        return baseMapper.selectOne(new QueryWrapper<PlatformActivity>().eq("uuid",uuid));
    }
}
