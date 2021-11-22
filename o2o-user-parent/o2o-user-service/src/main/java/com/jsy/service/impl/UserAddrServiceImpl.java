package com.jsy.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsy.basic.util.PageInfo;
import com.jsy.basic.util.exception.JSYException;
import com.jsy.basic.util.utils.BeansCopyUtils;
import com.jsy.domain.UserAddr;
import com.jsy.dto.UserAddrDto;
import com.jsy.mapper.UserAddrMapper;
import com.jsy.param.UserAddrParam;
import com.jsy.query.UserAddrQuery;
import com.jsy.service.IUserAddrService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 用户地址管理表	 服务实现类
 * </p>
 *
 * @author yu
 * @since 2021-11-22
 */
@Service
public class UserAddrServiceImpl extends ServiceImpl<UserAddrMapper, UserAddr> implements IUserAddrService {

    @Autowired
    private UserAddrMapper useraddrmapper;


    /**
     * 用户添加地址
     * @param userAddrParam
     * @return
     */
    @Override
    @Transactional
    public void addUserAddr(UserAddrParam userAddrParam) {
        UserAddr userAddr = new UserAddr();
        BeanUtils.copyProperties(userAddrParam,userAddr);
        useraddrmapper.insert(userAddr);

    }

    /**
     * 用户修改地址
     * @param userAddrParam
     * @return
     */
    @Override
    @Transactional
    public void updateUserAddr(UserAddrParam userAddrParam) {
        UserAddr userAddr = new UserAddr();
        BeanUtils.copyProperties(userAddrParam,userAddr);
        useraddrmapper.updateById(userAddr);
    }

    /**
     * 用户删除地址
     * @param id
     */
    @Override
    @Transactional
    public void deleteUserAddr(Long id) {
        useraddrmapper.deleteById(id);
    }

    /**
     * 地址分页列表
     * @param userAddrQuery 查询对象
     * @return  分页对象
     */
    @Override
    public PageInfo<UserAddrDto> UserAddrPageList(UserAddrQuery userAddrQuery) {
        if (Objects.isNull(userAddrQuery.getUserId())){
            throw new JSYException(-1,"用户id为空");
        }
        Page<UserAddr> page = new Page<>(userAddrQuery.getPage(),userAddrQuery.getRows());
        Page<UserAddr> userAddrPage = useraddrmapper.selectPage(page, new QueryWrapper<UserAddr>().eq("user_id",userAddrQuery.getUserId()));
        List<UserAddr> records = userAddrPage.getRecords();
        List<UserAddrDto> userAddrDtos = BeansCopyUtils.listCopy(records, UserAddrDto.class);
        PageInfo<UserAddrDto> pageInfo = new PageInfo<>();
        pageInfo.setSize(userAddrPage.getSize());
        pageInfo.setRecords(userAddrDtos);
        pageInfo.setCurrent(userAddrPage.getCurrent());
        pageInfo.setTotal(userAddrPage.getTotal());
        return pageInfo;
    }



}
