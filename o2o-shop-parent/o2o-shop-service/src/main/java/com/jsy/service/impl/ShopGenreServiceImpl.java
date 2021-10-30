package com.jsy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jsy.domain.ShopGenre;
import com.jsy.mapper.ShopGenreMapper;
import com.jsy.service.ShopGenreService;
import org.springframework.stereotype.Service;

@Service
public class ShopGenreServiceImpl  extends ServiceImpl<ShopGenreMapper, ShopGenre> implements ShopGenreService {
}
