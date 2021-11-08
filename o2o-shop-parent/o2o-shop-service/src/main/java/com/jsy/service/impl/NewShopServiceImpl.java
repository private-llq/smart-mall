package com.jsy.service.impl;

import com.jsy.basic.util.exception.JSYException;
import com.jsy.basic.util.utils.GouldUtil;
import com.jsy.basic.util.utils.RegexUtils;
import com.jsy.basic.util.utils.UUIDUtils;
import com.jsy.domain.NewShop;
import com.jsy.mapper.NewShopMapper;
import com.jsy.parameter.NewShopParam;
import com.jsy.service.INewShopService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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
        if (shopLogo.size()>5){
            throw new JSYException(-1,"照片只能上传5张");
        }

        NewShop newShop = new NewShop();
        BeanUtils.copyProperties(shopPacketParam,newShop);
        //获取登录用户
//        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
//        newShop.setOwnerUuid(loginUser.getId().toString());
        newShop.setLonLat(GouldUtil.getLonLat(shopPacketParam.getAddressDetail()));
        newShop.setUuid(UUIDUtils.getUUID());
        shopMapper.insert(newShop);
    }


}
