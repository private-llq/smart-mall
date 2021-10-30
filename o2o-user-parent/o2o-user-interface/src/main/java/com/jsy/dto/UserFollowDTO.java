package com.jsy.dto;
import com.baomidou.mybatisplus.annotation.TableId;
import com.jsy.domain.Activity;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserFollowDTO implements Serializable {

    /**
     * id主键
     */
    @TableId(value = "uuid")
    private String uuid;
    /**
     * 用户uuid
     */
    private String userUuid;
    /**
     * 商店uuid
     */
    private String shopUuid;
    /**
     * 商店名称
     */
    private String name;
    /**
     * 商店地址
     */
    private String addressDetail;
    /**
     * 商店Logo
     */
    private String shopLogo;

    /**
     * 商店活动
     */
    private List<Activity> shopActivity =new ArrayList();



}
