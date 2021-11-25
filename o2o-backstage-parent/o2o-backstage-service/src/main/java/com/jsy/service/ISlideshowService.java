package com.jsy.service;

import com.jsy.basic.util.PageInfo;
import com.jsy.domain.Slideshow;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jsy.dto.SlideshowDto;
import com.jsy.param.SlideshowParam;
import com.jsy.query.SlideshowQuery;

import java.util.List;

/**
 * <p>
 * 轮播图 服务类
 * </p>
 *
 * @author Tian
 * @since 2021-11-25
 */
public interface ISlideshowService extends IService<Slideshow> {

    /**
     * 添加轮播图
     * @param slideshowParam
     * @return
     */
    void addSlideshow(SlideshowParam slideshowParam);

    /**
     * 修改轮播图
     * @param slideshowParam
     */
    void updateSlideshow(SlideshowParam slideshowParam);

    /**
     * 删除轮播图
     * @param id
     */
    void delSlideshow(Long id);

    /**
     * 商城首页轮播图List
     * @return
     */
    List<SlideshowDto> listSlideshow();

    /**
     * 商城后台轮播图分页
     *
     * @param slideshowQuery 查询对象
     * @return PageInfo 分页对象
     */
    PageInfo<SlideshowDto> pageSlideshow(SlideshowQuery slideshowQuery);
}
