package com.jsy.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class MatchTheUserDto implements Serializable {
    /**
     *用户昵称
     */
    private String userName;
    /**
     * 登录状态
     */
    private Boolean state;

    /**
     * 下线分钟数据
     */
    private Long outTime;
    /**
     * 分类名称
     */
    private String treeName;
    /**
     * 距离
     */
    private String distance;

    /**
     * 用户头像
     */
    private String headImg;
}
