package com.jsy.config;

import lombok.Data;

/**
 * @className（类名称）: AliPayVO
 * @description（类描述）: this is the AliPayVO class
 * @author（创建人）: ${arli}
 * @createDate（创建时间）: 2021/12/14
 * @version（版本）: v1.0
 */
@Data
public class AliPayVO {
    private String orderStr;
    private String orderNum;
}
