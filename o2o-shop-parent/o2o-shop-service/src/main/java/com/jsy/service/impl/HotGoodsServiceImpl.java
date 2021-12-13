package com.jsy.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jsy.basic.util.MyPageUtils;
import com.jsy.basic.util.PageInfo;
import com.jsy.domain.NewShop;
import com.jsy.domain.HotGoods;
import com.jsy.dto.NewShopHotDto;
import com.jsy.mapper.HotGoodsMapper;
import com.jsy.mapper.NewShopMapper;
import com.jsy.query.NewShopQuery;
import com.jsy.service.IHotGoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jsy.util.RedisUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 热门商品表 服务实现类
 * </p>
 *
 * @author yu
 * @since 2021-12-03
 */
@Service
public class HotGoodsServiceImpl extends ServiceImpl<HotGoodsMapper, HotGoods> implements IHotGoodsService {
    @Resource
    private NewShopMapper shopMapper;
    @Resource
    private HotGoodsMapper hotGoodsMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private RedisUtils redisUtils;
    @Override
    public PageInfo<HotGoods> getHot(NewShopQuery newShopQuery) {

        Boolean flage = stringRedisTemplate.hasKey("hotGoods");
        List<HotGoods> hotGoodsList = hotGoodsMapper.selectList(null);
        redisUtils.setHotGoods(hotGoodsList);

        if (flage){
            List<HotGoods> hotGoods = redisUtils.getHotGoods();
            System.out.println(hotGoods+"111111111");
            PageInfo<HotGoods> hotDtoPageInfo = MyPageUtils.pageMap(newShopQuery.getPage(), newShopQuery.getRows(), hotGoods);
            return hotDtoPageInfo;
        }else {
            //redis中数据已过期
            System.out.println("22222222222");
            List<NewShopHotDto> newShopHotDtos = shopMapper.selectHot();
            System.out.println(newShopHotDtos);
            //清空热门数据表
            hotGoodsMapper.delete(null);
            //重新将热门数据假如数据表中
            for (NewShopHotDto newShopHotDto : newShopHotDtos) {
                HotGoods hotGoods = new HotGoods();
                hotGoods.setImages(newShopHotDto.getImages());
                hotGoods.setPrice(newShopHotDto.getPrice());
                hotGoods.setShopId(newShopHotDto.getShopId());
                hotGoods.setTitle(newShopHotDto.getTitle());
                hotGoods.setDiscountPrice(newShopHotDto.getDiscountPrice());
                hotGoods.setPvNum(newShopHotDto.getPvNum());
                hotGoods.setGoodsId(newShopHotDto.getId());
                hotGoods.setType(newShopHotDto.getType());
                hotGoods.setDiscountState(newShopHotDto.getDiscountState());
                hotGoodsMapper.insert(hotGoods);
            }
            List<HotGoods> hotGoods = hotGoodsMapper.selectList(null);
            //将热门数据重新设置缓存
            redisUtils.setHotGoods(hotGoods);
            PageInfo<HotGoods> hotDtoPageInfo = MyPageUtils.pageMap(newShopQuery.getPage(), newShopQuery.getRows(), hotGoods);
            return hotDtoPageInfo;
        }
    }

    @Override
    public Boolean delHotGoods(Long goodsId) {
        try {
            hotGoodsMapper.delHotGoods(goodsId);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public Boolean delHotShop(Long shopId) {
        try {
            hotGoodsMapper.delHotShop(shopId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public HotGoods getHotGoods(Long goodsId) {
        HotGoods hotGoods = hotGoodsMapper.selectOne(new QueryWrapper<HotGoods>().eq("goods_id", goodsId));
        if (hotGoods!=null){
            //获取热门数据过期时间
            Long time = stringRedisTemplate.getExpire("hotGoods", TimeUnit.HOURS);
            System.out.println("剩余时间"+time);
            //删除缓存
            stringRedisTemplate.delete("hootGoods");
            //去掉热门数据里商品的数据
            hotGoodsMapper.delHotGoods(goodsId);
            //查询最新的热门数据
            List<HotGoods> hotGoodsList = hotGoodsMapper.selectList(null);
            //设置新的缓存热门数据
            redisUtils.setHotGoods(hotGoodsList,time);
        }
        return hotGoods;
    }

    @Override
    public HotGoods getHotShop(Long shopId) {
        HotGoods hotGoods = hotGoodsMapper.selectOne(new QueryWrapper<HotGoods>().eq("shop_id", shopId));
        if (hotGoods!=null){
            //获取热门数据过期时间
            Long time = stringRedisTemplate.getExpire("hotGoods", TimeUnit.HOURS);
            System.out.println("剩余时间"+time);
            //删除缓存
            stringRedisTemplate.delete("hootGoods");
            //去掉热门数据里商品的数据
            hotGoodsMapper.delHotShop(shopId);
            //查询最新的热门数据
            List<HotGoods> hotGoodsList = hotGoodsMapper.selectList(null);
            //设置新的缓存热门数据
            redisUtils.setHotGoods(hotGoodsList,time);
        }
        return hotGoods;
    }

    @Override
    public List<HotGoods> getHotList() {
        List<HotGoods> hotGoodsList = hotGoodsMapper.selectList(null);
        return hotGoodsList;
    }
}
