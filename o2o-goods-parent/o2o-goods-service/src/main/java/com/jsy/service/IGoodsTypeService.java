package com.jsy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jsy.domain.GoodsBasic;
import com.jsy.domain.GoodsType;
import com.jsy.dto.GoodsTypeDTO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lijin
 * @since 2020-11-12
 */
public interface IGoodsTypeService extends IService<GoodsType> {

    List<GoodsBasic> findGoodsByTypeId(String uuid);

    boolean deleteByUuid(String id);

    List<GoodsTypeDTO> findGoodsType(String uuid);

}
