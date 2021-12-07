package com.jsy.service.impl;

import com.alibaba.fastjson.JSON;
import com.jsy.basic.util.MyPageUtils;
import com.jsy.basic.util.PageInfo;
import com.jsy.domain.NewShop;
import com.jsy.util.RedisUtils;
import com.jsy.domain.HotGoods;
import com.jsy.dto.NewShopHotDto;
import com.jsy.mapper.HotGoodsMapper;
import com.jsy.mapper.NewShopMapper;
import com.jsy.query.NewShopQuery;
import com.jsy.service.IHotGoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
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
                hotGoodsMapper.insert(hotGoods);
            }
            List<HotGoods> hotGoods = hotGoodsMapper.selectList(null);
            //将热门数据重新设置缓存
            redisUtils.setHotGoods(hotGoods);
            PageInfo<HotGoods> hotDtoPageInfo = MyPageUtils.pageMap(newShopQuery.getPage(), newShopQuery.getRows(), hotGoods);
            return hotDtoPageInfo;
        }
    }
}
