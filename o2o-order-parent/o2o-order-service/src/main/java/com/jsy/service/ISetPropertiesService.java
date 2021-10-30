package com.jsy.service;

import com.jsy.domain.SetProperties;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jsy.vo.SetPropertiesVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yu
 * @since 2021-02-24
 */
public interface ISetPropertiesService extends IService<SetProperties> {

    SetProperties selectByModule(String module);

    int save(SetPropertiesVo setPropertiesVo);

    int updateById(SetPropertiesVo setPropertiesVo);

    
}
