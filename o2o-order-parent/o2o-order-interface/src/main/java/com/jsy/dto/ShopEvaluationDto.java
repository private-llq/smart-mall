package com.jsy.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;


@EqualsAndHashCode(callSuper = false)
@TableName("t_shop_evaluation")
@Data
public class ShopEvaluationDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String uuid;
    /**
     * 店铺id
     */
    private String shopUuid;
    /**
     *
     */
    private String userUuid;
    /**
     * 订单信息
     */
    private String evaluateMessage;

    private String orderUuid;
    /**
     * 评价星级，1星，2星，3星，4星，5星
     */
    private Integer evaluateLevel;

    /**
     * 图片
     */
    private String image;

    /**
     * 姓名
     */
    private String name;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    @TableField(exist = false)
    private List<ChatForEvaluationDto> list;
}
