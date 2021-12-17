package com.jsy.mapper;

import com.jsy.domain.SetMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsy.query.SetMenuQuery;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lijin
 * @since 2021-11-10
 */
@Component
public interface SetMenuMapper extends BaseMapper<SetMenu> {

    void setAllState(@Param("setMenuQuery") SetMenuQuery setMenuQuery);
    void setAllDisable(@Param("setMenuQuery") SetMenuQuery setMenuQuery);

}
