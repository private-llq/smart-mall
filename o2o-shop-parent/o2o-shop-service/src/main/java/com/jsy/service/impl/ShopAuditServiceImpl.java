package com.jsy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jsy.domain.NewShop;
import com.jsy.domain.ShopAudit;
import com.jsy.mapper.NewShopMapper;
import com.jsy.mapper.ShopAuditMapper;
import com.jsy.parameter.NewShopAuditParam;
import com.jsy.service.IShopAuditService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhsj.baseweb.support.ContextHolder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yu
 * @since 2021-12-01
 */
@Service
public class ShopAuditServiceImpl extends ServiceImpl<ShopAuditMapper, ShopAudit> implements IShopAuditService {
    @Resource
    private NewShopMapper shopMapper;
    @Resource
    private ShopAuditMapper auditMapper;
    @Override
    public boolean addAudit(NewShopAuditParam auditParam) {
        NewShop newShop = shopMapper.selectById(auditParam.getShopId());
        ShopAudit shopAudit = auditMapper.selectOne(new QueryWrapper<ShopAudit>()
                .eq("shop_id", auditParam.getShopId())
                .orderByDesc("create_time")
                .last("limit 1")
        );

        if (shopAudit==null||shopAudit!=null&&shopAudit.getRejectedReason()!=null){
            //审核状态 0未审核 1已审核 2审核未通过 3资质未认证
            if (auditParam.getState()==1){
                newShop.setState(1);
            }else if(auditParam.getState()==2){
                ShopAudit shopAudit1 = new ShopAudit();
                newShop.setState(2);
                shopAudit1.setShopId(auditParam.getShopId());
                shopAudit1.setRejectedReason(auditParam.getRejectedReason());
                Long id = ContextHolder.getContext().getLoginUser().getId();
                shopAudit1.setUserId(id);
                auditMapper.insert(shopAudit1);
            }
        }else {
            //审核状态 0未审核 1已审核 2审核未通过 3资质未认证
            if (auditParam.getState()==1){
                newShop.setState(1);
            }else if(auditParam.getState()==2){
                newShop.setState(2);
                shopAudit.setShopId(auditParam.getShopId());
                shopAudit.setRejectedReason(auditParam.getRejectedReason());
                Long id = ContextHolder.getContext().getLoginUser().getId();
                shopAudit.setUserId(id);
                auditMapper.updateById(shopAudit);
            }
        }
        shopMapper.updateById(newShop);
        return true;
    }

    @Override
    public boolean updateShielding(NewShopAuditParam auditParam) {
        NewShop newShop = shopMapper.selectById(auditParam.getShopId());
        ShopAudit shopAudit = auditMapper.selectOne(new QueryWrapper<ShopAudit>()
                .eq("shop_id", auditParam.getShopId())
                .orderByDesc("create_time")
                .last("limit 1")
        );
        System.out.println(shopAudit);
        if (shopAudit==null||shopAudit!=null&&shopAudit.getShieldingReason()!=null){
            System.out.println("新增");
            //屏蔽状态 0未屏蔽  1已屏蔽
            if (auditParam.getShielding()==0){
                newShop.setShielding(0);
            }else if(auditParam.getShielding()==1){
                ShopAudit shopAudit1 = new ShopAudit();
                newShop.setShielding(1);
                shopAudit1.setShopId(auditParam.getShopId());
                shopAudit1.setShieldingReason(auditParam.getShieldingReason());
                Long id = ContextHolder.getContext().getLoginUser().getId();
                shopAudit1.setUserId(id);
                auditMapper.insert(shopAudit1);
            }
        }else {
            System.out.println("修改");
            //屏蔽状态 0未屏蔽  1已屏蔽
            if (auditParam.getShielding()==0){
                newShop.setState(0);
            }else if(auditParam.getShielding()==1){
                newShop.setShielding(1);
                shopAudit.setShopId(auditParam.getShopId());
                shopAudit.setShieldingReason(auditParam.getShieldingReason());
                Long id = ContextHolder.getContext().getLoginUser().getId();
                shopAudit.setUserId(id);
                auditMapper.updateById(shopAudit);
            }
        }
        shopMapper.updateById(newShop);
        return true;
    }
}
