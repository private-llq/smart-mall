package com.jsy.dto;

import com.jsy.basic.util.vo.UserDto;
import lombok.Data;

@Data
public class LoginDTO {
    private  String token;
    private UserDto user;
}
