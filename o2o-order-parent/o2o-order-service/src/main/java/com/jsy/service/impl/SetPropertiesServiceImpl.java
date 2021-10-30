package com.jsy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jsy.domain.SetProperties;
import com.jsy.mapper.SetPropertiesMapper;
import com.jsy.service.ISetPropertiesService;
import com.jsy.vo.SetPropertiesVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yu
 * @since 2021-02-24
 */
@Service
public class SetPropertiesServiceImpl extends ServiceImpl<SetPropertiesMapper, SetProperties> implements ISetPropertiesService {

    @Autowired
    private SetPropertiesMapper setPropertiesMapper;

    @Override
    public SetProperties selectByModule(String module) {
        SetProperties setProperties = setPropertiesMapper.selectOne(new QueryWrapper<SetProperties>().select("*").eq("for_module",module));
        //SetProperties setProperties = setPropertiesMapper.selectByModule(module);
        return setProperties;
    }

    @Override
    public int save(SetPropertiesVo setPropertiesVo) {
        SetProperties setProperties = new SetProperties();
        BeanUtils.copyProperties(setPropertiesVo,setProperties);
        //如果存在这个模块就失败
        if (!Objects.isNull(this.selectByModule(setProperties.getForModule()))){
            return 0;
        }
        return this.save(setProperties)? 1:0;
    }

    @Override
    public int updateById(SetPropertiesVo setPropertiesVo) {
        SetProperties setProperties = new SetProperties();
        BeanUtils.copyProperties(setPropertiesVo,setProperties);
        return this.updateById(setProperties)?1:0;
    }
}
