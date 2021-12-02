package com.jsy.mapper;

import com.jsy.domain.NewShop;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsy.dto.NewShopHotDto;
import com.jsy.query.NewShopQuery;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 新——店铺表 Mapper 接口
 * </p>
 *
 * @author yu
 * @since 2021-11-08
 */
public interface NewShopMapper extends BaseMapper<NewShop> {

    List<NewShop> mainSearch(@Param("keyword") String keyword);

    //查询地理位置里用户位置3km以内的商家
    List<NewShop> selectAddress(@Param("longitude") BigDecimal longitude, @Param("latitude") BigDecimal latitude);
    //大后台商家分页
    List<NewShop> selecctNewShopPage(@Param("shopQuery") NewShopQuery shopQuery);
//热门推荐
    List<NewShopHotDto> selectHot();
//大后台查询当月入驻商家
    Integer newShopAudit(@Param("startDate") String startDate, @Param("endDate") String endDate,@Param("state") Integer state);

}
