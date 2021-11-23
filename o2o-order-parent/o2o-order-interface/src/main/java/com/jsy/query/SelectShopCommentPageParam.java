package com.jsy.query;

import lombok.Data;

/**
 * @className（类名称）: SelectShopCommentPageParam
 * @description（类描述）: this is the SelectShopCommentPageParam class
 * @author（创建人）: ${arli}
 * @createDate（创建时间）: 2021/11/22
 * @version（版本）: v1.0
 */
@Data
public class SelectShopCommentPageParam {
    private  Integer  isPicture;//0不管有没有图1是有图片的
    private Integer current;
    private  Integer amount;
    private   Long  shopId;


}
