package com.jsy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jsy.client.FileClient;
import com.jsy.domain.ShopType;
import com.jsy.mapper.ShopTypeMapper;
import com.jsy.service.IShopTypeService;
import com.jsy.vo.ShopTypeVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lijin
 * @since 2020-11-12
 */
@Service
@Slf4j
public class ShopTypeServiceImpl extends ServiceImpl<ShopTypeMapper, ShopType> implements IShopTypeService {

    @Resource
    private ShopTypeMapper shopTypeMapper;

    @Autowired
    private FileClient fileClient;

    //根据门店类型uuid查询所属类型层级系
    @Override
    public String selectShopType(String uuid) {
        String type = "";
        ShopType shopType = shopTypeMapper.selectOne(new QueryWrapper<ShopType>().eq("uuid", uuid));
        if (shopType == null) {
            return null;
        }
        type = type + shopType.getName();
        if (!shopType.getTypeLevel().equals("0")) {
            String s = selectShopType(shopType.getParentUuid());
            type = s + ">" + type;
        }
        return type;
    }


    /**********************************/
    @Override
    public List<ShopType> findByParentId(Long parentId) {
        List<ShopType> shopTypes = shopTypeMapper.selectList(new QueryWrapper<ShopType>().eq("parent_id", parentId).orderByAsc("id"));

        ArrayList<String> list = new ArrayList<>();
        for (ShopType shopType : shopTypes) {
            list.add(shopType.getImageUrl());
        }

        Map<String, String> picUrl = fileClient.getPicUrl(list);
        for (ShopType shopType : shopTypes) {
            shopType.setImageUrl(picUrl.get(shopType.getImageUrl()));
        }

        return shopTypes;
    }

    @Override
    public List<ShopTypeVo> getList() {
        List<ShopType> shopTypes = shopTypeMapper.selectList(null);
        ArrayList<ShopTypeVo> typeVos = new ArrayList<>();
        for (ShopType shopType : shopTypes) {
            ShopTypeVo shopTypeVo = new ShopTypeVo();
            BeanUtils.copyProperties(shopType, shopTypeVo);
            typeVos.add(shopTypeVo);
        }
        //一级分类
        List<ShopTypeVo> shopTypeVoList = typeVos.stream().filter(entity ->
                entity.getParentId() == 0
        ).map((menu) -> {
            menu.setChildren(getChildrens(menu, typeVos));
            return menu;
        }).collect(Collectors.toList());

        //图片变为imageUrl
        ArrayList<String> list = new ArrayList<>();
        for (ShopTypeVo shopTypeVo : shopTypeVoList) {
            list.add(shopTypeVo.getImageUrl());
            for (ShopTypeVo child : shopTypeVo.getChildren()) {
                list.add(child.getImageUrl());
            }
        }
        Map<String, String> picUrl = fileClient.getPicUrl(list);
        for (ShopTypeVo shopTypeVo : shopTypeVoList) {
            shopTypeVo.setImageUrl(picUrl.get(shopTypeVo.getImageUrl()));
            for (ShopTypeVo child : shopTypeVo.getChildren()) {
                child.setImageUrl(picUrl.get(child.getImageUrl()));
            }
        }

        return shopTypeVoList;
    }

    //TODO:递归找出所有子类,可改进为Map循环
    //递归找出所有子类
    private List<ShopTypeVo> getChildrens(ShopTypeVo shopTypeVo, List<ShopTypeVo> all) {
        List<ShopTypeVo> children = all.stream().filter(entity -> {
            return entity.getParentId() == shopTypeVo.getId();
        }).map((menu) -> {
            menu.setChildren(getChildrens(menu, all));
            if (menu.getChildren().size() == 0) {
                menu.setChildren(null);
            }
            return menu;
        }).collect(Collectors.toList());
        return children;
    }
}
