package com.jsy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jsy.domain.GoodsSpec;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lijin
 * @since 2020-11-12
 */
public interface IGoodsSpecService extends IService<GoodsSpec> {
   //根据商品uuid查询正常的商品规格
   public List<GoodsSpec> getByGuuid(String uuid);

    //根据规格的id查询对应的商品的上下架状态
    public Integer  selectGoodStatu(String specUUID);
}
