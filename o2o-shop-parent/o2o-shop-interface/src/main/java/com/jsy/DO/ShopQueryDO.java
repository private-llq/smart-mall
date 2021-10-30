package com.jsy.DO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
  * @Author lxr
  * @Description 查询满足条件的商品或者店铺，数组组装代码执行
  * @Date 2020/12/3 15:17
  * @Param
  **/
@Data
@ApiModel("首页关键字查询数据库do")
public class ShopQueryDO {

    @ApiModelProperty("商品或者店铺的uuid")
    private String uuid;

    @ApiModelProperty("商品或者店铺的名称")
    private String name;

    @ApiModelProperty("店铺的uuid")
    private String shopUuid;

    @ApiModelProperty("商品图片url")
    private String goodsImage;

    @ApiModelProperty("店铺图片url")
    private String shopImage;

    @ApiModelProperty("店铺的名称")
    private String shopName;

    @ApiModelProperty("查询信息属于那个表")
    private String tb;

}
