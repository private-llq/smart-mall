package com.jsy.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsy.domain.Cart;
import com.jsy.domain.ShopInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lijin
 * @since 2020-11-23
 */

@Component
public interface CartMapper extends BaseMapper<Cart> {

    List<ShopInfo> getShopList(@Param("shopIds") List<String> collect1);
}
