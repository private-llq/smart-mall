package com.jsy.service;

import com.jsy.domain.NewUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yu
 * @since 2021-03-31
 */
public interface INewUserService extends IService<NewUser> {

   NewUser getNewUser(String shopUuid);

//   String isNewUser(String shopUuid, String userUuid);

    NewUser newestNewUser(String shopUuid);
}
