package com.jsy.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jsy.basic.util.PageList;
import com.jsy.domain.UserFollow;
import com.jsy.dto.UserFollowDTO;
import com.jsy.query.UserFollowQuery;

public interface IUserFollowService  extends IService<UserFollow> {

    void follow(String shopUuid);

    PageList<UserFollowDTO> followList(UserFollowQuery userFollowQuery);


    void outFollow(String shopUuid);

    Integer FollowStatus(String shopUuid);
}
