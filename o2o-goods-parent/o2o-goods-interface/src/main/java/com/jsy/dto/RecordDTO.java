package com.jsy.dto;

import io.swagger.annotations.Api;
import lombok.Data;

@Data
@Api("抢购用户记录dto")
public class RecordDTO{
    /**
     * 用户uuid
     */
    private String userUuid;
    /**
     * 用户名
     */
    private String name;
    /**
     * 用户电话号码
     */
    private String phone;

}
