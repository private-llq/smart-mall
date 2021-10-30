package com.jsy.service.impl;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jsy.basic.util.PageList;
import com.jsy.basic.util.RedisStateCache;
import com.jsy.basic.util.exception.JSYException;
import com.jsy.basic.util.utils.CurrentUserHolder;
import com.jsy.basic.util.utils.JwtUtils;
import com.jsy.basic.util.utils.MyHttpUtils;
import com.jsy.basic.util.utils.UUIDUtils;
import com.jsy.basic.util.vo.UserDto;
import com.jsy.domain.User;
import com.jsy.dto.LoginDTO;
import com.jsy.mapper.UserMapper;
import com.jsy.query.UserQuery;
import com.jsy.service.IUserService;
import org.apache.http.client.methods.HttpGet;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

   static final String  group="LoginCode:";

    @Resource
    private UserMapper userMapper;


    @Autowired
    private RedisStateCache redisStateCache;


    @Override
    public PageList<User> queryByPage(UserQuery query) {
        Page<User> page = new Page<User>(query.getPage(),query.getRows());
        page = super.page(page);
        QueryWrapper queryWrapper=new QueryWrapper();
        if(Objects.nonNull(query.getName())){
            queryWrapper.like("name",query.getName());
        }
        if(Objects.nonNull(query.getPhone())){
            queryWrapper.like("phone",query.getPhone());
        }
        return new PageList<User>(page.getTotal(),page.getRecords());
    }

    @Override
    public LoginDTO queryUserLogin(User user) {
        if(Objects.isNull(user.getPhone()) || user.getPhone()==""){
            throw new JSYException(-1,"请输入电话号码");
        }
        if(Objects.isNull(user.getPassword()) || user.getPassword()==""){
            throw new JSYException(-1,"请输入密码");
        }
        //查询当前用户信息
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("phone",user.getPhone());
        User userDto=baseMapper.selectOne(queryWrapper);

        if (null == userDto) {
            throw new JSYException(-1,"登录失败，该电话号码未注册用户!");
        }
        //加密密码
        if (!user.getPassword().equals(userDto.getPassword())) {
            throw new JSYException(-1,"电话号码或者密码错误");
        }
        UserDto returnUser=new UserDto();
        BeanUtils.copyProperties(userDto,returnUser);
        returnUser.setUserType(1);
        //查询单位信息
        String token = JwtUtils.createUserToken(returnUser);
        LoginDTO loginDTO=new LoginDTO();
        loginDTO.setToken(token);
        loginDTO.setUser(returnUser);
        return loginDTO;
    }

    @Override
    public String updateUser(User user) {
        UserDto dto= CurrentUserHolder.getCurrentUser();
        if(!user.getUuid().equals(dto.getUuid())){
            throw new JSYException(-1,"用户信息验证不通过");
        }
        user.setRelationUuid(null);
        if(!StringUtils.isEmpty(user.getPhone())){
            User getUser=getByUuid(user.getUuid());
            if(getUser.getPhone().equals(user.getPhone())){
                return user.getUuid();
            }
            List<User> userList=baseMapper.selectList(new QueryWrapper<User>().eq("phone",user.getPhone()));
            if (userList.size()>0){
                throw new JSYException(-1,"该电话号码已经被注册");
            }
        }
        if(!StringUtils.isEmpty(user.getPassword()) && (user.getPassword().length()<6 || user.getPassword().length()>12)){
            throw new JSYException(-1,"密码请保持在6-12位之间");
        }
        user.setUuid(null);
        baseMapper.update(user,new QueryWrapper<User>().eq("uuid",user.getUuid()));
        return user.getUuid();
    }

    @Override
    public String addUser(User user) {
        String regex = "^((19[0-9])|(13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,4-9]))\\d{8}$";
        if(StringUtils.isEmpty(user.getPhone()) || StringUtils.isEmpty(user.getPassword())){
            throw new JSYException(-1,"请填写注册信息");
        }
        if(!user.getPhone().matches(regex)){
            throw new JSYException(-1,"请填写正确的手机号码");
        }
        if (user.getPassword().length()<6 || user.getPassword().length()>12){
            throw new JSYException(-1,"密码请保持在6-12位之间");
        }
        List<User> userList=baseMapper.selectList(new QueryWrapper<User>().eq("phone",user.getPhone()));
        if (userList.size()>0){
            throw new JSYException(-1,"该电话号码已经被注册");
        }
        user.setUuid(UUIDUtils.getUUID());
        baseMapper.insert(user);
        return user.getUuid();
    }

    @Override
    public List<User> getUserList(String ids) {
        String[] split = ids.split(",");
        List<String> idList = Arrays.asList(split);
        return userMapper.selectBatchIds(idList);
    }

    @Override
    public User getByUuid(String uuid) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("uuid",uuid);
        return baseMapper.selectOne(queryWrapper);
    }
}



