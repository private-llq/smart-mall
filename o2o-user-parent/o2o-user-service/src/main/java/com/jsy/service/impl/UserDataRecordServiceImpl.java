package com.jsy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jsy.basic.util.MyPageUtils;
import com.jsy.basic.util.PageInfo;
import com.jsy.basic.util.exception.JSYException;
import com.jsy.basic.util.utils.GouldUtil;
import com.jsy.client.NewShopClient;
import com.jsy.client.TreeClient;
import com.jsy.domain.Tree;
import com.jsy.domain.UserDataRecord;
import com.jsy.dto.MatchTheUserDto;
import com.jsy.dto.NewShopDto;
import com.jsy.mapper.UserDataRecordMapper;
import com.jsy.query.UserDataRecordQuery;
import com.jsy.service.IUserDataRecordService;
import com.zhsj.baseweb.support.ContextHolder;
import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.im.chat.api.constant.RpcConst;
import com.zhsj.im.chat.api.entity.ChatUserOnLineInfo;
import com.zhsj.im.chat.api.rpc.IImChatUserInfoRpcService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * <p>
 * 用户健康信息档案 服务实现类
 * </p>
 *
 * @author yu
 * @since 2022-01-04
 */
@Service
public class UserDataRecordServiceImpl extends ServiceImpl<UserDataRecordMapper, UserDataRecord> implements IUserDataRecordService {

    @Autowired
    UserDataRecordMapper userDataRecordMapper;

    @Autowired
    TreeClient treeClient;

    @Autowired
    NewShopClient newShopClient;

    @DubboReference(version = RpcConst.Rpc.VERSION, group = RpcConst.Rpc.Group.GROUP_IM_CHAT,check = false)
    private IImChatUserInfoRpcService imService;


    @Override
    public void saveUserDataRecord(UserDataRecord userDataRecord) {
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
        if (Objects.isNull(loginUser)){
            new JSYException(-1,"用户认证失败！");
        }
        userDataRecord.setUid(String.valueOf(loginUser.getId()));
        userDataRecord.setImId(loginUser.getImId());
        Tree data = treeClient.getTree(userDataRecord.getShopTreeId()).getData();
        if (Objects.nonNull(data)){
            Long parentId = data.getParentId();
            Tree tree = treeClient.getTree(parentId).getData();
            if (Objects.nonNull(tree)){
                userDataRecord.setShopTreeName(tree.getName()+"-"+data.getName());
            }

        }
        userDataRecordMapper.insert(userDataRecord);
    }

    /**
     * @author Tian
     * @since 2021/12/3-11:32
     * @description 店铺分类名称
     **/
    private String getShopTreeIdName(String[] split) {
        String shopTreeIdName = "";
        if (split.length >= 0) {
            String name = treeClient.getTree(Long.valueOf(split[split.length - 1])).getData().getName();
            return name;
        }else {
            throw new JSYException(-1,"商品分类错误");
        }

    }

    @Override
    public List<UserDataRecord> getUserDataRecord(String imId) {
        List<UserDataRecord> userDataRecords = userDataRecordMapper.selectList(new QueryWrapper<UserDataRecord>().eq("im_id", imId));
        return userDataRecords;
    }

    @Override
    public List<UserDataRecord> listUserDataRecord(Integer type) {
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
        if (Objects.isNull(loginUser)){
            new JSYException(-1,"用户认证失败！");
        }
        List<UserDataRecord> selectList = userDataRecordMapper.selectList(new QueryWrapper<UserDataRecord>().eq("type",type).eq("uid",loginUser.getId()));
        return selectList;
    }

    @Override
    public List<UserDataRecord> getUserDataRecordTreeId(Long treeId) {
        List<UserDataRecord> list = userDataRecordMapper.selectList(new QueryWrapper<UserDataRecord>().eq("shop_tree_id", treeId));
        return list;
    }

    @Override
    public PageInfo<MatchTheUserDto> matchTheUser(UserDataRecordQuery query) {
        NewShopDto data = newShopClient.get(query.getShopId()).getData();
        BigDecimal shopLongitude = data.getLongitude();
        BigDecimal shopLatitude = data.getLatitude();
        List<UserDataRecord> userDataRecords= userDataRecordMapper.matchTheUser();
        ArrayList<MatchTheUserDto> matchTheUserDtos = new ArrayList<>();
        for (UserDataRecord userDataRecord : userDataRecords) {
            List<ChatUserOnLineInfo> users = imService.listOnLineUser(Arrays.asList(userDataRecord.getImId()));
            if (users.size()!=0){
                ChatUserOnLineInfo user = users.get(0);
                MatchTheUserDto matchTheUserDto = new MatchTheUserDto();
                matchTheUserDto.setHeadImg(user.getHeadImg()==null?"--":user.getHeadImg());
                matchTheUserDto.setUserName(user.getNickName()==null?"--":user.getNickName());
                matchTheUserDto.setState(user.getOnLineStatus()==null?false:user.getOnLineStatus());
                matchTheUserDto.setTreeName(userDataRecord.getShopTreeName());
                if (user.getLatitude()==null || user.getLongitude()==null){
                    matchTheUserDto.setDistance("--");
                }else {
                    String longitude = user.getLongitude();
                    String latitude = user.getLatitude();
                    double distance = GouldUtil.getDistance(longitude + "," + latitude, shopLongitude + "," + shopLatitude);
                    matchTheUserDto.setDistance(distance+"");
                }
                if (user.getOnlineTime()!=null && user.getOnLineStatus()==false){
                    Duration duration = Duration.between( user.getOnlineTime(), LocalDateTime.now());
                    matchTheUserDto.setOutTime(duration.toMinutes()+"");
                }else {
                    matchTheUserDto.setOutTime("--");

                }
                matchTheUserDtos.add(matchTheUserDto);
            }
        }

        PageInfo<MatchTheUserDto> pageInfo = MyPageUtils.pageMap(query.getPage(), query.getRows(), matchTheUserDtos);
        return pageInfo;
    }
    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

}
