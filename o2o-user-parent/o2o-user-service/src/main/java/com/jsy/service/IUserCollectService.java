package com.jsy.service;

import com.jsy.basic.util.PageInfo;
import com.jsy.domain.UserCollect;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jsy.dto.userCollectDto;
import com.jsy.param.UserCollectParam;
import com.jsy.query.UserCollectQuery;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yu
 * @since 2021-11-22
 */
public interface IUserCollectService extends IService<UserCollect> {

    /**
     * 收藏商品\服务\套餐\店铺
     * @param userCollectParam
     * @return
     */
    void addUserCollect(UserCollectParam userCollectParam);

    /**
     * 分页查询收藏的商品、服务、套餐、店铺
     * @param userCollectQuery 查询对象
     * @return PageList 分页对象
     */
    PageInfo<Object> userCollectPageList(UserCollectQuery userCollectQuery);


    /**
     * 收藏按钮状态 亮(已收藏)：true   灰色 ：false
     */
    Boolean userCollectState(Integer type, Long id);
}
