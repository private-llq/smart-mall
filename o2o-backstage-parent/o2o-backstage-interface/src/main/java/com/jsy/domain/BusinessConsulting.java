package com.jsy.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author yu
 * @since 2021-06-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_business_consulting")
public class BusinessConsulting implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * uuid
     */
    @TableId(value = "uuid", type = IdType.AUTO)
    private String uuid;

    /**
     * 业务咨询名字
     */
    private String consultingName;

    /**
     * 图片
     */
    private String img;

    /**
     * 0业务咨询1联系客服
     */
    private Integer type;

    /**
     * 级别
     */
    private Integer level;

    /**
     * 父级uuid
     */
    private String parent;


}
