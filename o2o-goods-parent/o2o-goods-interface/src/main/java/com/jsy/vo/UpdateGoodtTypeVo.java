package com.jsy.vo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@Data
@ApiModel(value = "修改商品的分类vo")
public class UpdateGoodtTypeVo {
    @ApiModelProperty(value = "商品的uuid",name = "gUuid")
   private   String gUuid;
    @ApiModelProperty(value = "商品分类的uuid",name = "newTypeUuid")
   private    String newTypeUuid;
}
