package com.jsy.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jsy.domain.Timetable;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yu
 * @since 2021-02-20
 */
@Component
public interface TimetableMapper extends BaseMapper<Timetable> {

    @Delete("delete from t_timetable where uuid = #{uuid}")
    void deleteOne(String uuid);

    @Update("update t_timetable set state = #{status} where uuid = #{uuid}")
    void updateStatus(@Param("uuid") String uuid, @Param("status") Long status);
}
