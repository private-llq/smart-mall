package com.jsy.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author yu
 * @since 2021-03-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_shop_poster")
public class ShopPoster implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String uuid;

    /**
     * 有效期 0：永久有效 1：自定义时间段
     */
    private Integer termValidity;

    /**
     * 海报图片
     */
    private String posterImage;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 文案标题
     */
    private String mainTitle;

    /**
     * 文案副标题
     */
    private String subTitle;

    /**
     * 店家uuid
     */
    private String shopUuid;

    /**
     * 启用状态： 0 撤销
     */
    private Integer state;
    /**
     * 海报排序 字段
     */
    private Integer sort;

}
