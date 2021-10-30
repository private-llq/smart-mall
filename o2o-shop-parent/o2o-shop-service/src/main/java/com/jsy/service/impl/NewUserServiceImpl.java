package com.jsy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jsy.basic.util.PageList;
import com.jsy.basic.util.exception.JSYException;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.client.OrderClient;
import com.jsy.domain.NewUser;
import com.jsy.domain.Order;
import com.jsy.mapper.NewUserMapper;
import com.jsy.query.OrderQuery;
import com.jsy.service.INewUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yu
 * @since 2021-03-31
 */
@Service
public class NewUserServiceImpl extends ServiceImpl<NewUserMapper, NewUser> implements INewUserService {

    @Resource
    private NewUserMapper newUserMapper;

    @Resource
    private OrderClient orderClient;



    @Override
    public NewUser getNewUser(String shopUuid) {
        long localTime = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
        NewUser newUser = newUserMapper.selectOne(new QueryWrapper<NewUser>().eq("shop_uuid", shopUuid));
        if (Objects.nonNull(newUser)){
            if (localTime >= newUser.getStartTime().toEpochSecond(ZoneOffset.of("+8")) &&
                    localTime <= newUser.getEndTime().toEpochSecond(ZoneOffset.of("+8"))&& newUser.getDeleted()!=0){
                return newUser;
            }
        }
        return null;
    }

    @Override
    public String isNewUser(String shopUuid, String userUuid) {
        NewUser newUser = this.getNewUser(shopUuid);
        //有进行中的新客活动
        if (Objects.nonNull(newUser)){
            String newUserUuid = newUser.getUuid();
            long  startTime=newUser.getStartTime().toEpochSecond(ZoneOffset.of("+8"));
            long  endTime= newUser.getEndTime().toEpochSecond(ZoneOffset.of("+8"));

            OrderQuery orderQuery = new OrderQuery();
            orderQuery.setPayState("1");
            orderQuery.setShopUuid(shopUuid);
            orderQuery.setShopUuid(userUuid);
            CommonResult<PageList<Order>> pageListCommonResult = orderClient.pageOrder(orderQuery);

            List<Order> rows = pageListCommonResult.getData().getRows();
            List<Order> collect = rows.stream().filter(x -> {
                //是否在该时间重复下过单
                if (x.getCreateTime().toEpochSecond(ZoneOffset.of("+8")) >= startTime && x.getCreateTime().toEpochSecond(ZoneOffset.of("+8")) <= endTime) {
                    return true;
                }
                return false;
            }).collect(Collectors.toList());

            if (collect.size()==0){//首单
                return newUserUuid;
            }else {//非首单


                return null;
            }
        }else {
            return null;
        }


    }

    @Override
    public NewUser newestNewUser(String shopUuid) {
        long localTime = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
        List<NewUser> newUserList = newUserMapper.selectList(new QueryWrapper<NewUser>().eq("shop_uuid",shopUuid));
        if (newUserList.size()==0){
            throw new JSYException(-1,"该店没有历史活动");
        }
        NewUser newUser = newUserList.get(newUserList.size() - 1);


        if (newUser.getDeleted() == 0) {
            newUser.setState(2);//已撤销
        }
        if (localTime >= newUser.getStartTime().toEpochSecond(ZoneOffset.of("+8"))
                && localTime <= newUser.getEndTime().toEpochSecond(ZoneOffset.of("+8"))
                && newUser.getState()!= 0) {
            newUser.setState(1);//进行中
        }
        if (localTime > newUser.getEndTime().toEpochSecond(ZoneOffset.of("+8"))) {
            newUser.setState(3);//已过期
        }
        if (localTime<newUser.getStartTime().toEpochSecond(ZoneOffset.of("+8"))){
            newUser.setState(4);//未开始
        }
        return newUser;
    }
}
