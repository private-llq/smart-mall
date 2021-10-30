package com.jsy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsy.domain.GoodsType;
import com.jsy.dto.GoodsTypeDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lijin
 * @since 2020-11-12
 */
public interface GoodsTypeMapper extends BaseMapper<GoodsType> {

    List<GoodsTypeDTO> findGoodsType(@Param("uuid") String uuid);
}
