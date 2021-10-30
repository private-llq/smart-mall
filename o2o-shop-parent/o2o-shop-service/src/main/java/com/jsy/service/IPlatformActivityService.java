package com.jsy.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jsy.basic.util.PageList;
import com.jsy.domain.PlatformActivity;
import com.jsy.query.PlatformActivityQuery;

/**
 * <p>
 *  服务类
 * </p>
 * @author lijin
 * @since 2020-11-20
 */
public interface IPlatformActivityService extends IService<PlatformActivity> {

    Integer delete(String uuid);

    PageList<PlatformActivity> selectByConditon(PlatformActivityQuery<PlatformActivity> activity);

    Integer saveAndUpdate(PlatformActivity platformActivity);

    PlatformActivity getByUuid(String uuid);
}
