package com.jsy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsy.basic.util.PageInfo;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.client.GoodsClient;
import com.jsy.client.NewShopClient;
import com.jsy.client.SetMenuClient;
import com.jsy.domain.UserCollect;
import com.jsy.dto.GoodsDto;
import com.jsy.dto.userCollectDto;
import com.jsy.mapper.UserCollectMapper;
import com.jsy.param.UserCollectParam;
import com.jsy.query.UserCollectQuery;
import com.jsy.service.IUserCollectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yu
 * @since 2021-11-22
 */
@Service
public class UserCollectServiceImpl extends ServiceImpl<UserCollectMapper, UserCollect> implements IUserCollectService {

    @Autowired
    private UserCollectMapper userCollectMapper;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SetMenuClient setMenuClient;

    @Autowired
    private NewShopClient newShopClient;



    /**
     * 收藏商品\服务\套餐\店铺
     * @param userCollectParam
     * @return
     */
    @Override
    public void addUserCollect(UserCollectParam userCollectParam) {
        UserCollect userCollect = new UserCollect();
        BeanUtils.copyProperties(userCollectParam,userCollect);
        userCollectMapper.insert(userCollect);
    }


    /**
     * 分页查询收藏的商品、服务、套餐、店铺
     * @param userCollectQuery 查询对象
     * @return PageList 分页对象
     */
    @Override
    public PageInfo<userCollectDto> userCollectPageList(UserCollectQuery userCollectQuery) {
        Page<UserCollect> page = new Page<>(userCollectQuery.getPage(), userCollectQuery.getRows());
        Page<UserCollect> userCollectPage = userCollectMapper.selectPage(page, new QueryWrapper<UserCollect>().eq("user_id", userCollectQuery.getUserId()));
        List<Long> goodsServiceCollect = new ArrayList<>();
        List<Long> setMenuCollect = new ArrayList<>();
        List<Long> shopCollect = new ArrayList<>();
        userCollectPage.getRecords().stream().forEach(x->{
            if (x.getType()==0){
                goodsServiceCollect.add(x.getGoodsId());
            }
            if (x.getType()==1){
                setMenuCollect.add(x.getMenuId());
            }
            if (x.getType()==2){
                shopCollect.add(x.getShopId());
            }
        });

        CommonResult<List<GoodsDto>> listCommonResult = goodsClient.batchGoods(goodsServiceCollect);


        return null;
    }
}
