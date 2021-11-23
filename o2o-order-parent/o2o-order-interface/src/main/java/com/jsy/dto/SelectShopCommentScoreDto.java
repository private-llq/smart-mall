package com.jsy.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @className（类名称）: selectShopCommentScoreDto
 * @description（类描述）: this is the selectShopCommentScoreDto class
 * @author（创建人）: ${arli}
 * @createDate（创建时间）: 2021/11/23
 * @version（版本）: v1.0
 */
@Data
public class SelectShopCommentScoreDto implements Serializable {
   //分数
    private Double  score;
    //数量
    private Integer size;
}
