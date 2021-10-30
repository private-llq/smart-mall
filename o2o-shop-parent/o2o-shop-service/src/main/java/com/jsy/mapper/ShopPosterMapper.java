package com.jsy.mapper;

import com.jsy.domain.ShopPoster;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsy.vo.SortVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yu
 * @since 2021-03-27
 */
public interface ShopPosterMapper extends BaseMapper<ShopPoster> {


    ShopPoster getPoster(@Param("shopUuid") String shopUuid);

    void setSort(@Param("shopUuid")String shopUuid,@Param("listParam") List<SortVo> sortVo);

    void setSort2(@Param("shopUuid")String shopUuid,@Param("listParam2")List<SortVo> sortVo);

}
