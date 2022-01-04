package com.jsy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jsy.basic.util.PageInfo;
import com.jsy.basic.util.exception.JSYException;
import com.jsy.basic.util.utils.BeansCopyUtils;
import com.jsy.client.NewShopClient;
import com.jsy.domain.GuestRecommend;
import com.jsy.dto.GuestRecommendDto;
import com.jsy.dto.NewShopDto;
import com.jsy.mapper.GuestRecommendMapper;
import com.jsy.query.GuestRecommendQuery;
import com.jsy.service.IGuestRecommendService;
import com.zhsj.baseweb.support.ContextHolder;
import com.zhsj.baseweb.support.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

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




    @Override
    public PageInfo<GuestRecommendDto> pageGuestRecommend(GuestRecommendQuery query) {
        if (Objects.isNull(query.getShopId())){
            throw new JSYException(-1,"shopId不能为空！");
        }
        Page<GuestRecommend> page = new Page(query.getPage(),query.getRows());
        Page<GuestRecommend> selectPage = guestRecommendMapper.selectPage(page, new QueryWrapper<GuestRecommend>().eq("shopId", query.getShopId()));
        List<GuestRecommend> records = selectPage.getRecords();
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
        guestRecommend.setShopName(data.getShopName());//商店名称
        guestRecommend.setTreeName(data.getShopTreeIdName());//商店分类名称
    }


}
