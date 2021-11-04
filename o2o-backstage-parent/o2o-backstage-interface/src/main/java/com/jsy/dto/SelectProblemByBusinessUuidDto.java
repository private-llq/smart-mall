package com.jsy.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import java.io.Serializable;


@Data
@ApiModel("根据相关模块uuid查询下面的问题返回对象")
public class SelectProblemByBusinessUuidDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * uuid
     */
    @TableId(value = "uuid", type = IdType.ASSIGN_UUID)
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
