package com.jsy.service.impl;

import com.jsy.domain.IndustryCategory;
import com.jsy.mapper.IndustryCategoryMapper;
import com.jsy.service.IIndustryCategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class IndustryCategoryServiceImpl extends ServiceImpl<IndustryCategoryMapper, IndustryCategory> implements IIndustryCategoryService {
    @Resource
    IndustryCategoryMapper categoryMapper;
    @Override
    public List<IndustryCategory> selectCategory() {
        List<IndustryCategory> categories = categoryMapper.selectList(null);
        List<IndustryCategory> collect = categories.stream().filter(item -> item.getPid().longValue()==0L)
                .map(item -> {
                    item.setChildren(getChrildrens(item, categories));
                    return item;
                }).collect(Collectors.toList());
        return collect;
    }
    private List<IndustryCategory> getChrildrens(IndustryCategory root, List<IndustryCategory> categories) {
        List<IndustryCategory> collectTree = categories.stream().filter(item ->
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
        List<IndustryCategory> categories = selectCategory(id);
        for (IndustryCategory item: categories) {
            //判断当前对象的chidren是否为空
            if (!item.getChildren().isEmpty()){
                //不为空就调方法遍历删除
                getNext(item.getChildren());
            }
            //删除主节点
            categoryMapper.deleteById(item.getId());
        }
    }
    public void getNext(List<IndustryCategory> category){
        //判断当前chrldren是否为空
        if (category.size()>0) {
            //遍历
            for (IndustryCategory category1 : category) {
                //删除当前节点
                int i = categoryMapper.deleteById(category1.getId());
                //在把children放进去查
                List<IndustryCategory> children = category1.getChildren();
                getNext(children);
            }
        }else {
            return;
        }
    }
    public List<IndustryCategory> selectCategory(Long id) {
        List<IndustryCategory> categories = categoryMapper.selectList(null);
        List<IndustryCategory> collect = categories.stream().filter(item -> item.getId().longValue()==id)
                .map(item -> {
                    item.setChildren(getChrildrens(item, categories));
                    return item;
                }).collect(Collectors.toList());
        return collect;
    }
}
