package com.jsy.service.impl;

import cn.hutool.core.date.DateTime;
import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jsy.basic.util.PageList;
import com.jsy.basic.util.exception.JSYException;
import com.jsy.basic.util.utils.CurrentUserHolder;
import com.jsy.basic.util.utils.UUIDUtils;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.basic.util.vo.UserEntity;
import com.jsy.client.UserRedpackClient;
import com.jsy.config.RidissonLock;
import com.jsy.domain.ShopRedpacket;
import com.jsy.domain.UserRedpacket;
import com.jsy.dto.SelectShopRedpacketByUserDto;
import com.jsy.dto.SelectShopRedpacketDto;
import com.jsy.dto.UserGetRedPacketDto;
import com.jsy.mapper.ShopRedpacketMapper;
import com.jsy.parameter.ShopRedPacketParam;
import com.jsy.query.ShopRedpacketQuery;
import com.jsy.service.IShopRedpacketService;
//import io.seata.spring.annotation.GlobalTransactional;
import org.aspectj.weaver.ast.Var;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lijin
 * @since 2020-11-12
 */
@Service
public class ShopRedpacketServiceImpl extends ServiceImpl<ShopRedpacketMapper, ShopRedpacket> implements IShopRedpacketService {

    @Resource
    private ShopRedpacketMapper shopRedpacketMapper;

    @Resource
    private UserRedpackClient userRedpackClient;


    //创建红包
    @Override
    public boolean creationRedPack(ShopRedPacketParam shopRedPacketParam) {
        DateTime DateTime = new DateTime();
        LocalDateTime begintime = shopRedPacketParam.getBegintime();//开始时间
        LocalDateTime endtime = shopRedPacketParam.getEndtime();//结束时间
        if (begintime.toInstant(ZoneOffset.of("+8")).toEpochMilli() >= endtime.toInstant(ZoneOffset.of("+8")).toEpochMilli()) {
            throw new JSYException(-1, "结束时间不得大于开始时间");
        }
        if (begintime.toInstant(ZoneOffset.of("+8")).toEpochMilli() < DateTime.getTime()) {
            throw new JSYException(-1, "开始时间不得小于当前时间");
        }

        ShopRedpacket shopRedpacket = new ShopRedpacket();
        BeanUtils.copyProperties(shopRedPacketParam, shopRedpacket);
        shopRedpacket.setUuid(UUIDUtils.getUUID());
        int insert = shopRedpacketMapper.insert(shopRedpacket);
        if (insert > 0) {
            return true;
        }
        return false;
    }

    //查询店铺红包信息
    @Override
    public SelectShopRedpacketDto selectShopRedpacket(String shopUuid) {
        ArrayList<ShopRedpacket> shopS = (ArrayList<ShopRedpacket>) shopRedpacketMapper.selectList(new QueryWrapper<ShopRedpacket>().eq("shop_uuid", shopUuid));
        ShopRedpacket shopRedpacket = new ShopRedpacket();
        shopRedpacket = shopS.get(shopS.size() - 1);
        SelectShopRedpacketDto shopRedpacketDto = new SelectShopRedpacketDto();
        BeanUtils.copyProperties(shopRedpacket, shopRedpacketDto);
        LocalDateTime endtime = shopRedpacket.getEndtime();
        LocalDateTime begintime = shopRedpacket.getBegintime();
        long l = begintime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        long la = endtime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        long lb = new Date().getTime();
        Integer l1 = (int) ((la - lb) / 1000 / 60 / 60 / 24);
        shopRedpacketDto.setResidue(l1);//剩余的（天）时间
        List<Integer> select = shopRedpacketMapper.selectYes(shopRedpacket.getUuid());
        int number = 0;
        for (Integer integer : select) {
            number = number + integer;
        }
        shopRedpacketDto.setYesMoney(number);//已经使用的金额
        shopRedpacketDto.setUseNumber(select.size());//使用数量
        List<Integer> integers = shopRedpacketMapper.selectNo(shopRedpacket.getUuid());
        int numberA = 0;
        for (Integer integer : integers) {
            numberA = numberA + integer;
        }
        shopRedpacketDto.setNoMoney(numberA);//未使用的金额

        return shopRedpacketDto;
    }

    //{用户}查看店铺红包信息
    @Override
    public SelectShopRedpacketByUserDto SelectShopRedpacketByUser(String shopUuid) {
        UserEntity userEntity = CurrentUserHolder.getUserEntity();
        String uid = userEntity.getUid();
        List<ShopRedpacket> list = shopRedpacketMapper.selectList(new QueryWrapper<ShopRedpacket>().eq("shop_uuid", shopUuid));
        if (list.size() != 0) {
            ShopRedpacket shopRedpacket = list.get(list.size() - 1);
            Integer intervals = shopRedpacket.getIntervals(); //间隔0（每人限领一次）1（每天可以领一个）
            String uuid = shopRedpacket.getUuid();//店铺红包活动的uuid
            long l = shopRedpacket.getBegintime().toInstant(ZoneOffset.of("+8")).toEpochMilli();
            long la = shopRedpacket.getEndtime().toInstant(ZoneOffset.of("+8")).toEpochMilli();
            long lb = new Date().getTime();
            if (l < lb && lb < la) {
                SelectShopRedpacketByUserDto dto = new SelectShopRedpacketByUserDto();
                BeanUtils.copyProperties(shopRedpacket, dto);
                dto.setGetStatus(1);//默认已经领取
                List<UserRedpacket> data = userRedpackClient.selectUserRedpacketAll(uuid).getData();//查询用户在此活动中领的红包
                if (intervals==0) {//（每人限领一次）
                    if (data.size()==0 || data==null) {
                        dto.setGetStatus(0);//没有领取
                    }
                }
                if (intervals==1) {//（每天可以领一个）
                    if (data.size()!=0) {
                        UserRedpacket userRedpacket = data.get(data.size() - 1);//获取最新的一个领取红包
                        long l1 = userRedpacket.getGetTime().toInstant(ZoneOffset.of("+8")).toEpochMilli();//获取最新红包的获取时间
                        long time = new Date().getTime();//现在的时间
                        if (time-l1>/*24*60**/60*1000) {
                            dto.setGetStatus(0);//可以继续领取
                        }
                    }else {
                        dto.setGetStatus(0);//可以继续领取
                    }
                }
                return dto;
            }
        }
        return null;
    }

    //{用户}领取红包
    @Override
    @RidissonLock
    /*@GlobalTransactional*/
    public UserGetRedPacketDto UserGetRedPacket(String redPacketUuid) {
        UserGetRedPacketDto dto = new UserGetRedPacketDto();//获取用户信息
        dto.setB(false);
        dto.setRedPrice(new BigDecimal(0));
        UserEntity userEntity = CurrentUserHolder.getUserEntity();
        ShopRedpacket shopRedpacket = shopRedpacketMapper.selectOne(new QueryWrapper<ShopRedpacket>().eq("uuid", redPacketUuid));
        UserRedpacket data1 = userRedpackClient.selectUserRedpacket(redPacketUuid).getData();//获取用户活动中领过的红包时间最近的一条数据
        System.out.println("***********"+data1);
        boolean status = false;
        if (shopRedpacket.getIntervals() == 1) {//每天可以领一个
            if (Objects.nonNull(data1)) {
                long time = new Date().getTime();//当前时间
                long l = data1.getGetTime().toInstant(ZoneOffset.of("+8")).toEpochMilli();//领取时间
                if (time - l > (/*24 * 60 * 60 * 1000*/60*1000)) {
                    status = true;
                } else {
                    throw new JSYException(10003, "今天已经领取");
                }
            }else {
                status = true;
            }

        }

        if (shopRedpacket.getIntervals() == 0) {//只能领一个
            if (data1 == null) {
                status = true;
            } else {
                throw new JSYException(-1, "已经领取过");
            }
        }

        if (status) {
            long l = shopRedpacket.getBegintime().toInstant(ZoneOffset.of("+8")).toEpochMilli();
            long la = shopRedpacket.getEndtime().toInstant(ZoneOffset.of("+8")).toEpochMilli();
            long lb = new Date().getTime();
            if (shopRedpacket.getNum() > 0) {
                if (l < lb && lb < la) {
                    if (shopRedpacket.getType() == 0) {//固定红包
                        dto.setB(true);
                        dto.setRedPrice(new BigDecimal(shopRedpacket.getMoney()));
                        shopRedpacket.setNum(shopRedpacket.getNum() - 1);
                        shopRedpacket.setGetNumber(shopRedpacket.getGetNumber() + 1);
                        shopRedpacketMapper.update(shopRedpacket, new UpdateWrapper<ShopRedpacket>().eq("uuid", redPacketUuid));//数量减1
                        UserRedpacket userRedpacket = new UserRedpacket();
                        userRedpacket.setUserUuid(userEntity.getUid());//用户的uuid
                        userRedpacket.setUuid(UUIDUtils.getUUID());//uuid
                        userRedpacket.setDeleted(1);//设置未使用状态
                        userRedpacket.setGetTime(LocalDateTime.now(Clock.system(ZoneId.of("Asia/Shanghai"))));//获取时间
                        userRedpacket.setShopUuid(shopRedpacket.getShopUuid());//店铺uuid
                        userRedpacket.setRedpacketUuid(UUIDUtils.getUUID());//红包uuid
                        userRedpacket.setMoney(dto.getRedPrice().intValue());//红包价格
                        userRedpacket.setType(1);//红包类型
                        userRedpacket.setActivitieUuid(redPacketUuid);//关联的红包uuid
                        userRedpacket.setValidity(shopRedpacket.getValidity());//红包有效时间
                        Boolean data = userRedpackClient.insterRedPacket(userRedpacket).getData();
                        System.out.println("data=******************" + data);

                    } else if (shopRedpacket.getType() == 1) {//随机红包
                        double v = shopRedpacket.getMin().doubleValue();
                        double v1 = shopRedpacket.getMax().doubleValue();
                        int round = (int) (Math.round(Math.random() * (v - v1) + v1));
                        dto.setB(true);
                        dto.setRedPrice(new BigDecimal(round));
                        shopRedpacket.setNum(shopRedpacket.getNum() - 1);
                        shopRedpacket.setGetNumber(shopRedpacket.getGetNumber() + 1);
                        shopRedpacketMapper.update(shopRedpacket, new UpdateWrapper<ShopRedpacket>().eq("uuid", redPacketUuid));//数量减1
                        UserRedpacket userRedpacket = new UserRedpacket();
                        userRedpacket.setUserUuid(userEntity.getUid());//用户的uuid
                        userRedpacket.setUuid(UUIDUtils.getUUID());//uuid
                        userRedpacket.setDeleted(1);//设置未使用状态
                        userRedpacket.setGetTime(LocalDateTime.now(Clock.system(ZoneId.of("Asia/Shanghai"))));//获取时间
                        userRedpacket.setShopUuid(shopRedpacket.getShopUuid());//店铺uuid
                        userRedpacket.setRedpacketUuid(UUIDUtils.getUUID());//红包uuid
                        userRedpacket.setMoney(dto.getRedPrice().intValue());//红包价格
                        userRedpacket.setType(1);//红包类型
                        userRedpacket.setActivitieUuid(redPacketUuid);//关联的红包活动uuid
                        userRedpacket.setValidity(shopRedpacket.getValidity());//红包有效时间
                        Boolean data = userRedpackClient.insterRedPacket(userRedpacket).getData();
                    }
                } else {
                    throw new JSYException(-1, "红包活动已经失效");
                }
            } else {
                throw new JSYException(10000, "红包已经领完");
            }
        }
        return dto;
    }


    /***************************************************/
    @Override
    public ShopRedpacket grant(String uuid) {
        ShopRedpacket shopRedpacket = getByUuid(uuid);
        if (Objects.isNull(shopRedpacket)) {
            throw new JSYException(-1, "未查询到该店铺红包");
        }
//        if (shopRedpacket.getDeleted() != 1) {
//            throw new JSYException(-1, "该红包已失效");
//        }
//        if (shopRedpacket.getEndtime().isBefore(LocalDateTime.now())) {
//            shopRedpacket.setDeleted(2);
//            baseMapper.update(shopRedpacket, new QueryWrapper<ShopRedpacket>().eq("uuid", uuid));
//            throw new JSYException(-1, "红包活动已结束");
//        }
        if (shopRedpacket.getNum() < 1) {
            throw new JSYException(-1, "很抱歉，红包已经被一抢而空");
        }
        shopRedpacket.setNum(shopRedpacket.getNum() - 1);
        baseMapper.update(shopRedpacket, new QueryWrapper<ShopRedpacket>().eq("uuid", uuid));
        return shopRedpacket;
    }

    @Override
    public void addAndUpdate(ShopRedpacket shopRedpacket) {
        LocalDateTime now = LocalDateTime.now();
        if (now.compareTo(shopRedpacket.getBegintime()) == -1) {
            throw new JSYException(-1, "红包发放时间不能小于当前时间！");
        }
        if (shopRedpacket.getNum() <= 0 || shopRedpacket.getMoney() <= 0) {
            throw new JSYException(-1, "请输入正确的红包数量及金额！");
        }
        try {
            if (!StringUtils.isEmpty(shopRedpacket.getUuid())) {
                baseMapper.update(shopRedpacket, new QueryWrapper<ShopRedpacket>().eq("uuid", shopRedpacket.getUuid()));
            } else {
                // shopRedpacket.setDeleted(0);
                shopRedpacket.setUuid(UUIDUtils.getUUID());
                super.save(shopRedpacket);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new JSYException(-1, "新增店铺红包失败！");
        }
    }

    @Override
    public void changStatus(String uuid, Integer status) {
        ShopRedpacket shopRedpacket = getByUuid(uuid);
        if (Objects.isNull(shopRedpacket)) {
            throw new JSYException(-1, "该店铺红包设置不存在！");
        }
        /* if (*//*!((status == 1 *//**//*&& shopRedpacket.getDeleted() == 0) || (status == 2 && shopRedpacket.getDeleted() == 1))*//*) {
            throw new JSYException(-1, "操作有误！");
        }*/
    }

    @Override
    public PageList<ShopRedpacket> queryByPage(ShopRedpacketQuery query) {
        Page<ShopRedpacket> page = new Page<ShopRedpacket>(query.getPage(), query.getRows());
        QueryWrapper queryWrapper = new QueryWrapper();
        if (Objects.nonNull(query.getDeleted())) {
            queryWrapper.eq("deleted", query.getDeleted());
        }
        page = super.page(page, queryWrapper);
        return new PageList<ShopRedpacket>(page.getTotal(), page.getRecords());
    }

    @Override
    public ShopRedpacket getByUuid(String uuid) {
        return baseMapper.selectOne(new QueryWrapper<ShopRedpacket>().eq("uuid", uuid));
    }

    @Override
    public Map<String, ShopRedpacket> getMapByUuid(List<String> uuids) {
        List<ShopRedpacket> list = baseMapper.selectList(new QueryWrapper<ShopRedpacket>().in("uuid", uuids));
        Map<String, ShopRedpacket> map = new HashMap<>();
        list.forEach(x -> {
            map.put(x.getUuid(), x);
        });
        return map;
    }
}
