package com.jsy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsy.domain.GoodsType;
import com.jsy.dto.GoodsTypeDTO;
import lombok.Data;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lijin
 * @since 2020-11-12
 */

@Component
public interface GoodsTypeMapper extends BaseMapper<GoodsType> {

    List<GoodsTypeDTO> findGoodsType(@Param("uuid") String uuid);
}
