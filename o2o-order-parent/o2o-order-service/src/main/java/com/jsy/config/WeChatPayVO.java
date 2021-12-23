package com.jsy.config;

import lombok.Data;

/**
 * @className（类名称）: WeChatPayVO
 * @description（类描述）: this is the WeChatPayVO class
 * @author（创建人）: ${arli}
 * @createDate（创建时间）: 2021/12/14
 * @version（版本）: v1.0
 */
@Data
public class WeChatPayVO {

    //private String  package;
    private String  appid;
    private String   sign;
    private String  orderNum;
    private String  prepayid;
    private String  noncestr;
    private String  timestamp;
}
