package com.jsy.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jsy.basic.util.exception.JSYException;
import com.jsy.basic.util.utils.CurrentUserHolder;
import com.jsy.basic.util.utils.RegexUtils;
import com.jsy.basic.util.vo.UserDto;
import com.jsy.basic.util.vo.UserEntity;
import com.jsy.domain.ShopAssets;
import com.jsy.mapper.ShopAssetsMapper;
import com.jsy.service.IShopAssetsService;
import com.jsy.vo.ShopAssetsVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * <p>
 * 商家资产 服务实现类
 * </p>
 *
 * @author lxr
 * @since 2020-12-17
 */
@Service
public class ShopAssetsServiceImpl extends ServiceImpl<ShopAssetsMapper, ShopAssets> implements IShopAssetsService {

    @Autowired
    private ShopAssetsMapper shopAssetsMapper;

    @Override
    public ShopAssets getByUUid(String uuid) {
        return shopAssetsMapper.getByUUid(uuid);
    }

    @Override
    public void add(ShopAssetsVO shopAssets) {
//        UserDto dto = CurrentUserHolder.getCurrentUser();
//        if (Objects.isNull(dto)) {
//            throw new JSYException(-1,"请登录后操作");
//        }
        if (StringUtils.isNotEmpty(shopAssets.getAlipay())) {
            if (!RegexUtils.isMobile(shopAssets.getAlipay())) {
                throw new JSYException(-1,"请输入正确的支付宝账号");
            }
        }
        if (StringUtils.isNotEmpty(shopAssets.getWechatpaty())) {
            if (!RegexUtils.isMobile(shopAssets.getWechatpaty())) {
                throw new JSYException(-1,"请输入正确的微信账号");
            }
        }
        ShopAssets assets = new ShopAssets();
        BeanUtils.copyProperties(shopAssets,assets);
        baseMapper.insert(assets);
    }

    @Override
    public ShopAssets select() {
        UserDto dto = CurrentUserHolder.getCurrentUser();
        if (Objects.isNull(dto)) {
            throw new JSYException(-1,"请登录后操作");
        }

        ShopAssets shopAssets = baseMapper.selectOne(new QueryWrapper<ShopAssets>().eq("owner_uuid", dto.getRelationUuid()));
        return shopAssets;
    }

    @Override
    public void updateAssets(int type, BigDecimal money,String uuid) {
        if (type == 0) {
            UserDto dto = CurrentUserHolder.getCurrentUser();
            if (Objects.isNull(dto)) {
                throw new JSYException(-1,"登录后操作");
            }
            uuid = dto.getRelationUuid();
        }
        ShopAssets one = baseMapper.selectOne(new QueryWrapper<ShopAssets>().eq("owner_uuid", uuid));
        //账户原有金额
        BigDecimal assets = one.getAssets();
        ShopAssets newAssets = new ShopAssets();
        //0:提现，1：入账
        if (type == 0) {
            //提现
            if (assets.compareTo(money) < 1) {
                throw new JSYException(-1,"金额不足重新输入");
            }
            BigDecimal newDecimal = assets.subtract(money);
            newAssets.setAssets(newDecimal);
        }
        if (type == 1) {
            //入账
            BigDecimal add = assets.add(money);
            newAssets.setAssets(add);
        }
        baseMapper.update(newAssets,new UpdateWrapper<ShopAssets>().eq("owner_uuid",uuid));

    }

    @Override
    public void updateCard(ShopAssetsVO shopAssetsVO) {
        UserDto dto = CurrentUserHolder.getCurrentUser();
        if (Objects.isNull(dto)) {
            throw new JSYException(-1,"请登录后操作");
        }
        ShopAssets assets = new ShopAssets();
        BeanUtils.copyProperties(shopAssetsVO,assets);
        baseMapper.update(assets,new UpdateWrapper<ShopAssets>().eq("uuid",shopAssetsVO.getUuid()));
    }

    @Override
    public int updateIdCard(ShopAssetsVO shopAssetsVo) {
        UserDto user = CurrentUserHolder.getCurrentUser();
        ShopAssets shopAssets = this.getOne(new QueryWrapper<ShopAssets>().eq("owner_uuid", user.getUuid()));
        if (StringUtils.isNotBlank(shopAssetsVo.getAlipay())){
            if (!RegexUtils.isMobile(shopAssetsVo.getAlipay())) {
                throw new JSYException(-1,"请输入正确的支付宝账号");
            }
            shopAssets.setAlipay(shopAssetsVo.getAlipay());
        }
        if (StringUtils.isNotBlank(shopAssetsVo.getWechatpaty())){
            if (!RegexUtils.isMobile(shopAssetsVo.getWechatpaty())) {
                throw new JSYException(-1,"请输入正确的支付宝账号");
            }
            shopAssets.setWechatpaty(shopAssetsVo.getWechatpaty());
        }
        return this.update(shopAssets,new QueryWrapper<ShopAssets>().eq("owner_uuid",user.getUuid()))?1:0;
    }

    @Override
    public int addAsset(ShopAssetsVO shopAssetsVO) {
        UserDto user = CurrentUserHolder.getCurrentUser();
        ShopAssets shopAssets = this.getOne(new QueryWrapper<ShopAssets>().eq("owner_uuid", user.getUuid()));
        if (Objects.nonNull(shopAssets)){
            if (user.getUuid().equals(shopAssets.getOwnerUuid())){
                throw new JSYException(-1,"已经有账号了");
            }
        }

        if (StringUtils.isNotBlank(shopAssetsVO.getAlipay())) {
            if (!RegexUtils.isMobile(shopAssetsVO.getAlipay())) {
                throw new JSYException(-1,"请输入正确的支付宝账号");
            }
        }
        if (StringUtils.isNotBlank(shopAssetsVO.getWechatpaty())){
            if (!RegexUtils.isMobile(shopAssetsVO.getWechatpaty())){
                throw new JSYException(-1,"请输入正确的微信账号");
            }
        }
        shopAssets = new ShopAssets();
        BeanUtil.copyProperties(shopAssetsVO,shopAssets,true);
        shopAssets.setOwnerUuid(user.getUuid());
        shopAssets.setUuid(UUID.randomUUID().toString().replaceAll("-",""));
        shopAssets.setAssets(BigDecimal.valueOf(0));
        return this.save(shopAssets)?1:0;
    }



    //修改账户余额
    //0:提现 1：入账
    //入账需要转入uuid 为店铺拥有者的uuid
    @Override
    public int updateMoney(Integer type, BigDecimal num, String uuid) {

        if (type<0||type>1){
            throw new JSYException(-1,"请规范交易账号的类型");
        }
        if (num.compareTo(BigDecimal.ZERO)<=0){
            throw new JSYException(-1,"交易失败");
        }
        //如果不是内部调用  uuid为空需从请求中获取
        if (StringUtils.isEmpty(uuid)){
            UserEntity userEntity = CurrentUserHolder.getUserEntity();
            uuid = userEntity.getUid();
        }
        ShopAssets shopAssets = this.getOne(new QueryWrapper<ShopAssets>().eq("owner_uuid", uuid));
        //提现
        if (type==0){
            if (num.compareTo(shopAssets.getAssets())>0){
                throw new JSYException(-1,"余额不足，提现失败");
            }
            shopAssets.setAssets(shopAssets.getAssets().subtract(num));
        }
        //入账
        if (type==1){
            shopAssets.setAssets(shopAssets.getAssets().add(num));
        }
        return this.update(shopAssets, new QueryWrapper<ShopAssets>().eq("owner_uuid", uuid))?1:0;
    }

    @Override
    public int backByOuid(String uuid) {
        return baseMapper.backByOuid(uuid);
    }

    @Override
    public ShopAssets getByOid(String uuid) {
        return baseMapper.getByOid(uuid);
    }
}
