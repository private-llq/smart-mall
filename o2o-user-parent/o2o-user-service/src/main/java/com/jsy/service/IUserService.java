package com.jsy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jsy.basic.util.PageList;
import com.jsy.domain.User;
import com.jsy.dto.LoginDTO;
import com.jsy.query.UserQuery;

import java.util.List;


public interface IUserService extends IService<User> {

    PageList<User> queryByPage(UserQuery query);

    LoginDTO  queryUserLogin(User user);

    String updateUser(User user);

    String addUser(User user);

    List<User> getUserList(String ids);

    User getByUuid(String uuid);

}
