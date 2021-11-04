package com.jsy.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CategoryVo {
    /**
     * uuid主键
     */
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

