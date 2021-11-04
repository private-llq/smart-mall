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
@TableName("t_business_problem")
public class BusinessProblem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * uuid
     */
    @TableId(value = "uuid", type = IdType.AUTO)
    private String uuid;

    /**
     * 序号
     */
    private Integer serial;

    /**
     * 问题
     */
    private String problem;

    /**
     * 业务咨询uuid
     */
    private String businessUuid;

    /**
     * 回复答案
     */
    private String replyContent;

    /**
     * 回复图片
     */
    private String replyImg;

    /**
     * 回复链接
     */
    private String replyUrl;


}
