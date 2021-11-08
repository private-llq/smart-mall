package com.jsy.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jsy.dto.CartDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.poi.hpsf.Decimal;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_shop_info")
@ApiModel("店铺信息entity")
public class ShopInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "id", value = "店铺id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @ApiModelProperty(name = "uuid", value = "店铺uuid")
    private String uuid;
    @ApiModelProperty(name = "ownerUuid", value = "店铺拥有者uuid")
    private String ownerUuid;
    @ApiModelProperty(name = "shopTypeUuid", value = "店铺类型uuid")
    private String shopTypeUuid;
    @ApiModelProperty(name = "mobile", value = "店铺座机电话")
    private String mobile;
    @ApiModelProperty(name = "linkman", value = "店铺联系人")
    private String linkman;
    @ApiModelProperty(name = "status", value = "店铺状态（关于审核）")
    private Integer status;
    @ApiModelProperty(name = "city", value = "所在城市")
    private String city;
    @ApiModelProperty(name = "addressDetail", value = "店铺详细地址）")
    private String addressDetail;
    @ApiModelProperty(name = "shopUsername", value = "店铺法人姓名")
    private String shopUsername;
    @ApiModelProperty(name = "shopPhone", value = "店铺法人电话")
    private String shopPhone;
    @ApiModelProperty(name = "shopCidZimg", value = "身份证正面(file_url)")
    private String shopCidZimg;
    @ApiModelProperty(name = "shopCidFimg", value = "身份证反面(file_url)")
    private String shopCidFimg;
    @ApiModelProperty(name = "shopName", value = "店铺名称")
    private String shopName;
    @ApiModelProperty(name = "businessImg", value = "营业执照图片地址(file_url)")
    private String businessImg;
    @ApiModelProperty(name = "businessNumber", value = "注册号（工商户注册号，统一信用代码）")
    private String businessNumber;
    @ApiModelProperty(name = "shopLogo", value = "店铺logo图片(file_url)")
    private String shopLogo;
    @ApiModelProperty(name = "shopFront", value = "店铺门脸图片(file_url)")
    private String shopFront;
    @ApiModelProperty(name = "shopImages1", value = "店铺环境图片1(file_url)")
    private String shopImages1;
    @ApiModelProperty(name = "shopImages2", value = "店铺环境图片2(file_url)")
    private String shopImages2;
    @ApiModelProperty(name = "shopImages3", value = "店铺环境图片3(file_url)")
    private String shopImages3;
    @ApiModelProperty(name = "shopDescribe", value = "店铺描述")
    private String shopDescribe;
    @ApiModelProperty(name = "longitude", value = "店铺地址经度")
    private BigDecimal longitude;
    @ApiModelProperty(name = "latitude", value = "店铺地址纬度")
    private BigDecimal latitude;
    @ApiModelProperty(name = "star", value = "店铺星级")
    private Double star;
    @ApiModelProperty(name = "businessStatus", value = "营业状态1营业中，2休息中，3暂停营业")
    private Integer businessStatus;
    @ApiModelProperty(name = "shopServe", value = "店铺服务特色")
    private String shopServe;
    @ApiModelProperty(name = "creatTime", value = "店铺创建时间")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8")
    private LocalDateTime creatTime;
    @ApiModelProperty(name = "updateTime", value = "店铺修改时间")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8")
    private LocalDateTime updateTime;
    @ApiModelProperty(name = "deleted", value = "逻辑删除")
    @TableLogic
    private Integer deleted;
    @ApiModelProperty(name = "deliveryArea", value = "配送范围(单位米)")
    private Long deliveryArea;
    @ApiModelProperty(name = "notice", value = "店铺公告")
    private String notice;
    @ApiModelProperty(name = "credentials", value = "证件路径的url用;隔开")
    private String credentials;
    @ApiModelProperty(name = "orderReceivingPhone", value = "接单电话最多三个用;隔开")
    private  String orderReceivingPhone;
    @ApiModelProperty(name = "distributionWay", value = "配送方式")
    private String distributionWay;
    @ApiModelProperty(name = "shopManageTypeUuid", value = "经营品类uuid")
    private String shopManageTypeUuid;
    @ApiModelProperty(name = "shopGenreUuid", value = "门店类型")
    private String shopGenreUuid;
    @ApiModelProperty(name = "isListPicture", value = "列表展示还是大图展示 默认为 1 列表展示")
    private Integer isListPicture=1;
    @ApiModelProperty(name = "shopSent", value = "是否是服务（1是服务类0商品类）")
    private String shopSent;
    @ApiModelProperty(name = "shopNumber", value = "门店编号")
    private String shopNumber;
    @ApiModelProperty(name = "postage", value = "配送费")
    private BigDecimal postage;

    /**
     * 展示商家里面的购物车时
     */
    @TableField(exist = false)
    private CartDTO cartDTO;


    @ApiModelProperty(name = "industry_category_id", value = "门店类型id")
    private String industry_category_id;

    @ApiModelProperty(name = "businessAddress", value = "营业执照地址")
    private String businessAddress;
    @ApiModelProperty(name = "companyName",value = "公司名称  营业执照的名称")
    private String companyName;
    @ApiModelProperty(name = "idName", value = "证件姓名")
    private String idName;
    @ApiModelProperty(name = "idNumber", value = "身份证号码")
    private String idNumber;
    @ApiModelProperty(name = "bankImages", value = "银行资料/开户许可证(file_url)")
    private List<String> bankImages;
    @ApiModelProperty(name = "bankNumber", value = "银行卡号")
    private String bankNumber;
    @ApiModelProperty(name = "depositBank", value = "开户银行")
    private String depositBank;
    @ApiModelProperty(name = "branchBank", value = "开户支行")
    private String branchBank ;

    @ApiModelProperty(name = "aliPay", value = "支付宝账号")
    private String aliPay;
    @ApiModelProperty(name = "weChat", value = "微信账号")
    private String weChat ;

}
