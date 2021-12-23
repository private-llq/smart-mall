package com.jsy.query;

import lombok.Data;

/**
 * @className（类名称）: ShopSelectCommentAndReplyParam
 * @description（类描述）: this is the ShopSelectCommentAndReplyParam class
 * @author（创建人）: ${arli}
 * @createDate（创建时间）: 2021/12/10
 * @version（版本）: v1.0
 */
@Data
public class ShopSelectCommentAndReplyParam {
    private  Integer  isReply;//0所有1是的没有回复
    private Integer current;
    private  Integer amount;
    private   Long  shopId;
}
