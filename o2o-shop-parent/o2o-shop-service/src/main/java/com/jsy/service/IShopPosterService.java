package com.jsy.service;

import com.jsy.domain.ShopPoster;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jsy.vo.SortVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yu
 * @since 2021-03-27
 */
public interface IShopPosterService extends IService<ShopPoster> {

    ShopPoster getPoster(String shopUuid);

    void setSort(String shopUuid,List<SortVo> sortVo);

    void setSort2(String shopUuid,List<SortVo> sortVo);

    List<ShopPoster> listPoster(Integer type,String shopUuid);

    void savePoster(ShopPoster shopPoster);

    void deletePoster(String posterUuid, String shopUuid);
}
