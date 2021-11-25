package com.jsy.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsy.basic.util.PageInfo;
import com.jsy.basic.util.utils.BeansCopyUtils;
import com.jsy.domain.Slideshow;
import com.jsy.dto.SlideshowDto;
import com.jsy.mapper.SlideshowMapper;
import com.jsy.param.SlideshowParam;
import com.jsy.query.SlideshowQuery;
import com.jsy.service.ISlideshowService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 轮播图 服务实现类
 * </p>
 *
 * @author Tian
 * @since 2021-11-25
 */
@Service
public class SlideshowServiceImpl extends ServiceImpl<SlideshowMapper, Slideshow> implements ISlideshowService {

    @Autowired
    private SlideshowMapper slideshowMapper;

    /**
     * 添加轮播图
     * @param slideshowParam
     * @return
     */
    @Override
    @Transactional
    public void addSlideshow(SlideshowParam slideshowParam) {
        Slideshow slideshow = new Slideshow();
        BeanUtils.copyProperties(slideshowParam,slideshow);
        slideshowMapper.insert(slideshow);
    }

    /**
     * 修改轮播图
     * @param slideshowParam
     * @return
     */
    @Override
    @Transactional
    public void updateSlideshow(SlideshowParam slideshowParam) {
        Slideshow slideshow = new Slideshow();
        BeanUtils.copyProperties(slideshowParam,slideshow);
        slideshowMapper.updateById(slideshow);
    }

    /**
     * 删除轮播图
     * @param id
     */
    @Override
    @Transactional
    public void delSlideshow(Long id) {
        slideshowMapper.deleteById(id);
    }

    /**
     * 商城首页轮播图List
     * @return
     */
    @Override
    public List<SlideshowDto> listSlideshow() {
        List<Slideshow> list = slideshowMapper.selectList(null);
        List<SlideshowDto> slideshowDtos = BeansCopyUtils.listCopy(list, SlideshowDto.class);
        return slideshowDtos;
    }


    /**
     * 商城后台轮播图分页
     * @param slideshowQuery 查询对象
     * @return PageInfo 分页对象
     */
    @Override
    public PageInfo<SlideshowDto> pageSlideshow(SlideshowQuery slideshowQuery) {
        Page<Slideshow> page = new Page<>(slideshowQuery.getPage(), slideshowQuery.getRows());
        Page<Slideshow> slideshowPage = slideshowMapper.selectPage(page, null);
        List<Slideshow> records = slideshowPage.getRecords();
        List<SlideshowDto> list = BeansCopyUtils.listCopy(records, SlideshowDto.class);
        PageInfo<SlideshowDto> pageInfo = new PageInfo<>();
        pageInfo.setTotal(slideshowPage.getTotal());
        pageInfo.setRecords(list);
        pageInfo.setCurrent(slideshowPage.getCurrent());
        pageInfo.setSize(slideshowPage.getSize());
        return pageInfo;
    }
}
