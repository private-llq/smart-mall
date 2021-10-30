package com.jsy.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jsy.basic.util.PageList;
import com.jsy.basic.util.exception.JSYException;
import com.jsy.basic.util.utils.CurrentUserHolder;
import com.jsy.basic.util.utils.UUIDUtils;
import com.jsy.basic.util.vo.UserDto;
import com.jsy.basic.util.vo.UserEntity;
import com.jsy.client.ActivityClient;
import com.jsy.client.FileClient;
import com.jsy.domain.Activity;
import com.jsy.domain.UserFollow;
import com.jsy.dto.UserFollowDTO;
import com.jsy.mapper.UserFollowMapper;
import com.jsy.query.UserFollowQuery;
import com.jsy.service.IUserFollowService;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserFollowServiceImpl extends ServiceImpl<UserFollowMapper, UserFollow> implements IUserFollowService{

    @Autowired
    private UserFollowMapper userFollowMapper;

    @Autowired
    private ActivityClient activityClient;

    @Autowired
    private FileClient FileClient;




    @Override
    @Transactional
    public void follow(String shopUuid) {
        String uuid = UUIDUtils.getUUID();
        UserEntity userEntity = CurrentUserHolder.getUserEntity();
        String userUuid = userEntity.getUid();
        UserFollow userFollow = userFollowMapper.selectOne(new QueryWrapper<UserFollow>().eq("user_uuid", userUuid).eq("shop_uuid", shopUuid));
        if (Objects.nonNull(userFollow)){
            throw new JSYException(-1,"你已关注过该店铺!");
        }

        userFollowMapper.follow(uuid,userUuid,shopUuid);
    }
    //分页
    @Override
    public PageList<UserFollowDTO> followList(UserFollowQuery userFollowQuery) {
        UserEntity userEntity = CurrentUserHolder.getUserEntity();
        String userUuid = userEntity.getUid();
        Page<UserFollow> page = new Page<>(userFollowQuery.getPage(),userFollowQuery.getRows());
        Page<UserFollow> shopList = userFollowMapper.selectPage(page, new QueryWrapper<UserFollow>().eq("user_uuid", userUuid));
        List<UserFollow> records = shopList.getRecords();
        ArrayList<String> shopUuids = new ArrayList<>();
        if (records.size()==0){
            return new PageList<>();
        }
        for (UserFollow record : records) {
            shopUuids.add(record.getShopUuid());
        }
        List<UserFollowDTO> list = userFollowMapper.followList(shopUuids, userUuid);
        LocalDateTime time = LocalDateTime.now();
        for (UserFollowDTO userFollowDTO : list) {
            List<Activity> shopActivity = userFollowDTO.getShopActivity();
            if (shopActivity.size() > 0) {
                Iterator<Activity> iterator = shopActivity.iterator();
                while (iterator.hasNext()){
                    Activity next = iterator.next();
                    if (next.getBeginTime().compareTo(time) >= 0 || next.getEndTime().compareTo(time) <= 0) {
                        iterator.remove();
                    }
                }
            }
        }

        PageList<UserFollowDTO> PageList = new PageList<>();
        PageList.setRows(list);
        PageList.setTotal(shopList.getTotal());
        return PageList;
    }

    @Override
    @Transactional
    public void outFollow(String shopUuid) {
        UserEntity userEntity = CurrentUserHolder.getUserEntity();
        String userUuid = userEntity.getUid();
        userFollowMapper.delete(new QueryWrapper<UserFollow>().eq("shop_uuid", shopUuid).eq("user_uuid", userUuid));
    }

    @Override
    @Transactional
    public Integer FollowStatus(String shopUuid) {
        if (Objects.isNull(shopUuid)){
            throw new JSYException(-1,"参数异常");
        }
        UserEntity userEntity = CurrentUserHolder.getUserEntity();
        String userUuid = userEntity.getUid();
        UserFollow userFollow = userFollowMapper.selectOne(new QueryWrapper<UserFollow>().eq("shop_uuid", shopUuid).eq("user_uuid", userUuid));
        if (Objects.isNull(userFollow)){
            return 0;//未关注
        }else {
            return 1;//已关注
        }
    }
}
