package com.jsy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jsy.basic.util.PageInfo;
import com.jsy.basic.util.exception.JSYException;
import com.jsy.basic.util.utils.BeansCopyUtils;
import com.jsy.client.NewShopClient;
import com.jsy.client.TreeClient;
import com.jsy.client.UserDataRecordClient;
import com.jsy.domain.GuestRecommend;
import com.jsy.domain.Tree;
import com.jsy.domain.UserDataRecord;
import com.jsy.dto.GuestRecommendDto;
import com.jsy.dto.NewShopDto;
import com.jsy.mapper.GuestRecommendMapper;
import com.jsy.query.GuestRecommendQuery;
import com.jsy.service.IGuestRecommendService;
import com.zhsj.baseweb.support.ContextHolder;
import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.im.chat.api.constant.RpcConst;
import com.zhsj.im.chat.api.entity.ChatUserOnLineInfo;
import com.zhsj.im.chat.api.rpc.IImChatUserInfoRpcService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
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
@Slf4j
public class GuestRecommendServiceImpl extends ServiceImpl<GuestRecommendMapper, GuestRecommend> implements IGuestRecommendService {

    @Autowired
    private GuestRecommendMapper guestRecommendMapper;
    @Autowired
    private NewShopClient newShopClient;

    @Autowired
    private UserDataRecordClient userDataRecordClient;

    @Autowired
    private TreeClient treeClient;

    @DubboReference(version = RpcConst.Rpc.VERSION, group = RpcConst.Rpc.Group.GROUP_IM_CHAT,check = false)
    private IImChatUserInfoRpcService imService;





    @Override
    @Transactional
    public PageInfo<GuestRecommendDto> pageGuestRecommend(GuestRecommendQuery query) {
        if (Objects.isNull(query.getShopId())){
            throw new JSYException(-1,"shopId不能为空！");
        }
        //查询已有数据是否存在
        List<GuestRecommend> recommendList = guestRecommendMapper.selectList(new QueryWrapper<GuestRecommend>().eq("shop_id", query.getShopId()));
        if (recommendList.size()==0){
            return new PageInfo<>();
        }
        //获取imid、shoptreeid、去重
        recommendList.forEach(x -> {
            HashSet<String> set = new HashSet<>();
            List<UserDataRecord> data = userDataRecordClient.getUserDataRecordTreeId(x.getTreeId()).getData();
            for (UserDataRecord datum : data) {

                set.add(datum.getImId()+datum.getShopTreeId().toString());

            }
            if (Objects.nonNull(set.isEmpty())){
                guestRecommendMapper.update(null,new UpdateWrapper<GuestRecommend>().eq("id",x.getId()).set("user_num",set.size()));
            }

        });


        Page<GuestRecommend> page = new Page(query.getPage(),query.getRows());
        Page<GuestRecommend> selectPage = guestRecommendMapper.selectPage(page, new QueryWrapper<GuestRecommend>().eq("shop_id", query.getShopId()));
        List<GuestRecommend> records = selectPage.getRecords();
        List<GuestRecommendDto> dtoList = BeansCopyUtils.listCopy(records, GuestRecommendDto.class);
        List<GuestRecommendDto> collect = dtoList.stream().map(x -> {
            GuestRecommendQuery recommendQuery = new GuestRecommendQuery();
            recommendQuery.setShopId(x.getShopId());
            x.setHeadImage(x.getHeadImage());
            return x;
        }).collect(Collectors.toList());

        PageInfo<GuestRecommendDto> pageInfo=new PageInfo();
        pageInfo.setRecords(collect);pageInfo.setTotal(selectPage.getTotal());
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
        Tree data = treeClient.getTree(guestRecommend.getTreeId()).getData();
        if (Objects.nonNull(data)){
            Tree tree = treeClient.getTree(data.getParentId()).getData();
            if (Objects.nonNull(tree)){
                guestRecommend.setTreeName(tree.getName()+"-"+data.getName());//商店分类名称
            }

        }
        NewShopDto data1 = newShopClient.get(shopId).getData();
        if (Objects.nonNull(data1)){
            guestRecommend.setShopName(data1.getShopName());
        }

        List<ChatUserOnLineInfo> users = imService.listOnLineUser(Arrays.asList(loginUser.getImId()));
        for (ChatUserOnLineInfo user : users) {
            guestRecommend.setLinkman(user.getNickName());
            guestRecommend.setHeadImg(user.getHeadImg());
        }

        guestRecommendMapper.insert(guestRecommend);

    }


    @Override
    @Transactional
    public void updateGuestRecommend(GuestRecommend guestRecommend) {
        if (guestRecommend.getId()==null){
            throw new JSYException(-1,"id不能为空！");
        }
        int i = guestRecommendMapper.updateById(guestRecommend);
    }

}
