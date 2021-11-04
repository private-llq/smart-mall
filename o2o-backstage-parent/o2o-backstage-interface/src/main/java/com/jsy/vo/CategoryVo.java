package com.jsy.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 大后台 行业服务分类
 * </p>
 *
 * @author yu
 * @since 2021-11-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CategoryVo  {

    private Long id;

    /**
     * 分类名称
     */
    private String classifyName;

    /**
     * 等级
     */
    private Integer level;

    /**
     * 父级id
     */
    private Long pid;

    /**
     * 是否显示
     */
    private Integer state;


}
