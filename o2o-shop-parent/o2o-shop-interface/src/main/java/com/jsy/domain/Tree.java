package com.jsy.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("t_shop_tree")
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


}
