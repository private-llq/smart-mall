package com.jsy.service.impl;

import com.jsy.basic.util.exception.JSYException;
import com.jsy.basic.util.utils.GouldUtil;
import com.jsy.basic.util.utils.RegexUtils;
import com.jsy.basic.util.utils.UUIDUtils;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.client.TreeClient;
import com.jsy.domain.NewShop;
import com.jsy.domain.Tree;
import com.jsy.dto.NewShopPreviewDto;
import com.jsy.dto.NewShopRecommendDto;
import com.jsy.mapper.NewShopMapper;
import com.jsy.parameter.NewShopParam;
import com.jsy.parameter.NewShopSetParam;
import com.jsy.service.INewShopService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 新——店铺表 服务实现类
 * </p>
 *
 * @author yu
 * @since 2021-11-08
 */
@Service
public class NewShopServiceImpl extends ServiceImpl<NewShopMapper, NewShop> implements INewShopService {

    @Resource
    private NewShopMapper shopMapper;
    @Resource
    private TreeClient treeClient;


    /**
     * @Description: 创建店铺
     * @Param: [shopPacketParam]
     * @Return: void
     * @Author: Tian
     * @Date: 2021/11/8-11:49
     *
     * @param shopPacketParam*/
    @Override
    public void addNewShop(NewShopParam shopPacketParam) {
        boolean mobile = RegexUtils.isLandline(shopPacketParam.getMobile());//验证电话
        if (!mobile) {
            throw new JSYException(-1, "座机电话格式错误");
        }
        boolean phone = RegexUtils.isMobile(shopPacketParam.getShopPhone());
        if (!phone){
            throw new JSYException(-1, "经营者/法人电话格式错误");
        }

        if (shopPacketParam.getShopName().length() > 15) {
            throw new JSYException(-1, "店铺名太长");
        }
        List<String> shopLogo = shopPacketParam.getShopLogo();
        if (shopLogo.size()>1){
            throw new JSYException(-1,"照片只能上传1张");
        }

        NewShop newShop = new NewShop();
        BeanUtils.copyProperties(shopPacketParam,newShop);
        //行业分类的  二级三级标题  逗号分隔
        String treeId = shopPacketParam.getShopTreeId();
        //数组
        String[] split = treeId.split(",");
        Long aLong = Long.valueOf(split[0]);
        System.out.println(aLong);
        Tree tree = treeClient.getTree(aLong).getData();
        //1是服务行业  0 套餐行业
        if (tree.getParentId()==1){
            newShop.setType(1);
        }else {
            newShop.setType(0);
        }
        //获取登录用户
//        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
//        newShop.setOwnerUuid(loginUser.getId().toString());
        newShop.setLonLat(GouldUtil.getLonLat(shopPacketParam.getAddressDetail()));
//        newShop.setUuid(UUIDUtils.getUUID());
        shopMapper.insert(newShop);
    }

    @Override
    public NewShopPreviewDto getPreviewDto(Long shopId) {
        NewShop newShop = shopMapper.selectById(shopId);
        NewShopPreviewDto newShopPreviewDto = new NewShopPreviewDto();
        String treeId = newShop.getShopTreeId();
        String[] split = treeId.split(",");
        String shopTreeIdName="";
        for (String s : split) {
            Tree tree = treeClient.getTree(Long.valueOf(s)).getData();
            shopTreeIdName = shopTreeIdName+"-"+tree.getName();
        }
        newShopPreviewDto.setShopTreeIdName(shopTreeIdName.substring(1));
        BeanUtils.copyProperties(newShop,newShopPreviewDto);
        return newShopPreviewDto;
    }

    @Override
    public void update(NewShopParam shopPacketParam) {
        boolean mobile = RegexUtils.isLandline(shopPacketParam.getMobile());//验证电话
        if (!mobile) {
            throw new JSYException(-1, "座机电话格式错误");
        }
        boolean phone = RegexUtils.isMobile(shopPacketParam.getShopPhone());
        if (!phone){
            throw new JSYException(-1, "经营者/法人电话格式错误");
        }

        if (shopPacketParam.getShopName().length() > 15) {
            throw new JSYException(-1, "店铺名太长");
        }
        List<String> shopLogo = shopPacketParam.getShopLogo();
        if (shopLogo.size()>1){
            throw new JSYException(-1,"照片只能上传1张");
        }

        NewShop newShop = new NewShop();
        BeanUtils.copyProperties(shopPacketParam,newShop);
        //行业分类的  二级三级标题  逗号分隔
        String treeId = shopPacketParam.getShopTreeId();
        //数组
        String[] split = treeId.split(",");
        Long aLong = Long.valueOf(split[0]);
        Tree tree = treeClient.getTree(aLong).getData();

        //1是服务行业  0 套餐行业
        if (tree.getParentId()==1){
            newShop.setType(1);
        }else {
            newShop.setType(0);
        }
        //获取登录用户
//        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
//        newShop.setOwnerUuid(loginUser.getId().toString());
        newShop.setLonLat(GouldUtil.getLonLat(shopPacketParam.getAddressDetail()));
//        newShop.setUuid(UUIDUtils.getUUID());
        shopMapper.updateById(newShop);
    }

    @Override
    public void setSetShop(NewShopSetParam shopSetParam) {
        NewShop newShop = shopMapper.selectById(shopSetParam.getId());
        BeanUtils.copyProperties(shopSetParam,newShop);
        System.out.println(newShop);
        shopMapper.updateById(newShop);
    }

    //C端查询店铺
    @Override
    public List<NewShopRecommendDto> getShopAllList(Long treeId, String location) {
        List<NewShop> newShopList = shopMapper.selectList(null);
//      newShopList.stream().filter(s->{})

        return null;
    }


}
