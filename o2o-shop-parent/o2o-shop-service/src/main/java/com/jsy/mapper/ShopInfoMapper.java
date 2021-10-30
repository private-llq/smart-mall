package com.jsy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsy.DO.ShopQueryDO;
import com.jsy.DO.ShopStarDo;
import com.jsy.basic.util.utils.PagerUtils;
import com.jsy.domain.ShopInfo;
import com.jsy.dto.GoodsOverViewDTO;
import com.jsy.dto.ShopActiveDTO;
import com.jsy.dto.ShopInfoDTO;
import com.jsy.dto.ShopQueryDTO;
import com.jsy.query.ShopInfoQuery;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lijin
 * @since 2020-11-12
 */
@Repository
public interface ShopInfoMapper extends BaseMapper<ShopInfo> {
    //查询拥有者已删除的店铺
@Select(" SELECT si.uuid,si.`status`,si.business_status ,si.shop_name,si.address_detail FROM t_shop_info as si WHERE  si.deleted=1 AND si.owner_uuid=#{shopUuid};")
    List<ShopInfo> selectDeletedShopList(String shopUuid);
    /************************************/

    List<ShopQueryDO> selectByConditon(@Param("doublelist") Map<String, Double> doubles,@Param("qurey") ShopInfoQuery shopInfoQuery);


    ShopInfoDTO selectShop(String uuid);

    ShopStarDo selectShopStar(@Param("shopUuid") String shopUuid);

    String selectShopStatus(String LoginPhone);

    List<ShopQueryDTO> selectOrderByStar(@Param("doublelist") Map<String,Double> map);

    List<ShopActiveDTO> selectByActive(@Param("doublelist") Map<String, Double> doubles);

    IPage<ShopQueryDTO> findAll(Page<ShopQueryDTO> page,@Param("doublelist")Map<String, Double> doubles,@Param("query")ShopInfoQuery shopInfoQuery);

    GoodsOverViewDTO overview(String uuid);
}
