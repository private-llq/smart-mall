package com.jsy.vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Value;

import java.util.ArrayList;

@Data
@ApiModel(value = "上下架商品vo")
public class ShelfTypeVo {
    @ApiModelProperty(value = "UUid集合",name = "uuids")
    private     ArrayList<String> uuids;
    @ApiModelProperty(value = "onshelf:上架/outshelf:下架",name = "shelfType")
    private      String shelfType;
}
