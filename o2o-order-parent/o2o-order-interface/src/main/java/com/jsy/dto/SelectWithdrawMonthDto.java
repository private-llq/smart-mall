package com.jsy.dto;

import com.jsy.domain.ShopWithdraw;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * @className（类名称）: SelectWithdrawMonthDto
 * @description（类描述）: this is the SelectWithdrawMonthDto class
 * @author（创建人）: ${arli}
 * @createDate（创建时间）: 2021/12/25
 * @version（版本）: v1.0
 */
@Data
public class SelectWithdrawMonthDto {

    //日期
    private String yearMonth;
    //多少条
    private  Integer size;

    //提现记录
    private List<SelectWithdrawDto> selectWithdrawDtos;
}
