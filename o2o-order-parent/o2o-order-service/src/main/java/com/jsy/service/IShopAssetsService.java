package com.jsy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jsy.domain.ShopAssets;
import com.jsy.vo.ShopAssetsVO;

import java.math.BigDecimal;

/**
 * <p>
 * 商家资产 服务类
 * </p>
 *
 * @author yu
 * @since 2020-12-17
 */
public interface IShopAssetsService extends IService<ShopAssets> {

    ShopAssets getByUUid(String uuid);

    void add(ShopAssetsVO shopAssets);

    ShopAssets select();

    //修改账户余额
    //0:提现 1：入账
    //入账需要转入uuid 为店铺的uuid
    void updateAssets(int type, BigDecimal money, String uuid);

    void updateCard(ShopAssetsVO shopAssetsVO);

    int backByOuid(String uuid);//设置流水记录为退款状态

    ShopAssets getByOid(String uuid);

    /**
     * PC
     * @title        商家注册的资产账号
     * @description  默认设置资产为零
     * @params
     * @since        - 2021-05-15
     * @throws
     * @return      Result<int>
     *
     * * * * * T * I * M * E * * * * *
     * 创建及修改内容
     * @author      shY
     * @createTime  2021-05-15
     * @editor      shY
     * @updateDesc  原著
     * @updateTime  2021-05-15
     */
    int addAsset(ShopAssetsVO shopAssetsVO);

    /**
     * PC
     * @title       修改账号的支付宝或微信
     * @description 只能修改支付宝微信，银行卡
     * @params
     * @since        - 2021-05-15
     * @throws
     * @return      Result<int>
     *
     * * * * * T * I * M * E * * * * *
     * 创建及修改内容
     * @author      shY
     * @createTime  2021-05-15
     * @editor      shY
     * @updateDesc  原著
     * @updateTime  2021-05-15
     */
    int updateIdCard(ShopAssetsVO shopAssetsVo);


    /**
     * PC
     * @title       店铺拥有者资金流动
     * @description     type 0是提现  1是入账 num 是金额 uuid是shopOwner(店铺拥有者的uuid)的uuid
     * @params          type num uuid
     * @since        - 2021-05-15
     * @throws
     * @return      Result<int>
     *
     * * * * * T * I * M * E * * * * *
     * 创建及修改内容
     * @author      shY
     * @createTime  2021-05-15
     * @editor      shY
     * @updateDesc  原著
     * @updateTime  2021-05-15
     */
    int updateMoney(Integer type, BigDecimal num, String uuid);
}
