package com.jsy.domain;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * <p>
 * </p>
 * @author yu
 * @since 2021-03-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_new_user")
public class NewUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    private String uuid;

    /**
     * 开始时间
     */

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime endTime;

    /**
     * 新客立减 价格
     */
    private BigDecimal price;

    /**
     * 是否失效 0 ：失效（撤销,过期） 1：正常（未进行，进行中） ps:过期是根据时间自动识别返回，数据库不进行维护
     */
    private Integer deleted;

    /**
     * 返回给前端的状态码 1 : 进行中 2 已撤销 3 已过期 4未开始
     */
    @TableField(exist = false)
    private Integer state;

    /**
     * 商家外键
     */
    private String shopUuid;

    /**
     * 活动创建时间(只展示，不插入)
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime creatTime;

    /**
     * 活动撤销时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime revokeTime;


}
