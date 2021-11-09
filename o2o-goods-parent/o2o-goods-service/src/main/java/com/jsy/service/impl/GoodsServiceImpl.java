package com.jsy.service.impl;

import com.jsy.domain.Goods;
import com.jsy.mapper.GoodsMapper;
import com.jsy.service.IGoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  商品表 服务实现类
 * </p>
 *
 * @author lijin
 * @since 2021-11-09
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements IGoodsService {

}
