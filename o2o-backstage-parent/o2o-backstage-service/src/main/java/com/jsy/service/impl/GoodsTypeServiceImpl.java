package com.jsy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jsy.basic.util.utils.BeansCopyUtils;
import com.jsy.domain.GoodsType;
import com.jsy.dto.GoodsTypeDto;
import com.jsy.mapper.GoodsTypeMapper;
import com.jsy.service.IGoodsTypeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 大后台 行业服务分类 服务实现类
 * </p>
 *
 * @author Tian
 * @since 2021-11-04
 */
@Service
public class GoodsTypeServiceImpl extends ServiceImpl<GoodsTypeMapper, GoodsType> implements IGoodsTypeService {
    @Resource
    GoodsTypeMapper categoryMapper;
    @Override
    public List<GoodsType> selectCategory() {
        List<GoodsType> categories = categoryMapper.selectList(null);
        List<GoodsType> collect = categories.stream().filter(item -> item.getPid().longValue()==0L)
                .map(item -> {
                    item.setChildren(getChrildrens(item, categories));
                    return item;
                }).collect(Collectors.toList());
        return collect;
    }
    private List<GoodsType> getChrildrens(GoodsType root, List<GoodsType> categories) {
        List<GoodsType> collectTree = categories.stream().filter(item ->
                item.getPid().equals(root.getId())
        ).map(item -> {
            item.setChildren(getChrildrens(item, categories));
            return item;
        }).collect(Collectors.toList());
        return collectTree;
    }

    @Override
    public void deleteById(Long id) {
        //把节点下面的子节点全部查出来
        List<GoodsType> categories = selectCategory(id);
        for (GoodsType item: categories) {
            //判断当前对象的chidren是否为空
            if (!item.getChildren().isEmpty()){
                //不为空就调方法遍历删除
                getNext(item.getChildren());
            }
            //删除主节点
            categoryMapper.deleteById(item.getId());
        }
    }

    @Override
    public List<GoodsTypeDto> selectLevel(Integer level) {
        List<GoodsType> goodsTypes = categoryMapper.selectList(new QueryWrapper<GoodsType>().eq("level", level).eq("state", 1));
        List<GoodsTypeDto> dtos = BeansCopyUtils.listCopy(goodsTypes, GoodsTypeDto.class);
        return dtos;
    }

    /**
     *
     * @param id 本级id
     * @return
     */
    @Override
    public List<GoodsType> listPar(Long id) {
        /*GoodsType goodsType = categoryMapper.selectById(id);
        List<GoodsType> goodsTypes1 = listPar(goodsType.getPid());*/

        return null;
    }




    public void getNext(List<GoodsType> category){
        //判断当前chrldren是否为空
        if (category.size()>0) {
            //遍历
            for (GoodsType category1 : category) {
                //删除当前节点
                int i = categoryMapper.deleteById(category1.getId());
                //在把children放进去查
                List<GoodsType> children = category1.getChildren();
                getNext(children);
            }
        }else {
            return;
        }
    }
    public List<GoodsType> selectCategory(Long id) {
        List<GoodsType> categories = categoryMapper.selectList(null);
        List<GoodsType> collect = categories.stream().filter(item -> item.getId().longValue()==id)
                .map(item -> {
                    item.setChildren(getChrildrens(item, categories));
                    return item;
                }).collect(Collectors.toList());
        return collect;
    }

}
