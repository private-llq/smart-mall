package com.jsy.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsy.basic.util.PageInfo;
import com.jsy.basic.util.exception.JSYException;
import com.jsy.basic.util.utils.BeansCopyUtils;
import com.jsy.basic.util.utils.GouldUtil;
import com.jsy.client.NewShopClient;
import com.jsy.domain.Goods;
import com.jsy.domain.UserAddr;
import com.jsy.dto.NewShopDto;
import com.jsy.dto.UserAddrDto;
import com.jsy.mapper.UserAddrMapper;
import com.jsy.param.UserAddrParam;
import com.jsy.query.UserAddrQuery;
import com.jsy.service.IUserAddrService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhsj.baseweb.support.ContextHolder;
import com.zhsj.baseweb.support.LoginUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
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

    @Autowired
    private NewShopClient newShopClient;


    /**
     * 用户添加地址
     * @param userAddrParam
     * @return
     */
    @Override
    @Transactional
    public void addUserAddr(UserAddrParam userAddrParam) {
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
        if (Objects.isNull(loginUser)){
            new JSYException(-1,"用户认证失败！");
        }

        Integer integer = userAddrParam.getDefaultAddress();
        if (Objects.isNull(integer)){
            throw new JSYException(-1,"默认地址参数不能为空！");
        }

        UserAddr userAddr = new UserAddr();
        userAddr.setUserId(loginUser.getId());
        BeanUtils.copyProperties(userAddrParam,userAddr);

        if (integer==1){
            int update = useraddrmapper.update(null, new UpdateWrapper<UserAddr>().set("default_address", 0));//把之前的默认地址全部重置为0
        }

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
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
        if (Objects.isNull(loginUser)){
            new JSYException(-1,"用户认证失败！");
        }
        Integer integer = userAddrParam.getDefaultAddress();

        if (Objects.isNull(integer)){
            throw new JSYException(-1,"默认地址参数不能为空！");
        }
        UserAddr userAddr = new UserAddr();
        userAddr.setUserId(loginUser.getId());
        BeanUtils.copyProperties(userAddrParam,userAddr);

        if (integer==1){
            int update = useraddrmapper.update(null, new UpdateWrapper<UserAddr>().set("default_address", 0));//把之前的默认地址全部重置为0
        }

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
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
        if (Objects.isNull(loginUser)){
            throw new JSYException(-1,"用户认证失败！");
        }

        Page<UserAddr> page = new Page<>(userAddrQuery.getPage(),userAddrQuery.getRows());
        Page<UserAddr> userAddrPage = useraddrmapper.selectPage(page, new QueryWrapper<UserAddr>().eq("user_id",loginUser.getId()));
        List<UserAddr> records = userAddrPage.getRecords();
        ArrayList<UserAddrDto> addrDtos = new ArrayList<>();
        records.forEach(x->{
            UserAddrDto userAddrDto = new UserAddrDto();
            BeanUtils.copyProperties(x,userAddrDto);
            userAddrDto.setId(String.valueOf(x.getId()));
            addrDtos.add(userAddrDto);
        });

        Integer index=null;
        for (UserAddrDto addrDto : addrDtos) {
            if (addrDto.getDefaultAddress()==1){
               index= addrDtos.indexOf(addrDto);
            }
        }
        if (Objects.nonNull(index)){
            Collections.swap(addrDtos,index,0);

        }
        PageInfo<UserAddrDto> pageInfo = new PageInfo<>();
        pageInfo.setSize(userAddrPage.getSize());
        pageInfo.setRecords(addrDtos);
        pageInfo.setCurrent(userAddrPage.getCurrent());
        pageInfo.setTotal(userAddrPage.getTotal());
        return pageInfo;
    }


    /**
     * 查询店铺到用户所在位置的距离
     * @param shopId
     * @return
     */
    @Override
    public String getDistance(Long shopId, BigDecimal userLongitude, BigDecimal userLatitude) {
        NewShopDto data = newShopClient.get(shopId).getData();
        if (Objects.isNull(data)){
            throw new JSYException(-1,"店铺信息异常！");
        }
        if (Objects.isNull(userLatitude)){
            throw new JSYException(-1,"经度不能为空！");
        }
        if (Objects.isNull(userLatitude)){
            throw new JSYException(-1,"维度不能为空！");
        }
        BigDecimal longitude = data.getLongitude();
        BigDecimal latitude = data.getLatitude();
        long distance = GouldUtil.getApiDistance(longitude + "," + latitude, userLongitude + "," + userLatitude);
        return distance/1000+"km";
    }


    /**
     * 用户的设置默认地址  默认地址 1 默认 0 否
     */
    @Override
    public void setUserAddr(Long id, Integer state) {
        if (state==1){
            int update = useraddrmapper.update(null, new UpdateWrapper<UserAddr>().set("default_address", 0));
            if (update>=0){
                useraddrmapper.update(null, new UpdateWrapper<UserAddr>().eq("id", id).set("default_address", 1));
            }
        }
        if (state==0){
            useraddrmapper.update(null,new UpdateWrapper<UserAddr>().eq("id",id).set("default_address",0));
        }
    }

}
