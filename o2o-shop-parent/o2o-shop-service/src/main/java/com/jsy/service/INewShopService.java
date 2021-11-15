package com.jsy.service;

import com.jsy.domain.NewShop;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jsy.dto.NewShopPreviewDto;
import com.jsy.dto.NewShopRecommendDto;
import com.jsy.parameter.NewShopParam;
import com.jsy.parameter.NewShopSetParam;

import java.util.List;

/**
 * <p>
 * 新——店铺表 服务类
 * </p>
 *
 * @author yu
 * @since 2021-11-08
 */
public interface INewShopService extends IService<NewShop> {
    //创建店铺
    void addNewShop(NewShopParam shopPacketParam);
    //根据店铺id预览店铺基本信息
    NewShopPreviewDto getPreviewDto(Long shopId);
//修改店铺的参数
    void update(NewShopParam shopPacketParam);
//修改店铺设置
    void setSetShop(NewShopSetParam shopSetParam);



    /***********************************************************************************/
    //C端查询店铺
    List<NewShopRecommendDto> getShopAllList(Long treeId,String location);
}
