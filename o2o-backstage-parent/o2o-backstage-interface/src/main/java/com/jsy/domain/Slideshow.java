package com.jsy.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.jsy.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 轮播图
 * </p>
 *
 * @author Tian
 * @since 2021-11-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("w_slideshow")
@ApiModel(value="Slideshow对象", description="轮播图")
public class Slideshow extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;




    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "图片")
    private String image;

    @ApiModelProperty(value = "标题")
    private String title;


}
