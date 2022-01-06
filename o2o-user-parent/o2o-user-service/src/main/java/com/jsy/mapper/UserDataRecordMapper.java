package com.jsy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsy.domain.UserDataRecord;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 用户健康信息档案 Mapper 接口
 * </p>
 *
 * @author yu
 * @since 2022-01-04
 */

@Component
public interface UserDataRecordMapper extends BaseMapper<UserDataRecord> {

    List<UserDataRecord> matchTheUser();

}
