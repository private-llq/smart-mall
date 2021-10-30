package com.jsy.vo;


import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel(description = "用户关注")
@Data
public class UserFollowVo {
    /**
     * uuid
     */
    private String uuid;
    /**
     * 用户uuid
     */
    private String userUuid;
    /**
     * 商店uuid
     */
    private String shopUuid;
}
