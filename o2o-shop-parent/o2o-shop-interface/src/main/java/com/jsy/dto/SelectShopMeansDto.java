package com.jsy.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Time;
import java.time.LocalDateTime;

@Data
@ApiModel("查询出店铺资料")
public class SelectShopMeansDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = "uuid", value = "店铺uuid")
    private String uuid;
    @ApiModelProperty(name = "mobile", value = "店铺联系电话")
    private String mobile;
    @ApiModelProperty(name = "linkman", value = "店铺联系人")
    private String linkman;
    @ApiModelProperty(name = "city", value = "所在城市")
    private String city;
    @ApiModelProperty(name = "addressDetail", value = "店铺详细地址）")
    private String addressDetail;
    @ApiModelProperty(name = "shopCidZimg", value = "身份证正面(file_url)")
    private String shopCidZimg;
    @ApiModelProperty(name = "shopCidFimg", value = "身份证反面(file_url)")
    private String shopCidFimg;
    @ApiModelProperty(name = "shopName", value = "店铺名称")
    private String shopName;
    @ApiModelProperty(name = "businessImg", value = "营业执照图片地址(file_url)")
    private String businessImg;
    @ApiModelProperty(name = "shopLogo", value = "店铺logo图片(file_url)")
    private String shopLogo;
    @ApiModelProperty(name = "shopFront", value = "店铺门脸图片(file_url)")
    private String shopFront;
    @ApiModelProperty(name = "deliveryArea", value = "配送范围(单位米)")
    private Long deliveryArea;
    @ApiModelProperty(name = "credentials", value = "证件路径的url用;隔开")
    private String credentials;
    @ApiModelProperty(name = "shopManageType", value = "经营品类")
    private String shopManageType;
    @ApiModelProperty(name = "shopGenre", value = "门店类型")
    private String shopGenre;
    @ApiModelProperty(name = "shop_sent", value = "是否是服务（1是服务类0商品类）")
    private String shopSent;

}
