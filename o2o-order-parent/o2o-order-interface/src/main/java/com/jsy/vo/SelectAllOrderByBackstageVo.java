package com.jsy.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.jsy.BaseEntity;
import com.jsy.domain.OrderGoods;
import com.jsy.domain.OrderService;
import com.jsy.domain.OrderSetMenu;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @className（类名称）: SelectAllOrderByBackstageVO
 * @description（类描述）: this is the SelectAllOrderByBackstageVO class
 * @author（创建人）: ${arli}
 * @createDate（创建时间）: 2021/12/25
 * @version（版本）: v1.0
 */
@Data
public class SelectAllOrderByBackstageVo  {

    private Long id;


    private LocalDateTime createTime;


    @ApiModelProperty(value = "c端用户id")
    private Long userId;

    @ApiModelProperty(value = "b端商家id")
    private Long shopId;

    @ApiModelProperty(value = "订单编号")
    private String orderNum;

    @ApiModelProperty(value = "支付时间")
    private LocalDateTime payTime;

    @ApiModelProperty(value = "消费方式（0用户到店，1商家上门）")
    private Integer consumptionWay;

    @ApiModelProperty(value = "订单的最终价格")
    private BigDecimal orderAllPrice;

    @ApiModelProperty(value = "联系人")
    private String linkman;

    @ApiModelProperty(value = "验卷状态")//验卷状态0未验卷，1验卷成功
    private Integer serverCodeStatus;
    @ApiModelProperty(value = "预约状态（0预约中，1预约成功）")
    private Integer appointmentStatus;
    @ApiModelProperty(value = "是否评价0未评价，1评价（评价完成为订单完成）")
    private Integer commentStatus;
    @ApiModelProperty(value = "支付状态（0未支付，1支付成功,2退款中，3退款成功，4拒绝退款）")
    private Integer payStatus;
    @ApiModelProperty(value = "订单状态（[1待上门、待配送、待发货]，2、完成）")
    private Integer orderStatus;
    @ApiModelProperty(value = "申请退款角色（0商家，1是平台）")
    private Integer refundApplyRole;



    @ApiModelProperty(value = "商品集合")
    private List<OrderGoodsVO> orderGoodsList;
    @ApiModelProperty(value = "套餐集合")
    private List<OrderSetMenuVO> orderSetMenuList;
    @ApiModelProperty(value = "服务集合")
    private List<OrderServiceVO> orderServiceList;

}
