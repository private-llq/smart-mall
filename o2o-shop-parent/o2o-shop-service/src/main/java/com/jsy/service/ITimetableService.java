package com.jsy.service;

import com.jsy.basic.util.utils.PagerUtils;
import com.jsy.domain.Timetable;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jsy.query.TimetableQuery;

import java.sql.Time;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yu
 * @since 2021-02-20
 */
public interface ITimetableService extends IService<Timetable> {

    void add(Timetable timetable);

    void deleteOne(String uuid);

    void updateStatus(String uuid, Long status);

    PagerUtils<Timetable> selectByConditon(TimetableQuery query);

    List<Timetable> selectOk();
}
