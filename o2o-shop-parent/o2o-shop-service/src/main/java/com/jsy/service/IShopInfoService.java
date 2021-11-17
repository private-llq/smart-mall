package com.jsy.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jsy.basic.util.utils.PagerUtils;
import com.jsy.domain.ShopInfo;
import com.jsy.dto.*;
import com.jsy.parameter.*;
import com.jsy.query.AdminShopQuery;
import com.jsy.query.ShopInfoQuery;
import com.jsy.vo.ShopInfoParamVo;
import com.jsy.vo.ShopInfoVo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lijin
 * @since 2020-11-12
 */
public interface IShopInfoService extends IService<ShopInfo> {

    //申请店铺
    public  Boolean applyShop(ShopInfoParamVo shopInfoParam);
    //分页查询待审核的店铺
    public IPage<ShopInfo> selectShopInfoByUnrevised(PageParam pageParam);
    //批量店铺审核
    public Boolean approveShop(List<String> uuidS);
    //根据账户查看自己的门店列表
    public List<ShopInfoByOwnerParam> selectShopListByShopOwnerUuid();
    //根据申请店铺uuid删除
    public Boolean deleteShopInfo(String uuid);
    //查询删除的店铺列表
    public  List<ShopInfoByOwnerParam> selectDeletedShopList();
    //查询店铺信息
    public ShopMessageDto selectShopMessage(String shopUuid);
    //查询店铺资料
    public SelectShopMeansDto selectShopMeans(String shopUuid);
    //修改店铺公告
    public  Boolean updateNotice(UpdateNoticeParam updateNoticeParam);
    //修改店铺环境
    public Boolean updateShopEnvironment(UpdateShopEnvironmentParam updateShopEnvironmentParam);
    //切换营业状态
    public Boolean updateBusinessStatus(UpdateBusinessStatusParam updateBusinessStatusParam);
    //修改接单电话
    public Boolean updateOrderReceivingPhone(UpdateOrderReceivingPhoneParam updateOrderReceivingPhoneParam );
    //修改店铺单笔配送费
    public Boolean updatePostage(UpdatePostageParam updatePostageParam);
    //查询商家单笔配送费
    public BigDecimal selectPostage(String shopUUid);


/**********************************************************************************************************************/
//    PagerUtils<ShopQueryDTO>   selectByConditon(ShopInfoQuery conditonVo);

    void addShop(ShopInfoVo shopInfoVo);

    Integer updateShop(ShopInfoVo shopInfoVo);

    Integer changeStatus(String uuid,String status);

    ShopInfoDTO selectShop(String uuid,double longitude,double latitude);

    void closeShop(String id);

    void selectShopStar(String shopUuid,Double star);

    List<ShopQueryDTO> commendShop(ShopInfoQuery query);

    Map<String, ShopInfo> getMapByUuid(List<String> uuids);

    PagerUtils<ShopInfo> getList(AdminShopQuery adminShopQuery);

    PagerUtils<ShopActiveDTO> getActivity(ShopInfoQuery query);

    GoodsOverViewDTO overview();

}

