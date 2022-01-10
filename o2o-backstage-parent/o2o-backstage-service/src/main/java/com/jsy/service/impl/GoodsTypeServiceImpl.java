package com.jsy.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jsy.basic.util.exception.JSYException;
import com.jsy.basic.util.utils.BeansCopyUtils;
import com.jsy.domain.GoodsType;
import com.jsy.dto.GoodsTypeDto;
import com.jsy.mapper.GoodsTypeMapper;
import com.jsy.service.IGoodsTypeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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



    @Override
    public List<GoodsType> getParentList(Long id) {
        List<GoodsType> goodsTypes = categoryMapper.selectList(null);
        List<GoodsType> treeList = new ArrayList<>();
        GoodsType goodsType = categoryMapper.selectById(id);
        if (Objects.isNull(goodsType)){
            throw new JSYException(-1,"没有找到该商品分类！");
        }
        treeList.add(goodsType);
        return getParentId(treeList,goodsType.getPid(),goodsTypes);
    }
    public List<GoodsType> getParentId(List<GoodsType> treeList,Long pid,List<GoodsType> trees) {
        GoodsType tree = trees.stream().filter(s -> s.getId() == pid).findFirst().get();
        treeList.add(tree);
        if (tree.getLevel() != 2) {
            getParentId(treeList, tree.getPid(), trees);
        }
        return treeList;
    }


    public List<Long> getGoodsTypeId(Long id) {
        List<GoodsType> treeList = categoryMapper.selectList(null);
        List<Long> list = new ArrayList<>();
        Long temp = id;
        for (GoodsType tree : treeList) {
            for (GoodsType tree1 : treeList) {
                if (tree1.getId().equals(temp) && tree1.getPid()!=0L){
                    list.add(tree1.getId());
                    temp = tree1.getPid();

                }
            }
        }
        System.out.println("++++++++++1111111111");
//        GoodsType tree = categoryMapper.selectById(temp);
//        if (ObjectUtil.isNotNull(tree)){
//
//            list.add(tree.getPid());
//        }
        System.out.println(list);


//        List<Long> collect = list.stream().filter(x -> {
//            if (x == 0L) {
//                return false;
//            }
//            return true;
//        }).collect(Collectors.toList());

        return list;

//        List<Tree> treeList = categoryMapper.selectList(null);
//        List<Long> list = new ArrayList<>();
//        Long temp = id;
//        for (Tree tree : treeList) {
//            for (Tree tree1 : treeList) {
//                if (tree1.getId()==temp&&tree1.getParentId()!=0){
//                    list.add(tree1.getParentId());
//                    temp = tree1.getParentId();
//                }
//            }
//        }
//        Tree tree = treeMapper.selectById(temp);
//        if (ObjectUtil.isNotNull(tree)){
//
//            list.add(tree.getParentId());
//        }
//        Collections.reverse(list);
//        return list.toString();
    }



    @Override
    public String bachGoodsType(List<Long> longList) {
        List<GoodsType> list = categoryMapper.selectBatchIds(longList);
        StringBuffer buffer = new StringBuffer();
        list.forEach(x->{
            buffer.append(x.getClassifyName()+"-");

        });
        buffer.deleteCharAt(buffer.length() - 1);
        return buffer.toString();
    }

}
