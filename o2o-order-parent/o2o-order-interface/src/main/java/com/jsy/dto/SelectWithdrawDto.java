package com.jsy.dto;

import com.jsy.domain.ShopWithdraw;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * @className（类名称）: SelectWithdrawDto
 * @description（类描述）: this is the SelectWithdrawDto class
 * @author（创建人）: ${arli}
 * @createDate（创建时间）: 2021/12/25
 * @version（版本）: v1.0
 */
@Data
public class SelectWithdrawDto {

    //日期
    private LocalDate date;
    //多少条
    private  Integer size;

    //提现记录
    private List<ShopWithdraw>  shopWithdraws;

}
