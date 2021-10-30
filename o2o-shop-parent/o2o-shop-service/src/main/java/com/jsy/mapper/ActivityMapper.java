package com.jsy.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsy.domain.Activity;
import org.apache.ibatis.annotations.Param;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lijin
 * @since 2020-11-19
 */
public interface ActivityMapper extends BaseMapper<Activity> {

    Activity findOne(@Param("uuid")String uuid,@Param("money") BigDecimal money, @Param("time")LocalDateTime dateTime);
}
