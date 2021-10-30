package com.jsy.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jsy.basic.util.PageList;
import com.jsy.basic.util.exception.JSYException;
import com.jsy.basic.util.utils.CurrentUserHolder;
import com.jsy.basic.util.utils.UUIDUtils;
import com.jsy.basic.util.vo.UserDto;
import com.jsy.client.FileClient;
import com.jsy.client.GoodsBasicFeign;
import com.jsy.domain.GoodsBasic;
import com.jsy.domain.ShopApplySeckill;
import com.jsy.dto.ShopApplySeckillDTO;
import com.jsy.dto.ShopApplySeckillDTOShow;
import com.jsy.mapper.ShopApplySeckillMapper;
import com.jsy.query.ShopApplySeckillQuery;
import com.jsy.query.UserApplySeckillQuery;
import com.jsy.service.IShopApplySeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;
/**
 * <p>
 *  服务实现类
 * </p>
 * @author yu
 * @since 2020-12-19
 */
@Service
public class ShopApplySeckillServiceImpl extends ServiceImpl<ShopApplySeckillMapper, ShopApplySeckill> implements IShopApplySeckillService {

    @Autowired
    private ShopApplySeckillMapper ShopApplySeckillMapper;

    @Autowired
    private FileClient fileClient;

    @Autowired
    private GoodsBasicFeign GoodsBasicFeign;



    /**
     * 商家提交申请 (当天的规定时间段从时间获取)
     * @param shopApplySeckill
     */
    @Override
    @Transactional
    public void submitApplication(ShopApplySeckill shopApplySeckill) {

        UserDto currentUser = CurrentUserHolder.getCurrentUser();
        String shopUuid = currentUser.getUuid();//店家UUid
        shopApplySeckill.setShopUuid(shopUuid);
        LocalDateTime startTime = shopApplySeckill.getStartTime();
        if (startTime==null){
            throw new JSYException(-1,"时间不能为空！");
        }
        if (startTime.isBefore(LocalDateTime.now())) {
            throw new JSYException(-1,"输入的活动时间错误,不能小于当前时间!");
        }
        if (shopApplySeckill.getSeckillPrice()==null){
            throw new JSYException(-1,"秒杀价格不能为空");
        }
        //每人限购必须大于1
        if (shopApplySeckill.getPurchaseRestrictions()<1){
            throw new JSYException(-1,"每人至少限购一份！");
        }
        //秒杀价格必须小于原价
        GoodsBasic goods = GoodsBasicFeign.getGoods(shopApplySeckill.getGoodsUuid());
        if (goods.getPrice().compareTo(shopApplySeckill.getSeckillPrice())==-1){
            throw new JSYException(-1,"商品秒杀价格必须小于原价");
        }
        shopApplySeckill.setUuid(UUIDUtils.getUUID());
        shopApplySeckill.setState(2);//申请中
        ShopApplySeckillMapper.insert(shopApplySeckill);
    }

    /**
     * 平台审核通过
     * @param uuid
     */
    @Override
    @Transactional
    public void reviewApplication(String uuid) {
        ShopApplySeckillMapper.update(new ShopApplySeckill(),new UpdateWrapper<ShopApplySeckill>().eq("uuid",uuid).set("state",1));
    }

    /**
     * 平台驳回，并携带原因
     * @param uuid
     */
    @Override
    @Transactional
    public void rejectApplication(String uuid, String rejectionReasons){
        ShopApplySeckillMapper.update(new ShopApplySeckill(),new UpdateWrapper<ShopApplySeckill>().eq("uuid",uuid).set("state",0).set("rejection_reasons",rejectionReasons));
    }


    /**
     * 平台端
     * 分页查询所有秒杀活动申请
     * @param query 查询对象
     * @return PageList 分页对象
     */
    @Override
    public PageList<ShopApplySeckillDTO> ApplicationPageList(ShopApplySeckillQuery query) {
        Page<ShopApplySeckill> shopApplySeckillPage = new Page<>(query.getPage(),query.getRows());
        IPage<ShopApplySeckillDTO> iPage = ShopApplySeckillMapper.applicationPageList(shopApplySeckillPage, query);
        List<ShopApplySeckillDTO> records = iPage.getRecords();
        long total = iPage.getTotal();

        ArrayList<String>  images = new ArrayList<>();
        for (ShopApplySeckillDTO record : records) {
            images.add(record.getImagesUrl());
        }

        Map<String, String> picUrl = fileClient.getPicUrl(images);
        for (ShopApplySeckillDTO record : records) {
            record.setImagesUrl(picUrl.get(record.getImagesUrl()));
        }

        PageList<ShopApplySeckillDTO> pageList = new PageList<>();
        pageList.setTotal(total);
        pageList.setRows(records);

        if (Objects.isNull(records) || records.size() == 0){
            throw new JSYException(-1,"暂时没有活动");
        }
        return pageList;
    }
    /**
     * 用户端
     * 根据时间段查询 平台审核通过的活动 （首页正在抢活动展示）
     * ps:前端通过treeMap拿到本次活动的开始时间段，以及本次活动的系统结束时间，
     * 前端需要判断当前系统时间是否到了本次活动的开始时间段，
     * 如果到了就把该时间段传入此接口查询
     */
    @Override
    public ShopApplySeckillDTOShow userApplicationPageList(UserApplySeckillQuery query) {

        //先做展示 不进行验证 前端写死时间参数
       /* if (query.getSelectTime()!=null && !"".equals(query.getSelectTime())){
            //参数：年月日
            LocalDateTime selectTime = query.getSelectTime();
            int year = selectTime.getYear();
            int monthValue = selectTime.getMonthValue();
            int dayOfMonth = selectTime.getDayOfMonth();
            //当前服务器：年月日
            LocalDateTime now = LocalDateTime.now();
            int year1 = now.getYear();
            int monthValue1 = now.getMonthValue();
            int dayOfMonth1 = now.getDayOfMonth();
            if (year!=year1||monthValue!=monthValue1||dayOfMonth!=dayOfMonth1){
                throw  new JSYException(-1,"年月日时间参数错误,不是当天的时间！");
            }
        }*/
        Page<ShopApplySeckill> shopApplySeckillPage = new Page<>(query.getPage(),query.getRows());
        IPage<ShopApplySeckillDTO> iPage = ShopApplySeckillMapper.userApplicationPageList(shopApplySeckillPage, query);
        List<ShopApplySeckillDTO> records = iPage.getRecords();
        long total = iPage.getTotal();
        LocalDateTime set1 = null;

        for (ShopApplySeckillDTO record : records) {
            set1=record.getStartTime();
            record.setImagesUrl(Arrays.asList(record.getImagesUrl().split(";")).get(0));
        }
        PageList<ShopApplySeckillDTO> pageList = new PageList<>();
        pageList.setRows(records);
        pageList.setTotal(total);
        ShopApplySeckillDTOShow shopApplySeckillDTOShow = new ShopApplySeckillDTOShow();
        shopApplySeckillDTOShow.setDtoPageList(pageList);

        if (set1!=null){
            shopApplySeckillDTOShow.setStartTime(set1.toEpochSecond(ZoneOffset.of("+8")));
        }
        if (Objects.isNull(records) || records.size() == 0){
            throw new JSYException(-1,"暂时没有活动");
        }

        return shopApplySeckillDTOShow;
    }
    /**
     * 一次返回
     * 秒杀活动页面的数据
     * @return
     */
    @Override
    public TreeMap<LocalTime,List<ShopApplySeckillDTO>> activityList() {
        //参数：年月日
        LocalDateTime selectTime = LocalDateTime.now();
        int year = selectTime.getYear();
        int monthValue = selectTime.getMonthValue();
        int dayOfMonth = selectTime.getDayOfMonth();
        ArrayList<String> images = new ArrayList<>();
        List<ShopApplySeckillDTO> shopApplySeckills = ShopApplySeckillMapper.activityList();//所有参加秒杀活动的 店家信息 商品信息（按时间段区分）
        //过滤 ：只展示当天参加秒杀的商品
        List<ShopApplySeckillDTO> collect = shopApplySeckills.stream().filter(shopApplySeckillDTO -> {
            return shopApplySeckillDTO.getStartTime().getYear() == year && shopApplySeckillDTO.getStartTime().getMonthValue() == monthValue && shopApplySeckillDTO.getStartTime().getDayOfMonth() == dayOfMonth;
        }).collect(Collectors.toList());
        //判断今天活动不为空
        if (!CollectionUtils.isEmpty(collect)) {
            TreeMap<LocalTime, List<ShopApplySeckillDTO>> map = new TreeMap<>();
            for (ShopApplySeckillDTO applySeckill : collect) {//循环过滤之后的所有活动 （今天）
                LocalDateTime startTime = applySeckill.getStartTime();//循环获取每一条记录的时间
                int hour = startTime.getHour();
                LocalTime time = LocalTime.of(hour, 0);//格式化之后的时间
                if (!map.containsKey(time)) {//key不存在
                    ArrayList<ShopApplySeckillDTO> dtoArrayList = new ArrayList<>();
                    dtoArrayList.add(applySeckill);
                    map.put(time,dtoArrayList);
                } else {
                    map.get(time).add(applySeckill);//根据时间键得到一个list 再追加一个对象元素
                }
                images.add(applySeckill.getImagesUrl());
            }
            //设置图片
            Map<String, String> picUrl = fileClient.getPicUrl(images);
            for (ShopApplySeckillDTO shopApplySeckill : shopApplySeckills) {
                shopApplySeckill.setImagesUrl(picUrl.get(shopApplySeckill.getImagesUrl()));
            }
            return map;
        } else {
            return null;
        }
    }

    /**
     * 查询平台审核通过的秒杀活动信息
     * @param shopUuid
     * @param goodsUuid
     * @return
     */
    @Override
    public ShopApplySeckill selectKillInfo(String shopUuid, String goodsUuid) {
        ShopApplySeckill shopApplySeckill = ShopApplySeckillMapper.selectOne(new QueryWrapper<ShopApplySeckill>().eq("shop_uuid", shopUuid).eq("goods_uuid", goodsUuid).eq("state",1));
        return shopApplySeckill;
    }

    /**
     * 修改秒杀商品的库存
     * @param shopUuid
     * @param goodsUuid
     * @param cartNum 购物车秒杀商品的数量
     * @return
     */
    @Override
    public boolean updateStock(String shopUuid, String goodsUuid, Integer cartNum) {

        return ShopApplySeckillMapper.updateStock( shopUuid,  goodsUuid,  cartNum);
    }


}
