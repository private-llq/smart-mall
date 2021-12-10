package com.jsy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsy.basic.util.PageInfo;
import com.jsy.basic.util.utils.BeansCopyUtils;
import com.jsy.basic.util.utils.SnowFlake;
import com.jsy.client.GoodsTypeClient;
import com.jsy.domain.BackstageGoods;
import com.jsy.dto.BacksageDto;
import com.jsy.dto.BackstageGoodsDto;
import com.jsy.dto.GoodsTypeDto;
import com.jsy.mapper.BackstageGoodsMapper;
import com.jsy.param.BackstageGoodsParam;
import com.jsy.query.BackstageGoodsQuery;
import com.jsy.service.IBackstageGoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Tian
 * @since 2021-11-29
 */
@Service
public class BackstageGoodsServiceImpl extends ServiceImpl<BackstageGoodsMapper, BackstageGoods> implements IBackstageGoodsService {



    @Autowired
    private BackstageGoodsMapper backstageGoodsMapper;

    @Autowired
    private GoodsTypeClient goodsTypeClient;

    @Override
    public void addBackstageGoods(BackstageGoodsParam backstageGoodsParam) {
        BackstageGoods backstageGoods = new BackstageGoods();
        GoodsTypeDto data = goodsTypeClient.get(backstageGoodsParam.getGoodsTypeId()).getData();
        if (Objects.nonNull(data)){
            backstageGoods.setGoodsTypeName(data.getClassifyName());//商品分类名称
        }
        backstageGoods.setGoodsNumber(String.valueOf(SnowFlake.nextId()));
        backstageGoods.setPlatform("平台官方");
        backstageGoods.setState(0);//默认禁用
        BeanUtils.copyProperties(backstageGoodsParam,backstageGoods);
        backstageGoodsMapper.insert(backstageGoods);
    }

    @Override
    public void updateBackstageGoods(BackstageGoodsParam backstageGoodsParam) {
        BackstageGoods backstageGoods = new BackstageGoods();
        BeanUtils.copyProperties(backstageGoodsParam,backstageGoods);
        int i = backstageGoodsMapper.updateById(backstageGoods);
    }

    @Override
    public PageInfo<BacksageDto> pageBackstageGoods(BackstageGoodsQuery backstageGoodsQuery) {
        String goodsName = backstageGoodsQuery.getGoodsName();
        String goodsNumber = backstageGoodsQuery.getGoodsNumber();
        LocalDateTime startTime = backstageGoodsQuery.getStartTime();
        LocalDateTime endTime = backstageGoodsQuery.getEndTime();
        Long goodsTypeId = backstageGoodsQuery.getGoodsTypeId();
        Page<BackstageGoods> page= new Page<>(backstageGoodsQuery.getPage(),backstageGoodsQuery.getRows());
        Page<BackstageGoods> goodsPage = backstageGoodsMapper.selectPage(page, new QueryWrapper<BackstageGoods>()
                .eq("state",1)
                .like(StringUtils.isNotBlank(goodsName),"title",goodsName)
                .eq(StringUtils.isNotBlank(goodsNumber),"goods_number",goodsNumber)
                .between(Objects.nonNull(startTime) && Objects.nonNull(endTime),"create_time",startTime,endTime)
                .eq(Objects.nonNull(goodsTypeId),"goods_type_id",goodsTypeId)

        );
        PageInfo<BacksageDto> pageInfo = new PageInfo<>();
        List<BackstageGoods> records = goodsPage.getRecords();
        List<BacksageDto> backstageDtos = BeansCopyUtils.listCopy(records, BacksageDto.class);
        pageInfo.setRecords(backstageDtos);
        pageInfo.setCurrent(goodsPage.getCurrent());
        pageInfo.setSize(goodsPage.getSize());
        pageInfo.setTotal(goodsPage.getTotal());
        return pageInfo;
    }

    /**
     * 分页条件查询 医疗C端 推荐商品页面
     * @param backstageGoodsQuery
     * @return
     */
    @Override
    public PageInfo<BackstageGoodsDto> listBackstageGoods(BackstageGoodsQuery backstageGoodsQuery) {

        /**
         * 搜索内容
         */
        String keyword = backstageGoodsQuery.getKeyword();

        /**
         * 价格 0 ：降序 1 升序
         */
        Integer priceSort = backstageGoodsQuery.getPriceSort();
        /**
         * 销量 0 ：降序 1 升序
         */
        Integer salesSort = backstageGoodsQuery.getSalesSort();

        Page<BackstageGoods> page= new Page<>(backstageGoodsQuery.getPage(),backstageGoodsQuery.getRows());
        Page<BackstageGoods> goodsPage = backstageGoodsMapper.selectPage(page, new QueryWrapper<BackstageGoods>()
                .eq("state",1)
        );
        PageInfo<BackstageGoodsDto> pageInfo = new PageInfo<>();
        List<BackstageGoods> records = goodsPage.getRecords();

        if (Objects.nonNull(priceSort) && priceSort==0){

            List<BackstageGoods> collect = records.stream().sorted(Comparator.comparing(BackstageGoods::getShowPrice)).collect(Collectors.toList());
            List<BackstageGoodsDto> backstageDtos = BeansCopyUtils.listCopy(collect, BackstageGoodsDto.class);
            pageInfo.setRecords(backstageDtos);
            pageInfo.setCurrent(goodsPage.getCurrent());
            pageInfo.setSize(goodsPage.getSize());
            pageInfo.setTotal(goodsPage.getTotal());
            return pageInfo;


        }
        if (Objects.nonNull(priceSort) && priceSort==1){
            List<BackstageGoods> collect = records.stream().sorted(Comparator.comparing(BackstageGoods::getShowPrice).reversed()).collect(Collectors.toList());
            List<BackstageGoodsDto> backstageDtos = BeansCopyUtils.listCopy(collect, BackstageGoodsDto.class);
            pageInfo.setRecords(backstageDtos);
            pageInfo.setCurrent(goodsPage.getCurrent());
            pageInfo.setSize(goodsPage.getSize());
            pageInfo.setTotal(goodsPage.getTotal());
            return pageInfo;
        }
        //todo 销量数据暂时拿不到
        /*if (Objects.nonNull(salesSort) && salesSort==0){
            records.stream().sorted(Comparator.comparing(BackstageGoods::getShowPrice)).collect(Collectors.toList());
        }
        if (Objects.nonNull(salesSort) && salesSort==1){
            records.stream().sorted(Comparator.comparing(BackstageGoods::getShowPrice).reversed()).collect(Collectors.toList());
        }*/
        List<BackstageGoodsDto> backstageDtos = BeansCopyUtils.listCopy(records, BackstageGoodsDto.class);
        pageInfo.setRecords(backstageDtos);
        pageInfo.setCurrent(goodsPage.getCurrent());
        pageInfo.setSize(goodsPage.getSize());
        pageInfo.setTotal(goodsPage.getTotal());
        return pageInfo;
    }
}
