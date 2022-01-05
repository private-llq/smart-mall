package com.jsy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jsy.basic.util.MyPageUtils;
import com.jsy.basic.util.PageInfo;
import com.jsy.basic.util.exception.JSYException;
import com.jsy.basic.util.utils.BeansCopyUtils;
import com.jsy.basic.util.utils.GouldUtil;
import com.jsy.client.NewShopClient;
import com.jsy.client.UserDataRecordClient;
import com.jsy.domain.GuestRecommend;
import com.jsy.domain.UserDataRecord;
import com.jsy.dto.GuestRecommendDto;
import com.jsy.dto.MatchTheUserDto;
import com.jsy.dto.NewShopDto;
import com.jsy.mapper.GuestRecommendMapper;
import com.jsy.query.GuestRecommendQuery;
import com.jsy.service.IGuestRecommendService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Tian
 * @since 2022-01-04
 */
@Service
public class GuestRecommendServiceImpl extends ServiceImpl<GuestRecommendMapper, GuestRecommend> implements IGuestRecommendService {

    @Autowired
    private GuestRecommendMapper guestRecommendMapper;
    @Autowired
    private NewShopClient newShopClient;

    @Autowired
    private UserDataRecordClient userDataRecordClient;

    @DubboReference(version = RpcConst.Rpc.VERSION, group = RpcConst.Rpc.Group.GROUP_IM_CHAT,check = false)
    private IImChatUserInfoRpcService imService;





    @Override
    public PageInfo<GuestRecommendDto> pageGuestRecommend(GuestRecommendQuery query) {
        if (Objects.isNull(query.getShopId())){
            throw new JSYException(-1,"shopId不能为空！");
        }
        Page<GuestRecommend> page = new Page(query.getPage(),query.getRows());
        Page<GuestRecommend> selectPage = guestRecommendMapper.selectPage(page, new QueryWrapper<GuestRecommend>().eq("shop_id", query.getShopId()));
        List<GuestRecommend> records = selectPage.getRecords();
       /* List<GuestRecommend> collect = records.stream().map(x -> {
            x.setUserNum(selectPage.getTotal());
            return x;
        }).collect(Collectors.toList());*/
        List<GuestRecommendDto> dtoList = BeansCopyUtils.listCopy(records, GuestRecommendDto.class);
        PageInfo<GuestRecommendDto> pageInfo=new PageInfo();
        pageInfo.setRecords(dtoList);pageInfo.setTotal(selectPage.getTotal());
        pageInfo.setSize(selectPage.getSize());pageInfo.setCurrent(selectPage.getCurrent());
        return pageInfo;
    }



    @Override
    public void saveGuestRecommend(GuestRecommend guestRecommend) {
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
        Long shopId = guestRecommend.getShopId();
        if (Objects.isNull(shopId)){
            throw new JSYException(-1,"shopId不能为空！");
        }
        guestRecommend.setShopUserId(loginUser.getId());//userId
        NewShopDto data = newShopClient.get(shopId).getData();
        if (Objects.nonNull(data)){
            guestRecommend.setShopName(data.getShopName());//商店名称
            guestRecommend.setTreeName(data.getShopTreeIdName());//商店分类名称
        }
        guestRecommendMapper.insert(guestRecommend);
    }

    @Override
    public PageInfo<MatchTheUserDto> matchTheUser(GuestRecommendQuery query) {
        if (Objects.isNull(query)){
            throw new JSYException(-1,"shopId不能为空！");
        }
        List<GuestRecommend> recommendList = guestRecommendMapper.selectList(new QueryWrapper<GuestRecommend>().eq("shop_id", query.getShopId()));
        ArrayList<Long> longs = new ArrayList<>();
        recommendList.forEach(x->{
           longs.add(x.getTreeId());
        });
        List<UserDataRecord> userList = userDataRecordClient.listUserDataRecord().getData();
        List<String> imIds = userList.stream().filter(x -> {//匹配的用户imId
            if (longs.contains(x.getShopTreeId())) {
                return true;
            }
            return false;
        }).map(UserDataRecord::getImId).collect(Collectors.toList());
        List<ChatUserOnLineInfo> infoList = imService.listOnLineUser(imIds);
        ArrayList<MatchTheUserDto> matchTheUserDtos = new ArrayList<>();
        infoList.forEach(x->{
            MatchTheUserDto matchTheUserDto = new MatchTheUserDto();
            matchTheUserDto.setUserName(x.getNickName());
            matchTheUserDto.setHeadImg(x.getHeadImg());
            matchTheUserDto.setState(x.getOnLineStatus());
            if (x.getOnLineStatus()==false){
                LocalDateTime onlineTime = x.getOnlineTime();
                long minutes = Duration.between(onlineTime, LocalDateTime.now()).toMinutes();
                matchTheUserDto.setOutTime(minutes);
            }
            UserDataRecord data = userDataRecordClient.getUserDataRecord(x.getImId()).getData();
            if (Objects.nonNull(data)){
                matchTheUserDto.setTreeName(data.getShopTreeName());
            }
            NewShopDto newShopDto = newShopClient.get(query.getShopId()).getData();
            if (Objects.nonNull(newShopDto)){
                BigDecimal longitude = newShopDto.getLongitude();
                BigDecimal latitude = newShopDto.getLatitude();
                double distance = GouldUtil.getDistance(longitude + "," + latitude, x.getLongitude() + "," + x.getLatitude());
                matchTheUserDto.setDistance(String.valueOf(distance));
            }
            matchTheUserDtos.add(matchTheUserDto);

        });
        PageInfo<MatchTheUserDto> pageInfo = MyPageUtils.pageMap(query.getPage(), query.getRows(), matchTheUserDtos);
        return pageInfo;
    }

}
