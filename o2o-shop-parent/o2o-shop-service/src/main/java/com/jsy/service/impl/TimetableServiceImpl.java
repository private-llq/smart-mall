package com.jsy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsy.basic.util.exception.JSYException;
import com.jsy.basic.util.utils.PagerUtils;
import com.jsy.basic.util.utils.UUIDUtils;
import com.jsy.domain.Timetable;
import com.jsy.mapper.TimetableMapper;
import com.jsy.query.TimetableQuery;
import com.jsy.service.ITimetableService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.alibaba.druid.sql.visitor.SQLEvalVisitorUtils.eq;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yu
 * @since 2021-02-20
 */
@Service
public class TimetableServiceImpl extends ServiceImpl<TimetableMapper, Timetable> implements ITimetableService {

    /**
     * 新增一个时间端
     * @param timetable
     */
    @Override
    public void add(Timetable timetable) {
        String uuid = UUIDUtils.getUUID();
        timetable.setUuid(uuid);
        if (timetable.getState() == 2) {
            Integer state = baseMapper.selectCount(new QueryWrapper<Timetable>().eq("state", 2));
            if (state > 0) {
                throw new JSYException(-1,"结束时间只能存在一个");
            }
            timetable.setEndTimeLong(LocalDateTime.of(LocalDate.now(),timetable.getTime()));
        }
        int insert = baseMapper.insert(timetable);
    }

    /**
     * 根据uuid删除一个时间 实际删除
     * @param uuid
     */
    @Override
    public void deleteOne(String uuid) {
        baseMapper.deleteOne(uuid);
    }

    /**
     * 根据uuid修改一个时间的状态
     * @param uuid
     * @param status 修改的状态
     */
    @Override
    public void updateStatus(String uuid, Long status) {
        baseMapper.updateStatus(uuid,status);
    }

    /**
     * 根据条件 分页 查看时间
     * @return
     */
    @Override
    public PagerUtils<Timetable> selectByConditon(TimetableQuery query) {
        String keyword = query.getKeyword();
        QueryWrapper<Timetable> queryWrapper = null;
        if (!StringUtils.isEmpty(keyword)) {
            queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("state",keyword);
        }

        Page<Timetable> page = new Page<>(query.getPage(),query.getRows());
        Page<Timetable> selectPage = baseMapper.selectPage(page, queryWrapper);

        //排序 从小到大
        List<Timetable> timetableList = selectPage.getRecords()
                .stream()
                .sorted(Comparator.comparing(Timetable::getTime))
                .collect(Collectors.toList());

        return new PagerUtils<Timetable>((int) selectPage.getTotal(),timetableList);

    }

    /**
     * 查询当前可以使用的时间
     * @param // 可以使用的状态 1
     * @return
     */
    @Override
    public List<Timetable> selectOk() {
        int status = 1;
        List<Timetable> timetableList = baseMapper.selectList(new QueryWrapper<Timetable>().eq("state", status));
        //根据时间排序 从小到大
        List<Timetable> list = timetableList.stream().sorted(Comparator.comparing(Timetable::getTime)).collect(Collectors.toList());

        return list;
    }
}
