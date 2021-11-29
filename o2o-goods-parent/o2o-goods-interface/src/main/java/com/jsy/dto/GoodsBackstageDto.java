package com.jsy.dto;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class GoodsBackstageDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品编号")
    private String goodsNumber;

    @ApiModelProperty(value = "大后台：排序序号")
    private Long sort;

    @ApiModelProperty(value = "商品名称/服务 标题")
    private String title;

    @ApiModelProperty(value = "商品/服务 分类名称")
    private String goodsTypeName;

    @ApiModelProperty(value = "发布商家 名称")
    private String shopName;

    @ApiModelProperty(value = "创建时间")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "大后台：商品状态 0 启用 1禁用")
    private Integer state;
}
