package com.jsy.service;

import com.jsy.basic.util.PageInfo;
import com.jsy.domain.NewShop;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jsy.dto.*;
import com.jsy.parameter.*;
import com.jsy.query.MainSearchQuery;
import com.jsy.query.NewShopQuery;

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
    Long addNewShop(NewShopParam shopPacketParam);
    //根据店铺id预览店铺基本信息
    NewShopPreviewDto getPreviewDto(Long shopId);
//修改所有店铺的参数
    void update(NewShopModifyParam modifyParam);
//修改店铺设置
    void setSetShop(NewShopSetParam shopSetParam);



    /***********************************************************************************/
    //C端查询店铺
    PageInfo<NewShopRecommendDto> getShopAllList(NewShopQuery shopQuery);


    /**
     * 首页搜索
     */
    PageInfo<MyNewShopDto> mainSearch(MainSearchQuery mainSearchQuery);


    /**
     * 根据id批量插询
     * @param ids
     * @return
     */
    List<NewShopDto> batchIds(List<Long> ids);
 /**
  * @author Tian
  * @since 2021/11/29-9:34
  * @description 大后台分页
  **/
    PageInfo<NewShopDto> newShopPage(NewShopQuery shopQuery);
 /**
  * @author Tian
  * @since 2021/11/29-16:08
  * @description 热门推荐
  *
  * @param newShopQuery*/
    PageInfo<NewShopHotDto> getHot(NewShopQuery newShopQuery);

     /**
      * @author Tian
      * @since 2021/12/1-9:29
      * @description   创建店铺资质认证
      **/
    void addQualification(NewShopQualificationParam qualificationParam);
 /**
  * @author Tian
  * @since 2021/12/1-11:18
  * @description 店铺预览基本信息查询
  **/
    NewShopBasicDto selectBasic(Long shopId);
     /**
      * @author Tian
      * @since 2021/12/1-11:47
      * @description 修改店铺基本信息填写
      **/
    void updateBasic(NewShopParam shopPacketParam);
 /**
  * @author Tian
  * @since 2021/12/2-10:10
  * @description 本月入驻商家数量
  **/
    Integer newShopAudit();
 /**
  * @author Tian
  * @since 2021/12/3-10:00
  * @description 查询店铺支持
  **/
 NewShopSupportDto getSupport(Long shopId);
 /**
  * @author Tian
  * @since 2021/12/8-11:41
  * @description C端查询店铺的距离多远
  **/
 NewShopDistanceDto getDistance(NewShopDistanceParam distanceParam);
 /**
  * @author Tian
  * @since 2021/12/9-10:08
  * @description 医疗---的救助机构
  **/
 PageInfo<NewShopRecommendDto> getMedicalShop(NewShopQuery shopQuery);
  /**
   * @author Tian
   * @since 2021/12/13-10:30
   * @description 通过关键词搜索店铺和服务名称
   **/
 NewShopServiceDto getShopService(NewShopQuery shopQuery);
}
