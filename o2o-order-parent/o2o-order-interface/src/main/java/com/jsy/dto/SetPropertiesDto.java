package com.jsy.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author yu
 * @since 2021-02-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_set_properties")
public class SetPropertiesDto implements Serializable {

    private static final long serialVersionUID = 1L;


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 未支付订单结束间隔时间
     */
    private String nopay;

    /**
     * 完成订单间隔多久好评
     */
    private String finishpay;

    /**
     * 哪个模块
     */
    private String forModule;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;


}
