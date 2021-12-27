package com.jsy.service;

import com.jsy.domain.ShopWithdraw;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jsy.dto.SelectWithdrawDto;
import com.jsy.dto.SelectWithdrawMonthDto;
import com.jsy.mapper.ShopWithdrawMapper;
import com.jsy.query.AddShopWithdrawOneParam;
import com.jsy.query.SelectWithdraParam;
import com.jsy.query.SelectWithdrawMonthParam;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 商家提现记录表 服务类
 * </p>
 *
 * @author arli
 * @since 2021-12-24
 */
public interface IShopWithdrawService extends IService<ShopWithdraw> {
    //新增一条提现记录
    Boolean addShopWithdrawOne(AddShopWithdrawOneParam param);
    //查询提现记录
    List<SelectWithdrawDto> selectWithdraw(SelectWithdraParam param);
    //查询某个月提现记录
    SelectWithdrawMonthDto selectWithdrawMonth(SelectWithdrawMonthParam param);
}
