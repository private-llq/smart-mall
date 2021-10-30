package com.jsy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jsy.domain.GoodsSpec;
import com.jsy.mapper.GoodsSpecMapper;
import com.jsy.service.IGoodsSpecService;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lijin
 * @since 2020-11-12
 */
@Service
public class     GoodsSpecServiceImpl extends ServiceImpl<GoodsSpecMapper, GoodsSpec> implements IGoodsSpecService {

    @Resource
    private GoodsSpecMapper goodsSpecMapper;

    //根据商品id查询状态正常的商品规格
    @Override
    public List<GoodsSpec> getByGuuid(String uuid) {
        return goodsSpecMapper.selectList(new QueryWrapper<GoodsSpec>().eq("goods_uuid",uuid).eq("spec_status",1));
    }
    //根据规格的id查询对应的商品的上下架状态
    @Override
    public Integer selectGoodStatu(String specUUID) {
        Integer statu = goodsSpecMapper.selectGoodStatu(specUUID);
        return statu;
    }
}
