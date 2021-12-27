package com.jsy.dto;

import com.jsy.domain.ShopBill;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @className（类名称）: SelectBillDto
 * @description（类描述）: this is the SelectBillDto class
 * @author（创建人）: ${arli}
 * @createDate（创建时间）: 2021/12/24
 * @version（版本）: v1.0
 */
@Data
public class SelectBillDto {


    //日期
    private  String  date;
    //数量
    private  Integer amount;
    //当天总计
    private BigDecimal totalMoney;

    //数据
    private List<ShopBill> billDataList;
}
