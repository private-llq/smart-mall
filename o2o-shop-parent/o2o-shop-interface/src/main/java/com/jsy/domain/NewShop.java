package com.jsy.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.jsy.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 * 新——店铺表
 * </p>
 *
 * @author Tian
 * @since 2021-11-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("w_new_shop")
@ApiModel(value="NewShop对象", description="新——店铺表")
@NoArgsConstructor
@AllArgsConstructor
public class NewShop extends BaseEntity  {

    private static final long serialVersionUID = 1L;

//    @ApiModelProperty(value = "店铺id")
//    @TableId(value = "id", type = IdType.AUTO)
//    private Long id;


    @ApiModelProperty(value = "店铺拥有者uuid")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long ownerUuid;

    @ApiModelProperty(value = "店铺所在城市")
    private String city;

    @ApiModelProperty(value = "店铺名称")
    private String shopName;

    @ApiModelProperty(value = "店铺联系电话")
    private String mobile;

    @ApiModelProperty(value = "门店类型（与w_SHOP_TREE行业关联），用逗号分隔")
    private String shopTreeId;

    @ApiModelProperty(value = "店铺logo(file_url)")
    private String shopLogo;

    @ApiModelProperty(value = "营业执照图片(file_uuid)")
    private String businessImg;

    @ApiModelProperty(value = "注册号（工商户注册号，统一信用代码）")
    private String businessNumber;

    @ApiModelProperty(value = "公司名称（与营业执照的名称一致）")
    private String companyName;

    @ApiModelProperty(value = "店铺法人/经营者姓名")
    private String shopUsername;

    @ApiModelProperty(value = "店铺法人/经营者电话")
    private String shopPhone;

    @ApiModelProperty(value = "联系人身份证正面fileurl")
    private String shopCidZimg;

    @ApiModelProperty(value = "联系人身份证反面fileurl")
    private String shopCidFimg;

    @ApiModelProperty(value = "身份证名字")
    private String idName;

    @ApiModelProperty(value = "身份证号")
    private String idNumber;

    @ApiModelProperty(value = "银行卡图片")
    private String bankImages;

    @ApiModelProperty(value = "银行卡卡号")
    private String bankNumber;

    @ApiModelProperty(value = "开户支行")
    private String depositBank;

    @ApiModelProperty(value = "开户银行")
    private String branchBank;

    @ApiModelProperty(value = "支付宝账号")
    private String aliPay;

    @ApiModelProperty(value = "微信账号")
    private String weChat;

    @ApiModelProperty(value = "详细地址（定位）")
    private String addressDetail;

    @ApiModelProperty(value = "营业执照地址）")
    private String businessAddress;


    @ApiModelProperty(value = "经营类型（1-服务行业   0-套餐行业）")
    private Integer type;

    @ApiModelProperty(value = "证件照片（展示给C端看的证件）")
    private String papers;

    @ApiModelProperty(value = "店铺环境照片")
    private String shopImages;

    @ApiModelProperty(value = "营业时间1")
    private String businessHours1;

    @ApiModelProperty(value = "营业时间2")
    private String businessHours2;

    @ApiModelProperty(value = "店铺公告")
    private String notice;

    @ApiModelProperty(value = "经度")
    private BigDecimal longitude;

    @ApiModelProperty(value = "维度")
    private BigDecimal latitude;

    @ApiModelProperty(value = "距离多远")
    @TableField(exist = false)
    private BigDecimal distance;

    @ApiModelProperty(value = "审核状态 0未审核 1已审核 2审核未通过  3资质未认证")
    private Integer state;

    @ApiModelProperty(value = "是否被屏蔽   1已屏蔽  0未屏蔽")
    private Integer shielding;

    @ApiModelProperty(value = "是否支持上门服务：0 不支持 1 支持")
    private Integer isVisitingService;
    @ApiModelProperty(value = "是否是虚拟店铺  0不是 1是")
    private Integer isVirtualShop;

    @ApiModelProperty(value = "是否是官方店铺  0不是 1是")
    private Integer isOfficialShop;
}
