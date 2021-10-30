package com.jsy.vo;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class PassVo {

    @NotBlank(message = "请重新输入密码")
    @Size(max = 12, min = 6, message = "请输入6-12位密码，字母区分大小写")
    private String password;
}
