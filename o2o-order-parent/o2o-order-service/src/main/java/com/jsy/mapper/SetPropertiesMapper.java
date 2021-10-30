package com.jsy.mapper;

import com.jsy.domain.SetProperties;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yu
 * @since 2021-02-24
 */
public interface SetPropertiesMapper extends BaseMapper<SetProperties> {


    @Select("")
    SetProperties selectByModule(String module);
}
