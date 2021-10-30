package com.jsy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jsy.domain.GoodsOtherCost;
import com.jsy.dto.SelectGoodsOtherCostByGoodsUuidDto;
import com.jsy.mapper.GoodsOtherCostMapper;
import com.jsy.parameter.SelectGoodsOtherCostByGoodsUuidParam;
import com.jsy.service.GoodsOtherCostService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GoodsOtherCostServiceImpl extends ServiceImpl<GoodsOtherCostMapper, GoodsOtherCost> implements GoodsOtherCostService {
    @Resource
    private GoodsOtherCostMapper goodsOtherCostMapper;


    //根据商品的uuid查询其他收费项目
    @Override
    public List<SelectGoodsOtherCostByGoodsUuidDto> selectGoodsOtherCostByGoodsUuid(List<SelectGoodsOtherCostByGoodsUuidParam> params) {
        List<SelectGoodsOtherCostByGoodsUuidDto> lists = new ArrayList<>();//返回对象list
        List<GoodsOtherCost> goodsOtherCosts = new ArrayList<>();//查询出来的数据

        for (SelectGoodsOtherCostByGoodsUuidParam param : params) {
            List<GoodsOtherCost> goods_uuid = goodsOtherCostMapper.selectList(new QueryWrapper<GoodsOtherCost>().eq("goods_uuid", param.goodUuid));

          //  GoodsOtherCost goodsOtherCost = goodsOtherCostMapper.selectOne(new QueryWrapper<GoodsOtherCost>().eq("goods_uuid", param.goodUuid));
            if (goods_uuid != null || goods_uuid.size()!=0) {
                for (GoodsOtherCost goodsOtherCost : goods_uuid) {
                    goodsOtherCosts.add(goodsOtherCost);//添加
                }

            }
        }
        //安装替他收费名分组创建集合
        Map<String, List<GoodsOtherCost>> collect = goodsOtherCosts.stream().collect(Collectors.groupingBy(x -> x.getName()));

        for (String s : collect.keySet()) {//循环键集合
            SelectGoodsOtherCostByGoodsUuidDto param = new SelectGoodsOtherCostByGoodsUuidDto();
            param.setName(s);//其他收费的名称
            List<GoodsOtherCost> goodsOtherCosts1 = collect.get(s);//根据键取出集合
            BigDecimal sun = new BigDecimal(0);//每种其他费用的和
            for (GoodsOtherCost goodsOtherCost : goodsOtherCosts1) {//遍历
                BigDecimal num = new BigDecimal(0);
                for (SelectGoodsOtherCostByGoodsUuidParam selectGoodsOtherCostByGoodsUuidParam : params) {
                    if (goodsOtherCost.getGoodsUuid().equals(selectGoodsOtherCostByGoodsUuidParam.getGoodUuid())) {
                         num =new BigDecimal(selectGoodsOtherCostByGoodsUuidParam.getNum());
                    }
                }
                sun = sun.add(goodsOtherCost.getPrice().multiply(num));
            }
            param.setPrice(sun);//其他费用
            lists.add(param);
        }

        return lists;

    }

}





