package com.jsy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsy.domain.ShopApplySeckill;
import com.jsy.dto.ShopApplySeckillDTO;
import com.jsy.query.ShopApplySeckillQuery;
import com.jsy.query.UserApplySeckillQuery;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yu
 * @since 2020-12-19
 */
@Component
public interface ShopApplySeckillMapper extends BaseMapper<ShopApplySeckill> {

    IPage<ShopApplySeckillDTO> applicationPageList(Page page, @Param("query") ShopApplySeckillQuery query);

    IPage<ShopApplySeckillDTO> userApplicationPageList(Page<ShopApplySeckill> shopApplySeckillPage, @Param("userQuery") UserApplySeckillQuery userQuery);

    List<ShopApplySeckillDTO> activityList();

    boolean updateStock(@Param("shopUuid") String shopUuid, @Param("goodsUuid") String goodsUuid, @Param("cartNum") Integer cartNum);
}
