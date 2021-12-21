package com.jsy.mapper;

import com.jsy.domain.Browse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yu
 * @since 2021-11-22
 */
public interface BrowseMapper extends BaseMapper<Browse> {
    //彻底删除浏览记录
    Boolean deleteBrowse(@Param("id") Long id);
    //批量删除
    void delList(@Param("userId") Long userId, @Param("stringList") List<Long> stringList);
}
