package com.jsy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsy.domain.UserRedpacket;
import com.jsy.dto.UserRedpacketDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lijin
 * @since 2020-11-16
 */
public interface UserRedpacketMapper extends BaseMapper<UserRedpacket> {

    List<UserRedpacketDTO> queryUsableRedpacket(@Param("map") Map<String, Object> map);

}
