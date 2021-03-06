package com.jsy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jsy.basic.util.MyPageUtils;
import com.jsy.basic.util.PageInfo;
import com.jsy.client.GoodsClient;
import com.jsy.client.HotClient;
import com.jsy.client.SetMenuClient;
import com.jsy.client.TreeClient;
import com.jsy.domain.Tree;
import com.jsy.parameter.NewShopBackstageDto;
import com.jsy.query.SetMenuQuery;
import com.jsy.query.ShopAuditQuery;
import com.jsy.util.RedisUtils;
import com.jsy.domain.HotGoods;
import com.jsy.domain.NewShop;
import com.jsy.domain.ShopAudit;
import com.jsy.mapper.HotGoodsMapper;
import com.jsy.mapper.NewShopMapper;
import com.jsy.mapper.ShopAuditMapper;
import com.jsy.parameter.NewShopAuditParam;
import com.jsy.service.IShopAuditService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhsj.base.api.constant.RpcConst;
import com.zhsj.base.api.rpc.IBaseAuthRpcService;
import com.zhsj.baseweb.support.ContextHolder;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yu
 * @since 2021-12-01
 */
@Service
public class ShopAuditServiceImpl extends ServiceImpl<ShopAuditMapper, ShopAudit> implements IShopAuditService {
    @Resource
    private NewShopMapper shopMapper;
    @Resource
    private ShopAuditMapper auditMapper;
    @Resource
    private HotGoodsMapper hotGoodsMapper;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private TreeClient treeClient;
    @Resource
    private SetMenuClient setMenuClient;
    @Resource
    private GoodsClient goodsClient;
    @Resource
    private HotClient hotClient;
    @DubboReference(version = RpcConst.Rpc.VERSION, group = RpcConst.Rpc.Group.GROUP_BASE_USER, check = false)
    private IBaseAuthRpcService iBaseAuthRpcService;
    @Override
    public boolean addAudit(NewShopAuditParam auditParam) {
        NewShop newShop = shopMapper.selectById(auditParam.getShopId());
        ShopAudit shopAudit = auditMapper.selectOne(new QueryWrapper<ShopAudit>()
                .eq("shop_id", auditParam.getShopId())
                .orderByDesc("create_time")
                .last("limit 1")
        );

        if (shopAudit==null||shopAudit!=null&&shopAudit.getRejectedReason()!=null){
            //审核状态 0未审核 1已审核 2审核未通过 3资质未认证
            if (auditParam.getState()==1){
                newShop.setState(1);
                //成为商家用户
                iBaseAuthRpcService.addLoginTypeScope(newShop.getOwnerUuid(),"shop_admin");
            }else if(auditParam.getState()==2){
                ShopAudit shopAudit1 = new ShopAudit();
                newShop.setState(2);
                shopAudit1.setShopId(auditParam.getShopId());
                shopAudit1.setRejectedReason(auditParam.getRejectedReason());
                Long id = ContextHolder.getContext().getLoginUser().getId();
                shopAudit1.setUserId(id);
                auditMapper.insert(shopAudit1);
            }
        }else {
            //审核状态 0未审核 1已审核 2审核未通过 3资质未认证
            if (auditParam.getState()==1){
                newShop.setState(1);
                //成为商家用户
                iBaseAuthRpcService.addLoginTypeScope(newShop.getOwnerUuid(),"shop_admin");
            }else if(auditParam.getState()==2){
                newShop.setState(2);
                shopAudit.setShopId(auditParam.getShopId());
                shopAudit.setRejectedReason(auditParam.getRejectedReason());
                Long id = ContextHolder.getContext().getLoginUser().getId();
                shopAudit.setUserId(id);
                auditMapper.updateById(shopAudit);
            }
        }
        shopMapper.updateById(newShop);
        return true;
    }

    @Override
    public boolean updateShielding(NewShopAuditParam auditParam) {
        NewShop newShop = shopMapper.selectById(auditParam.getShopId());
        SetMenuQuery setMenuQuery = new SetMenuQuery();
        //店铺被屏蔽之后 禁用所有套餐
        setMenuQuery.setIsDisable(1);
        setMenuQuery.setShopId(auditParam.getShopId());
        ShopAudit shopAudit = auditMapper.selectOne(new QueryWrapper<ShopAudit>()
                .eq("shop_id", auditParam.getShopId())
                .orderByDesc("create_time")
                .last("limit 1")
        );
        System.out.println(shopAudit);
        if (auditParam.getShielding()==1){
            Long time = stringRedisTemplate.getExpire("hotGoods", TimeUnit.HOURS);
            System.out.println("剩余时间"+time);
            //去掉热门数据里屏蔽店铺的数据
            hotGoodsMapper.delete(new QueryWrapper<HotGoods>().eq("shop_id",auditParam.getShopId()));
            //删除缓存
            stringRedisTemplate.delete("hootGoods");
            //重新设置缓存
            List<HotGoods> hotGoodsList = hotGoodsMapper.selectList(null);
            redisUtils.setHotGoods(hotGoodsList,time);
            //店铺被屏蔽之后 下架所有套餐
            setMenuQuery.setIsDisable(1);
            //禁用所以商品和服务
            goodsClient.disableAll(auditParam.getShopId(),0);
            //更新热门数据
            hotClient.getHotShop(auditParam.getShopId());

        }
        if (shopAudit==null||shopAudit!=null&&shopAudit.getShieldingReason()!=null){
            //屏蔽状态 0未屏蔽  1已屏蔽
            if (auditParam.getShielding()==0){
                newShop.setShielding(0);
                //店铺取消屏蔽之后 上架架所有套餐
                setMenuQuery.setIsDisable(0);
                //取消禁用所以商品和服务
                goodsClient.disableAll(auditParam.getShopId(),1);
            }else if(auditParam.getShielding()==1){
                ShopAudit shopAudit1 = new ShopAudit();
                newShop.setShielding(1);
                shopAudit1.setShopId(auditParam.getShopId());
                shopAudit1.setShieldingReason(auditParam.getShieldingReason());
                Long id = ContextHolder.getContext().getLoginUser().getId();
                shopAudit1.setUserId(id);
                setMenuClient.setState(setMenuQuery);
                auditMapper.insert(shopAudit1);
                //店铺被屏蔽之后 下架所有套餐
                setMenuQuery.setIsDisable(1);
                //禁用所以商品和服务
                goodsClient.disableAll(auditParam.getShopId(),0);
                //更新热门数据
                hotClient.getHotShop(auditParam.getShopId());
            }
        }else {
            //屏蔽状态 0未屏蔽  1已屏蔽
            if (auditParam.getShielding()==0){
                newShop.setState(0);
                //店铺取消屏蔽之后 上架架所有套餐
                setMenuQuery.setIsDisable(0);
                //取消禁用所以商品和服务
                goodsClient.disableAll(auditParam.getShopId(),1);
            }else if(auditParam.getShielding()==1){
                newShop.setShielding(1);
                shopAudit.setShopId(auditParam.getShopId());
                shopAudit.setShieldingReason(auditParam.getShieldingReason());
                Long id = ContextHolder.getContext().getLoginUser().getId();
                shopAudit.setUserId(id);
                auditMapper.updateById(shopAudit);
                //店铺被屏蔽之后 下架所有套餐
                setMenuQuery.setIsDisable(1);
                //禁用所以商品和服务
                goodsClient.disableAll(auditParam.getShopId(),0);
                //更新热门数据
                hotClient.getHotShop(auditParam.getShopId());
            }
        }
        setMenuClient.setState(setMenuQuery);
        shopMapper.updateById(newShop);
        return true;
    }

    @Override
    public PageInfo<NewShopBackstageDto> selectList(ShopAuditQuery auditQuery) {
        List<NewShopBackstageDto> backstageDtoList = shopMapper.selectAuitPage(auditQuery);
        for (NewShopBackstageDto newShopBackstageDto : backstageDtoList) {
            String[] split = newShopBackstageDto.getShopTreeId().split(",");
            String shopTreeIdName = getShopTreeIdName2(split);
            newShopBackstageDto.setShopTreeIdName(shopTreeIdName);
        }
        PageInfo<NewShopBackstageDto> shopAuditPageInfo = MyPageUtils.pageMap(auditQuery.getPage(), auditQuery.getRows(), backstageDtoList);
        return shopAuditPageInfo;
    }

    @Override
    public PageInfo<NewShopBackstageDto> selectListPage(ShopAuditQuery auditQuery) {
        List<NewShopBackstageDto> backstageDtoList = shopMapper.selectNewShopPage(auditQuery);
        for (NewShopBackstageDto newShopBackstageDto : backstageDtoList) {
            String[] split = newShopBackstageDto.getShopTreeId().split(",");
            String shopTreeIdName = getShopTreeIdName2(split);
            newShopBackstageDto.setShopTreeIdName(shopTreeIdName);
        }
        PageInfo<NewShopBackstageDto> shopAuditPageInfo = MyPageUtils.pageMap(auditQuery.getPage(), auditQuery.getRows(), backstageDtoList);
        return shopAuditPageInfo;
    }
    private String getShopTreeIdName2(String[] split) {
        String shopTreeIdName = "";
        if (split.length<=2){
            //医疗
            Tree data = treeClient.getTree(Long.valueOf(split[split.length-1])).getData();
            return data.getName();
        }else {
            //其他
            String id = split[split.length-1];
            Tree data1= treeClient.getTree(Long.valueOf(split[split.length-1])).getData();
            System.out.println(data1);
            Tree data2= treeClient.getTree(Long.valueOf(split[split.length-2])).getData();
            return data2.getName()+"-"+data1.getName();
        }
    }



}
