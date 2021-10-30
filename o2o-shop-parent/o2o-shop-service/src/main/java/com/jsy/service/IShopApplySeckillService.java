package com.jsy.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jsy.basic.util.PageList;
import com.jsy.domain.ShopApplySeckill;
import com.jsy.dto.ShopApplySeckillDTO;
import com.jsy.dto.ShopApplySeckillDTOShow;
import com.jsy.query.ShopApplySeckillQuery;
import com.jsy.query.UserApplySeckillQuery;
import java.time.LocalTime;
import java.util.List;
import java.util.TreeMap;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yu
 * @since 2020-12-19
 */
public interface IShopApplySeckillService extends IService<ShopApplySeckill> {


    void submitApplication(ShopApplySeckill shopApplySeckill);

    void reviewApplication(String uuid);

    void rejectApplication(String uuid, String rejectionReasons);

    PageList<ShopApplySeckillDTO> ApplicationPageList(ShopApplySeckillQuery query);

    ShopApplySeckillDTOShow userApplicationPageList(UserApplySeckillQuery query);

    TreeMap<LocalTime,List<ShopApplySeckillDTO>> activityList();

    ShopApplySeckill selectKillInfo(String shopUuid, String goodsUuid);

    boolean updateStock(String shopUuid, String goodsUuid, Integer cartNum);
}
