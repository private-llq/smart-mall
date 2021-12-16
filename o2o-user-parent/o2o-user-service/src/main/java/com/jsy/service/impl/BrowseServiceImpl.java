package com.jsy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jsy.domain.Browse;
import com.jsy.mapper.BrowseMapper;
import com.jsy.service.IBrowseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yu
 * @since 2021-11-22
 */
@Service
public class BrowseServiceImpl extends ServiceImpl<BrowseMapper, Browse> implements IBrowseService {
    @Resource
    private BrowseMapper browseMapper;
    @Override
    public void del(Long id) {
        int b = browseMapper.delete(new QueryWrapper<Browse>().eq("user_id", id));

    }
}
