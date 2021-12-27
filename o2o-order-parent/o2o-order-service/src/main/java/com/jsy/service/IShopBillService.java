package com.jsy.service;

import com.jsy.domain.ShopBill;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jsy.dto.SelectBillDto;
import com.jsy.dto.SelectSevenBillDto;
import com.jsy.mapper.ShopBillMapper;
import com.jsy.query.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 商家账单表 服务类
 * </p>
 *
 * @author arli
 * @since 2021-12-24
 */
public interface IShopBillService extends IService<ShopBill> {

    //添加一条收入/退款记录
    Boolean addShopBillOne(AddShopBillOneParam param);
    //分页查询店铺收入、退款记录
    List<SelectBillDto> selectBill(SelectBillParam param);
    //查询某一天店铺收入、退款记录
    SelectBillDto selectBillOneDay(SelectBillOneDayParam param);
    //查询某一时间段店铺收入、退款记录
    List<SelectBillDto> selectBillSomeTime(SelectBillSomeTimeParam param);
    //近N日成交额：统计最近N天完成交易的订单（不包括退款售后订单金额）
    SelectSevenBillDto selectSevenBill(SelectSevenBillParam param);
}
