package com.jsy.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jsy.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author yu
 * @since 2021-11-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("w_shop_tree")
public class Tree implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    /**
     * 菜单名称
     */
    private String name;

    /**
     * 父级id
     */
    private Integer parentId;


    /**
     * 图片路径
     */
    private String imgPath;

    /**
     * 排序序号
     */
    private Integer ranks;

    /**
     * 级别 B端保留字段
     */
    private Integer level;

    /**
     * 子级菜单
     */
    @TableField(exist = false)
    private List<Tree> childrens=new ArrayList<>();

    /**
     * 逻辑删除
     */
    @JsonIgnore// 不需要返回给前端
    @TableLogic
    private Long deleted;


}
