package com.jsy.domain;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_user_follow")
public class UserFollow {
    private Long id;
    private String uuid;
    private String shopUuid;
    private String userUuid;
}
