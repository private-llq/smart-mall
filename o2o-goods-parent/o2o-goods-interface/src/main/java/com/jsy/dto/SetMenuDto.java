package com.jsy.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jsy.BaseEntity;
import com.jsy.domain.SetMenuGoods;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 
 * </p>
 *
 * @author lijin
 * @since 2021-11-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("w_set_menu")
@ApiModel(value="SetMenu对象", description="")
public class SetMenuDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    @ApiModelProperty(value = "套餐名称")
    private String name;

//    @ApiModelProperty(value = "服务特点表ids 逗号隔开")
//    private List<ServiceCharacteristicsDto> serviceCharacteristicsIds;

    @ApiModelProperty(value = "真实价格")
    private BigDecimal realPrice;

    @ApiModelProperty(value = "售卖价格")
    private BigDecimal sellingPrice;

    @ApiModelProperty(value = "图片（最大三张）")
    private String images;


    @ApiModelProperty(value = "套餐详情")
    @TableField(exist = false)
    private  Map<String, List<SetMenuGoodsDto>> map;

    @ApiModelProperty(value = "套餐说明")
    private String menuExplain;


//    @ApiModelProperty(value = "是否支持上门服务 0 不支持 1 支持")
//    private Integer isVisitingService;

    @ApiModelProperty(value = "套餐访问量")
    private Long pvNum;

    private Long shopId;

}
